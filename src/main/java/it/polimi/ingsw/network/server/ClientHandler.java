package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.message.Message;

public interface ClientHandler {

    /**
     * this method returns the connection status.
     *
     * @return {@code true} if the client is still connected and reachable, {@code false} otherwise.
     */
    boolean isConnected();

    /**
     * this method disconnects from the client.
     */
    void disconnect();

    /**
     * this method sends a message to the client.
     *
     * @param message the message to be sent.
     */
    void sendMessage(Message message);
}
