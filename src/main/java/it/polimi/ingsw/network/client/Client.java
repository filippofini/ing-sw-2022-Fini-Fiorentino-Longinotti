package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.message.Message;

import java.util.logging.Logger;

public abstract class Client {

    public static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    /**
     * this method sends a message to the server.
     * @param message the message to be sent.
     */
    public abstract void sendMessage(Message message);

    /**
     * this method reads a message from the server and notifies the ClientController.
     */
    public abstract void readMessage();


    /**
     * this method is used to disconnect from the server.
     */
    public abstract void disconnect();


    /**
     * this method enables a heartbeat to keep the connection alive.
     * @param enabled it's set to {@code true} to enable the heartbeat.
     *                it's set to {@code false} to kill the heartbeat.
     *
     */
    public abstract void enableHeartbeat(boolean enabled);



}
