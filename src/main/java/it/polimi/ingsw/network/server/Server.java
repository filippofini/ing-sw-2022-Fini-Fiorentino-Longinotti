package it.polimi.ingsw.network.server;


import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.enumerations.ClientHandlerPhase;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;
import it.polimi.ingsw.network.message.toClient.NumberOfPlayersRequest;
import it.polimi.ingsw.network.message.toClient.WaitingInTheLobbyMessage;

/**
 * Server class that starts a socket server.
 */
public class Server implements ServerInterface {

    private int port;
    //Thread pool which contains a thread for each client connected to the server
    private final ExecutorService executor;
    //Server socket, used to accept connections from new client, it is constructed only when the server start working
    private ServerSocket serverSocket;
    private int numOfPlayersForNextGame = -1;
    private List<ClientHandler> lobby;

    private Set<String> groupOfNicknames;
    private ReentrantLock lockLobby = new ReentrantLock(true);
    private ReentrantLock lockGames = new ReentrantLock(true);
    public static final Logger SERVER_LOGGER = Logger.getLogger("Server logger");
    private boolean IsLog;


    public Server(int port, boolean IsLog) {
        this.port = port;
        this.executor = Executors.newCachedThreadPool();
        this.lobby = new LinkedList<>();
        this.IsLog = IsLog;
    }

    /**
     * Method used to start the server
     */
    public void startServer() {
        if (IsLog)
            InLogger();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            SERVER_LOGGER.log(Level.SEVERE, "Impossible open the server on port " + port);
            return;
        }

        SERVER_LOGGER.log(Level.INFO, "Server open on port " + port);

        try {
            //server accepts connections from clients
            while (true) {
                Socket clientSocket = serverSocket.accept();
                SERVER_LOGGER.log(Level.INFO, "Received connection from address: [" + clientSocket.getInetAddress().getHostAddress() + "]");
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                executor.submit(clientHandler);
            }
        } catch (IOException e) {
            SERVER_LOGGER.log(Level.SEVERE, "An exception caused the server to stop working.");
        }
    }

    /**
     * Method used to initialise the logger file
     */
    private void InLogger() {
        Date date = GregorianCalendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM_HH.mm.ss");
        try {
            FileHandler fh = new FileHandler("server-" + dateFormat.format(date) + ".log");
            fh.setFormatter(new SimpleFormatter());
            SERVER_LOGGER.addHandler(fh);
        } catch (IOException e) {
            SERVER_LOGGER.severe(e.getMessage());
        }
    }

    /**
     * Method used to check the state of the waiting clients. In particular:
     * - If the number of players has not been asked, it sends a request to the first player of the queue
     * - If the number of players is already been decided, it checks whether a game is ready to start. In particular it checks:
     * a) If there are enough players in the lobby
     * b) If the nicknames of the players who will join the game are unique
     * - If both a) and b) are true a new multiplayer game starts
     */
    @Override
    public synchronized void newGameManager() {
        lockLobby.lock();
        try {
            if (numOfPlayersForNextGame == -1 && lobby.size() > 0 && lobby.get(0).getClientHandlerPhase() != ClientHandlerPhase.WAITING_NUMBER_OF_PLAYERS) {
                lobby.get(0).setClientHandlerPhase(ClientHandlerPhase.WAITING_NUMBER_OF_PLAYERS);
                lobby.get(0).sendMessageToClient(new NumberOfPlayersRequest());
            } else if (numOfPlayersForNextGame != -1 && lobby.size() >= numOfPlayersForNextGame) {
                if (!invalidNickname())
                    startNewGame();
            }
        } finally {
            lockLobby.unlock();
        }
    }


    /**
     * Method to check if the nicknames are all different.
     *
     * @return true if all the nicknames are valid
     */
    private boolean invalidNickname() {
        lockLobby.lock();
        try {
            for (int i = 1; i < numOfPlayersForNextGame; i++) {
                if (!lobby.get(i).isValidNickname())
                    return true;
            }
        } finally {
            lockLobby.unlock();
        }
        return false;
    }


    @Override
    public void setNumberOfPlayersForNextGame(ClientHandlerInterface clientHandler, int numOfPlayersForNextGame) {
        this.numOfPlayersForNextGame = numOfPlayersForNextGame;
        newGameManager();
    }

    /**
     * Method to add a client handler to the clientInLobby's list
     *
     * @param clientHandler the client handler to add
     */
    public void addClientHandler(ClientHandler clientHandler) {
        lockLobby.lock();
        lobby.add(clientHandler);
        lockLobby.unlock();
    }


    public synchronized void handleNicknameChoice(ClientHandler connection) {

        groupOfNicknames.add(connection.getNickname());
        connection.setValidNickname(true);

        lockLobby.lock();
        try {
            if (!lobby.contains(connection)) {
                lobby.add(connection);
            }
            newGameManager();
            if (lobby.contains(connection) && connection.getClientHandlerPhase() == ClientHandlerPhase.WAITING_IN_THE_LOBBY)
                connection.sendMessageToClient(new WaitingInTheLobbyMessage());
        } finally {
            lockLobby.unlock();
        }
    }

    //TODO:Mettere un modo per indicare se è expert mode o no; aggiorno le fasi in base al controller.
    private void startNewGame() {
        if (lobby.size() < numOfPlayersForNextGame)
            return;
        GameController controller = new GameController();

        controller.setServer(this);
        lockLobby.lock();
        try {
            List<String> playersInGame = lobby.stream().filter(x -> lobby.indexOf(x) < numOfPlayersForNextGame).map(x -> x.getNickname()).collect(Collectors.toList());

            for (int i = 0; i < numOfPlayersForNextGame; i++) {
                lobby.get(0).setClientHandlerPhase(ClientHandlerPhase.READY_TO_START);
                lobby.get(0).setGameStarted(true);
                controller.addConnection(lobby.get(0));
                lobby.get(0).setController(controller);
                lobby.remove(0);
            }

            for (String nickname : playersInGame) {
                controller.getConnectionByNickname(nickname).sendMessageToClient(new SendPlayersNicknamesMessage(nickname, playersInGame.stream().filter(x -> !(x.equals(nickname))).collect(Collectors.toList())));
            }

            controller.setControllerID(playersInGame.stream().sorted().reduce("", String::concat).hashCode());
            lockGames.lock();
            try {
                activeGames.add(controller);
            } finally {
                lockGames.unlock();
            }
            assert controller != null;
            controller.start();
            numOfPlayersForNextGame = -1;
            if (lobby.size() > 0) {
                lobby.get(0).setClientHandlerPhase(ClientHandlerPhase.WAITING_NUMBER_OF_PLAYERS);
                lobby.get(0).sendMessageToClient(new NumberOfPlayersRequest());
            }
        } finally {
            lockLobby.unlock();
        }
    }


    public void removeConnectionLobby(ClientHandler clientHandler) {

    }

    public void removeGame(GameController controller) {
        activeGames.remove(controller);
    }


    public void gameEnded() {

    }


}

