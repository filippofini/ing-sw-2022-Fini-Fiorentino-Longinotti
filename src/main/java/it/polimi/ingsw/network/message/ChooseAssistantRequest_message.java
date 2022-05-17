package it.polimi.ingsw.network.message;

/**
 * Message used by the server to request the assistant chosen by the client.
 */
public class ChooseAssistantRequest_message extends Message {

   // private static final long serialVersionUID =;

    /**
     * Constructor of the class.
     * @param name The name of the player.
     */
    public ChooseAssistantRequest_message(String name) {
        super(name, MessageType.CHOOSEASSISTANT_REQUEST);
    }

    @Override
    public String toString() {
        return "ChooseAssistantRequest{" +
                "name=" + getName() +
                '}';
    }
}
