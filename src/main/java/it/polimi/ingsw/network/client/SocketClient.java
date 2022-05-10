package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.message.Error_message;
import it.polimi.ingsw.network.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.ErrorManager;
import java.util.stream.Stream;

/**
 * This class represents a socket client implementation.
 */
public class SocketClient extends Client{

    private final Socket socket;

    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private final ExecutorService executionQueue;
    private final ScheduledExecutorService heartbeat;

    private static final int TIMEOUT = 20000;

    /**
     * Constructor of the class.
     * @param address The address to open the client.
     * @param port The port to connect.
     * @throws IOException
     */
    public SocketClient(String address, int port) throws IOException {
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(address,port), TIMEOUT);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.executionQueue = Executors.newSingleThreadExecutor();
        this.heartbeat = Executors.newSingleThreadScheduledExecutor();
    }


    /**
     * This method sends a message to the server via socket.
     * @param message the message to be sent.
     */
    @Override
    public void sendMessage(Message message) {
        try {
            outputStream.writeObject(message);
            outputStream.reset();
        } catch (IOException e){
            disconnect();
            //notifyObserver(new Error_message(null, "Could not disconnect."));
        }
    }

    @Override
    public void readMessage() {
        executionQueue.execute(() -> {

            while (!executionQueue.isShutdown()) {
                Message message;
                try {
                    message = (Message) inputStream.readObject();
                    Client.LOGGER.info("Received: " + message);
                } catch (IOException | ClassNotFoundException e) {
                    disconnect();
                    message = new Error_message(null, "Connection lost with the server.");
                    disconnect();
                    executionQueue.shutdownNow();
                }
                //notifyObserver(message);
            }
        });
    }

    /**
     * This method disconnect the socket from the server.
     */
    @Override
    public void disconnect() {
        try {
            if (!socket.isClosed()){
                executionQueue.shutdownNow();
                enableHeartbeat(false);
                socket.close();
            }
        }catch (IOException e){
            //notifyObserver(new Error_message(null, "Could not disconnect"));
        }
    }

    /**
     * Enable a heartbeat (ping messages) between client and server sockets to keep the connection alive.
     * @param enabled set this argument to {@code True} to enable the heartbeat.
     *                set to {@code False} to kill the heartbeat.
     */
    @Override
    public void enableHeartbeat(boolean enabled) {
        if (enabled){
            //heartbeat.scheduleAtFixedRate(() -> sendMessage(new PingMessage()),0, 1000, TimeUnit.MILLISECONDS)
        } else {
            heartbeat.shutdownNow();
        }
    }
}
