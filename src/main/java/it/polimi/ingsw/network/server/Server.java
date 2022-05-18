package it.polimi.ingsw.network.server;



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
/**
 * Server class that starts a socket server.
 */
public class Server {

    private int port;
    //Thread pool which contains a thread for each client connected to the server
    private final ExecutorService executor;
    //Server socket, used to accept connections from new client, it is constructed only when the server start working
    private ServerSocket serverSocket;
    private int numberOfPlayersForNextGame = -1;
    //private Map<String, Controller> clientsDisconnected;
    //private Map<String, GameOverMessage> clientsDisconnectedGameFinished;
    //private Set<String> takenNicknames;
    //private List<Controller> activeGames;
    //private ReentrantLock lockLobby = new ReentrantLock(true);
    //private ReentrantLock lockGames = new ReentrantLock(true);
    public static final Logger SERVER_LOGGER = Logger.getLogger("Server logger");
    private boolean IsLog;

    public Server(int port, boolean saveLog) {
        this.port = port;
        this.executor = Executors.newCachedThreadPool();


    }
    public void startServer() {
       if(IsLog == true) {
           try {
               serverSocket = new ServerSocket(port);
           } catch (IOException e) {
               SERVER_LOGGER.log(Level.SEVERE, "Impossible open the server on port" + port);
               return;
           }
       }
        SERVER_LOGGER.log(Level.INFO,"Server open on port"+ port);

        try {
            //server accepts connections from clients
            while (true) {
                Socket clientSocket = serverSocket.accept();
                SERVER_LOGGER.log(Level.INFO,"Received connection from address: [" + clientSocket.getInetAddress().getHostAddress() + "]");
               // ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                //executor.submit(clientHandler);
            }
        } catch (IOException e) {
            SERVER_LOGGER.log(Level.SEVERE, "An exception caused the server to stop working.");
        }
    }

    }

