package it.polimi.ingsw.network.message;

import java.io.Serializable;

/**
 * This class must be extended by each message type.
 * Server and clients will communicate using this generic type of message.
 * This avoids the usage of the "instance of" primitive.
 */
public abstract class Message implements Serializable {
    private static final long serialVersionUID = 6589184250663958343L;
    private final String name;
    private MessageType messageType;
    private boolean timer;

    /**
     * Constructor of the class.
     * @param name The name of the player.
     * @param messageType The type of message.
     */
    Message(String name, MessageType messageType){
        this.name = name;
        this.messageType = messageType;
    }

    public String getName() {
        return name;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public boolean hasTimer() {
        return timer;
    }

    @Override
    public String toString() {
        return "Message{" + "name=" + name + ", messageType=" + messageType + '}';
    }

}
