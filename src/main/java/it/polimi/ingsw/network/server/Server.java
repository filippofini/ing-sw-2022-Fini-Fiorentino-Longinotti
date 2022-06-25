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
import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.network.message.toClient.NumberOfPlayersRequest;
import it.polimi.ingsw.network.message.toClient.ResultsNotify;
import it.polimi.ingsw.network.message.toClient.SendPlayersNamesMessage;
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
    public static final Logger SERVER_LOGGER = Logger.getLogger("Server logger");
    private boolean IsLog;


    /**
     * Constructor of the class.
     * @param port The server port.
     * @param IsLog Boolean to check the log.
     */
    public Server(int port, boolean IsLog) {
        this.port = port;
        this.executor = Executors.newCachedThreadPool();
        this.lobby = new LinkedList<>();
        this.IsLog = IsLog;
    }

    /**
     * This method is used to start the server.
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
     * This method is used to initialise the logger file.
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
     * This method is used to check the state of the waiting clients. In particular:
     * - If the number of players has not been asked, it sends a request to the first player of the queue
     * - If the number of players is already been decided, it checks whether a game is ready to start. In particular it checks:
     * a) If there are enough players in the lobby
     * b) If the nicknames of the players who will join the game are unique
     * - If both a) and b) are true a new multiplayer game starts.
     * @param mode The game mode.
     */
    @Override
    public synchronized void newGameManager(GameMode mode) {
        lockLobby.lock();
        try {
            if (numOfPlayersForNextGame == -1 && lobby.size() > 0 && lobby.get(0).getClientHandlerPhase() != ClientHandlerPhase.WAITING_NUMBER_OF_PLAYERS) {
                lobby.get(0).setClientHandlerPhase(ClientHandlerPhase.WAITING_NUMBER_OF_PLAYERS);
                lobby.get(0).sendMessageToClient(new NumberOfPlayersRequest());
            } else if (numOfPlayersForNextGame != -1 && lobby.size() >= numOfPlayersForNextGame) {
                if (!invalidNickname())
                    startNewGame(mode);
            }
        } finally {
            lockLobby.unlock();
        }
    }


    /**
     * This method is used to check if the nicknames are all different.
     * @return {@code True} if all the nicknames are valid, {@code False} if not.
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


    /**
     * This method sets the number of players for the next game.
     * @param clientHandler The client handler interface.
     * @param numOfPlayersForNextGame The number of players for the next game.
     */
    @Override
    public void setNumberOfPlayersForNextGame(ClientHandlerInterface clientHandler, int numOfPlayersForNextGame) {
        this.numOfPlayersForNextGame = numOfPlayersForNextGame;
        GameMode mode = clientHandler.getGameMode();
        newGameManager(mode);
    }

    /**
     * This method is used to add a client handler to the clientInLobby's list.
     * @param clientHandler The client handler to add.
     */
    public void addClientHandler(ClientHandler clientHandler) {
        lockLobby.lock();
        lobby.add(clientHandler);
        lockLobby.unlock();
    }


    /**
     * This method is used to handle the nickname choice (if the nickname is valid).
     * @param connection The connection from the client handler.
     */
    public synchronized void handleNicknameChoice(ClientHandler connection) {

        groupOfNicknames.add(connection.getNickname());
        connection.setValidNickname(true);

        lockLobby.lock();
        try {
            if (!lobby.contains(connection)) {
                lobby.add(connection);
            }
            newGameManager(connection.getGameMode());
            if (lobby.contains(connection) && connection.getClientHandlerPhase() == ClientHandlerPhase.WAITING_IN_THE_LOBBY)
                connection.sendMessageToClient(new WaitingInTheLobbyMessage());
        } finally {
            lockLobby.unlock();
        }
    }

    /**
     * This method is used to manage the start of a game.
     * @param mode The game mode.
     */
    private void startNewGame(GameMode mode) {
        if (lobby.size() < numOfPlayersForNextGame)
            return;
        GameController gamecontroller = new GameController((mode));
        gamecontroller.setServer(this);
        lockLobby.lock();
        try {
            List<String> playersInGame = lobby.stream().filter(x -> lobby.indexOf(x) < numOfPlayersForNextGame).map(x -> x.getNickname()).collect(Collectors.toList());

            for (int i = 0; i < numOfPlayersForNextGame; i++) {
                lobby.get(0).setClientHandlerPhase(ClientHandlerPhase.READY_TO_START);
                lobby.get(0).setGameStarted(true);
                gamecontroller.addConnection(lobby.get(0));
                lobby.get(0).setGameController(gamecontroller);
                lobby.remove(0);
            }

            for (String nickname : playersInGame) {
                gamecontroller.getConnectionByNickname(nickname).sendMessageToClient(new SendPlayersNamesMessage(nickname, playersInGame.stream().filter(x -> !(x.equals(nickname))).collect(Collectors.toList())));
            }


            assert gamecontroller != null;
            gamecontroller.start();
            numOfPlayersForNextGame = -1;
            if (lobby.size() > 0) {
                lobby.get(0).setClientHandlerPhase(ClientHandlerPhase.WAITING_NUMBER_OF_PLAYERS);
                lobby.get(0).sendMessageToClient(new NumberOfPlayersRequest());
            }
        } finally {
            lockLobby.unlock();
        }
    }



    /**
     * This method is used to handle disconnection of clients that has not been added to a game.
     * @param connection The connection from the client handler.
     */
    public void removeConnectionLobby(ClientHandler connection){
        int position = -1;
        try{
            lockLobby.lock();
            //If the client has already taken a valid nickname, I remove it from the list.
            if (connection.getClientHandlerPhase() != ClientHandlerPhase.WAITING_NICKNAME && connection.getClientHandlerPhase() !=ClientHandlerPhase.WAITING_GAME_MODE)
                groupOfNicknames.remove(connection.getNickname());
            position = lobby.indexOf(connection);
            if (position > -1) {
                lobby.remove(connection);
                groupOfNicknames.remove(connection.getNickname());
                if (position == 0)
                    numOfPlayersForNextGame = -1;
                if(position < numOfPlayersForNextGame || numOfPlayersForNextGame == -1)
                    newGameManager(connection.getGameMode());
            }
        }
        finally {
            lockLobby.unlock();
        }
    }


    /**
     * This method is used to manage the end of the game.
     * It removes all nicknames from the list and sends the results message to the players.
     * @param gamecontroller The game controller.
     * @param resultsNotify The message used to notify the results of the game.
     */
    public void gameEnded(GameController gamecontroller, ResultsNotify resultsNotify) {
        gamecontroller.getPlayers_ID().forEach(x -> groupOfNicknames.remove(x.getNickname()));
        gamecontroller.sendMessageToAll(resultsNotify);

    }


    /**
     * This method removes the connection with the clients after the game ends.
     * @param connection The connection from the client handler.
     */
    public void removeConnectionGame(ClientHandler connection) {
        groupOfNicknames.remove(connection.getNickname());
        connection.getGameController().removeConnection(connection);
    }

    /**
     * This method removes the nickname from the list.
     * @param nickname The nickname to be removed.
     */
    public void removeNickname(String nickname) {
        groupOfNicknames.remove(nickname);
    }
}

