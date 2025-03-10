package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.network.message.toClient.*;
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

/**
 * Server class that starts a socket server.
 */
public class Server implements ServerInterface {

    private final int port;
    //Thread pool which contains a thread for each client connected to the server
    private final ExecutorService executor;
    //Server socket, used to accept connections from new client, it is constructed only when the server start working
    private ServerSocket serverSocket;
    private int numOfPlayersForNextGame = -1;
    private final List<ClientHandler> lobby;
    private final ReentrantLock lockLobby = new ReentrantLock(true);
    public static final Logger SERVER_LOGGER = Logger.getLogger(
            "Server logger"
    );
    private boolean loggingEnabled;
    private volatile boolean running = false;

    /**
     * Constructor of the class.
     *
     * @param port           The server port.
     * @param loggingEnabled Boolean to check the log.
     */
    public Server(int port, boolean loggingEnabled) {
        this.port = port;
        this.executor = Executors.newCachedThreadPool();
        this.lobby = new LinkedList<>();
        this.loggingEnabled = loggingEnabled;
    }

    /**
     * This method is used to start the server.
     */
    public synchronized void startServer() {
        if (running) {
            SERVER_LOGGER.warning("Server is already running!");
            return;
        }
        running = true;

        if (loggingEnabled) {
            initLogger();
        }

        try {
            serverSocket = new ServerSocket(port);
            SERVER_LOGGER.log(Level.INFO, "Server open on port " + port);

        } catch (IOException e) {
            SERVER_LOGGER.log(Level.SEVERE, "Could not open server on port " + port, e);
            running = false;
            return;
        }

        try {
            //server accepts connections from clients
            while (running) {
                Socket clientSocket = serverSocket.accept();
                SERVER_LOGGER.log(Level.INFO,
                        "Received connection from address: ["
                                + clientSocket.getInetAddress().getHostAddress() + "]");
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                executor.submit(clientHandler);
                wait(100);
                addClientHandler(clientHandler);

                if (lobby.size() == 1) {
                    lobby.get(0).sendMessageToClient(new GameModeRequest());
                    lobby.get(0).sendMessageToClient(new NumberOfPlayersRequest());
                }

                lobby.get(lobby.size() - 1).sendMessageToClient(new NameRequest(false));
                for (int i = 0; i < lobby.size(); i++) {
                    if ((lobby.get(lobby.size() - 1).getNickname().equals(lobby.get(i).getNickname())) && (i != lobby.size() - 1))
                        lobby.get(lobby.size() - 1).sendMessageToClient(new NameRequest(true));
                }

                if (!lobby.isEmpty()) {
                    lobby.get(lobby.size() - 1).sendMessageToClient(new WaitingInTheLobbyMessage());
                }

                if (lobby.size() == numOfPlayersForNextGame) {
                    GameMode mode = lobby.get(0).getGameMode();
                    newGameManager(mode);
                }
            }
        } catch (IOException e) {
            if (running) {
                SERVER_LOGGER.log(Level.SEVERE, "Server socket exception: ", e);
                running = false;
            } else {
                SERVER_LOGGER.log(Level.INFO, "Server has been stopped.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialize the logger file if loggingEnabled is true.
     */
    private void initLogger() {
        Date date = GregorianCalendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM_HH.mm.ss");
        try {
            FileHandler fh = new FileHandler("server-" + dateFormat.format(date) + ".log");
            fh.setFormatter(new SimpleFormatter());
            SERVER_LOGGER.addHandler(fh);
        } catch (IOException e) {
            SERVER_LOGGER.severe("Could not initialize FileHandler for logging: " + e.getMessage());
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
        startNewGame(mode);
        lockLobby.unlock();
    }

    /**
     * This method sets the number of players for the next game.
     * @param clientHandler The client handler interface.
     * @param numOfPlayersForNextGame The number of players for the next game.
     */
    @Override
    public void setNumberOfPlayersForNextGame(ClientHandlerInterface clientHandler, int numOfPlayersForNextGame) {
        this.numOfPlayersForNextGame = numOfPlayersForNextGame;
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
     * This method is used to manage the start of a game.
     * @param mode The game mode.
     */
    private void startNewGame(GameMode mode) {
        if (lobby.size() < numOfPlayersForNextGame) return;

        GameController gamecontroller = new GameController((mode));
        gamecontroller.setServer(this);
        lockLobby.lock();
        try {
            for (int i = 0; i < numOfPlayersForNextGame; i++) {
                lobby.get(0).setGameStarted(true);
                gamecontroller.addConnection(lobby.get(0));
                lobby.get(0).setGameController(gamecontroller);
                lobby.remove(0);
            }
            gamecontroller.start();
            numOfPlayersForNextGame = -1;
        } catch (IOException e) {
            SERVER_LOGGER.log(Level.SEVERE, "Error starting game controller: ", e);
        } finally {
            lockLobby.unlock();
        }
    }

    /**
     * This method is used to manage the end of the game.
     * It removes all nicknames from the list and sends the results message to the players.
     * @param gamecontroller The game controller.
     * @param resultsNotify The message used to notify the results of the game.
     */
    public void gameEnded(GameController gamecontroller, ResultsNotify resultsNotify) throws IOException {
        gamecontroller.sendMessageToAll(resultsNotify);
        serverSocket.close();
    }

    /**
     * This method removes the connection with the clients after the game ends.
     * @param connection The connection from the client handler.
     */
    public void removeConnectionGame(ClientHandler connection) {
        connection.getGameController().removeConnection(connection);
    }
}