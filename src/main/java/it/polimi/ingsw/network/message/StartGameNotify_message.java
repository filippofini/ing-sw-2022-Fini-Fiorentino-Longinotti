package it.polimi.ingsw.network.message;

/**
 * Message used by the server to notify that the game is started to the client.
 */
public class StartGameNotify_message extends Message {
    //private static final long serialVersionUID =;

    /**
     * Constructor of the class.
     * @param name the name of the player.
     */
    public StartGameNotify_message(String name) {
        super(name, MessageType.STARTGAME_NOTIFY);
    }

    @Override
    public String toString() {
        return "StartGameNotify{" +
                "name=" + getName() +
                '}';
    }


}
