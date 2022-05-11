package it.polimi.ingsw.network.message;

/**
 * Message used to keep the connection alive.
 */
public class Ping_message extends Message {

    private static final long serialVersionUID = -7019523659587734169L;

    /**
     * Constructor of the class.
     */
    public Ping_message() {
        super(null, MessageType.PING);
    }
}
