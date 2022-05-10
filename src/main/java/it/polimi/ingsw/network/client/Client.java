package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.message.Message;

import java.util.logging.Logger;

/**
 * Abstract class to communicate with the server. Every type of connection must implement this interface.
 */
public abstract class Client {

    public static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    /**
     * This method sends a message to the server.
     * @param message the message to be sent.
     */
    public abstract void sendMessage(Message message);

    /**
     * This method reads a message from the server and notifies the ClientController.
     */
    public abstract void readMessage();


    /**
     * This method is used to disconnect from the server.
     */
    public abstract void disconnect();


    /**
     * This method enables a heartbeat to keep the connection alive.
     * @param enabled it's set to {@code True} to enable the heartbeat, it's set to {@code False} to kill the heartbeat.
     */
    public abstract void enableHeartbeat(boolean enabled);



}
