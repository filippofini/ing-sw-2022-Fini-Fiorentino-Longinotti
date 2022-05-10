package it.polimi.ingsw.network.message;

public class Error_message extends Message{

    private static final long serialVersionUID = 3796309698593755714L;

    private final String error;

    /**
     * Constructor of the class.
     * @param name The name of the player.
     * @param error The message of error.
     */
    public Error_message(String name, String error) {
        super(name, MessageType.ERROR);
        this.error = error;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "Error_message{" +
                "name=" + getName() +
                ", error=" + error +
                '}';
    }
}
