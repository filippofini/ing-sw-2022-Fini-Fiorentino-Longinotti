package it.polimi.ingsw.network.message;

import java.io.Serializable;

/**
 * this class must be extended by each message type.
 */
public abstract class Message implements Serializable {
    private static final long serialVersionUID = 6589184250663958343L;
    private final String name = null;
    private MessageType messageType;

    public String getName() {
        return name;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        return "Message{" + "name=" + name + ", messageType=" + messageType + '}';
    }

}
