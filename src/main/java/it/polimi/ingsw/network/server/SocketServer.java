package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Socket server manages all new socket connection.
 */
public class SocketServer implements Runnable {

    private final Server server;
    private final int port;
    ServerSocket serverSocket;

    public SocketServer(Server server, int port) {
        this.server = server;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            Server.LOGGER.info(() -> "Socket server started on port " + port + ".");
        } catch (IOException e) {
            Server.LOGGER.severe("Server could not start!");
            return;
        }

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket client = serverSocket.accept();

                client.setSoTimeout(20000);

                SocketClientHandler clientHandler = new SocketClientHandler(this, client);
                Thread thread = new Thread(clientHandler, "ss_handler" + client.getInetAddress());
                thread.start();
            } catch (IOException e) {
                Server.LOGGER.severe("Connection dropped");
            }
        }

    }

    /**
     * This method manages the addition of a new client.
     *
     * @param name      the nickname of the new client.
     * @param clientHandler the ClientHandler of the new client.
     */
    public void addClient(String name, ClientHandler clientHandler) {

        server.addClient(name, clientHandler);
    }

    /**
     * This method forwards a received message from the client to the Server.
     *
     * @param message the message to be forwarded.
     */
    public void onMessageReceived(Message message) {
        server.onMessageReceived(message);
    }

    /**
     * This method manages a client disconnection.
     *
     * @param clientHandler the ClientHandler of the disconnecting client.
     */
    public void onDisconnect(ClientHandler clientHandler) {
        server.onDisconnect(clientHandler);
    }

}
