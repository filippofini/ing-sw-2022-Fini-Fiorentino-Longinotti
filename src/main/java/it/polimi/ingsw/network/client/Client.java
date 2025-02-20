package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.network.message.ConnectionMessage;
import it.polimi.ingsw.network.message.toClient.MessagesToClient;
import it.polimi.ingsw.network.message.toClient.TimeoutExpiredMessage;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Manages the client-side connection to the server. It handles starting the connection,
 * receiving and processing server messages, and properly closing the connection.
 */
public class Client implements ClientInterface {

    public static final int HEARTBEAT = 5000; // A ping message is sent every 5 seconds

    private final String serverIP;
    private final int serverPort;

    private Optional<String> playerName;
    private Optional<GameMode> gameMode;

    private final AtomicBoolean isConnected;
    private boolean connectionClosed;

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private Thread packetReceiverThread;
    private Thread serverMessageReaderThread;
    private Thread pingerThread;

    private BlockingQueue<Object> incomingMessagesQueue;
    private final View view;

    /**
     * Primary constructor that initializes required fields and sets up threads
     * to manage server communication.
     *
     * @param serverIP  the IP of the server
     * @param serverPort the port of the server
     * @param view       the view responsible for handling user interaction
     */
    public Client(String serverIP, int serverPort, View view) {
        this(serverIP, serverPort, view, Optional.empty(), Optional.empty());
    }

    /**
     * Overloaded constructor allowing the client to initialize its name and game mode.
     *
     * @param serverIP   the IP of the server
     * @param serverPort the port of the server
     * @param view       the view responsible for handling user interaction
     * @param gameMode   optional game mode (e.g., expert mode)
     * @param playerName optional player name
     */
    public Client(String serverIP,
                  int serverPort,
                  View view,
                  Optional<GameMode> gameMode,
                  Optional<String> playerName) {

        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.view = view;

        this.playerName = playerName;
        this.gameMode = gameMode;

        this.isConnected = new AtomicBoolean(false);
        this.connectionClosed = false;

        initializeThreads();
    }

    /**
     * Initializes the worker threads used by the client.
     */
    private void initializeThreads() {
        this.packetReceiverThread = new Thread(this::manageIncomingPackets, "PacketReceiverThread");
        this.serverMessageReaderThread = new Thread(this::waitForServerMessages, "ServerMessageReaderThread");

        this.pingerThread = new Thread(() -> {
            while (isConnected.get()) {
                try {
                    Thread.sleep(HEARTBEAT);
                    sendMessageToServer(ConnectionMessage.PING);
                } catch (InterruptedException e) {
                    closeSocket();
                    break;
                }
            }
        }, "PingerThread");
    }

    /**
     * Connects to the server, sets up input/output streams, and starts the packet receiver
     * and server message reader threads.
     *
     * @throws IOException if a connection cannot be established
     */
    public void start() throws IOException {
        socket = new Socket();
        incomingMessagesQueue = new LinkedBlockingQueue<>();

        socket.connect(new InetSocketAddress(serverIP, serverPort));
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());

        isConnected.set(true);

        if (!packetReceiverThread.isAlive()) {
            packetReceiverThread.start();
        }
        serverMessageReaderThread.start();
        pingerThread.start();
    }

    /**
     * Continuously takes messages from the incoming message queue and processes them.
     */
    private void manageIncomingPackets() {
        while (isConnected.get()) {
            try {
                Object message = incomingMessagesQueue.take();
                ((MessagesToClient) message).handleMessage(view);
            } catch (InterruptedException e) {
                closeSocket();
                return;
            }
        }
    }

    /**
     * Waits for messages from the server on the input stream. Handles special cases:
     * - Server requests to close the connection
     * - Timeout messages
     * - Ping messages
     */
    private void waitForServerMessages() {
        try {
            while (isConnected.get()) {
                Object message = inputStream.readObject();
                if (message == ConnectionMessage.CONNECTION_CLOSED) {
                    closeSocket();
                } else if (message instanceof TimeoutExpiredMessage) {
                    handleTimeoutMessage((TimeoutExpiredMessage) message);
                    return;
                } else if (message != null && message != ConnectionMessage.PING) {
                    incomingMessagesQueue.add(message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            pingerThread.interrupt();
            packetReceiverThread.interrupt();
        }
    }

    /**
     * Sends a serializable object to the server through the output stream.
     *
     * @param message the message to send
     */
    @Override
    public synchronized void sendMessageToServer(Serializable message) {
        if (isConnected.get()) {
            try {
                outputStream.writeObject(message);
                outputStream.flush();
            } catch (IOException e) {
                closeSocket();
            }
        }
    }

    /**
     * Closes the client socket and interrupts all active threads.
     * No further messages will be sent or received.
     */
    public void closeSocket() {
        if (connectionClosed) {
            return;
        }
        connectionClosed = true;

        boolean wasConnected = isConnected.getAndSet(false);
        if (!wasConnected) {
            return;
        }

        interruptAllThreads();
        view.handleCloseConnection(wasConnected);
        closeAllResources();
    }

    /**
     * Closes threads and resources typically when the application is exiting
     * or connection is terminated.
     */
    public void closeThreads() {
        interruptAllThreads();
        closeAllResources();
    }

    /**
     * Interrupts all running threads.
     */
    private void interruptAllThreads() {
        if (packetReceiverThread.isAlive()) {
            packetReceiverThread.interrupt();
        }
        if (serverMessageReaderThread.isAlive()) {
            serverMessageReaderThread.interrupt();
        }
        if (pingerThread.isAlive()) {
            pingerThread.interrupt();
        }
    }

    /**
     * Closes the input/output streams and the socket.
     */
    private void closeAllResources() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException ignored) {
        }

        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException ignored) {
        }

        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * Handles the scenario when a TimeoutExpiredMessage is received from the server.
     *
     * @param message the TimeoutExpiredMessage
     */
    private void handleTimeoutMessage(TimeoutExpiredMessage message) {
        isConnected.set(false);
        pingerThread.interrupt();
        packetReceiverThread.interrupt();
        message.handleMessage(view);
    }

    // ------------------ Setters / Getters ------------------

    public void setName(String name) {
        this.playerName = Optional.of(name);
    }

    public void setGameMode(GameMode mode) {
        this.gameMode = Optional.of(mode);
    }

    public Optional<String> getName() {
        return playerName;
    }

    public Optional<GameMode> getGameMode() {
        return gameMode;
    }
}