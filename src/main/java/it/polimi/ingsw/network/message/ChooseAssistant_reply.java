package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Assistance_card;

/**
 * Message used to send to the server the Assistant chosen by the client.
 */
public class ChooseAssistant_reply extends Message{

    //private static final long serialVersionUID =;
    private Assistance_card card;

    /**
     * Constructor of the class.
     * @param name The name of the player.
     * @param card The Assistant card.
     */
    public ChooseAssistant_reply(String name, Assistance_card card) {
        super(name, MessageType.CHOOSEASSISTANT_REPLY);
        this.card = card;
    }

    public Assistance_card getAssistant_Card() {
        return card;
    }

    @Override
    public String toString() {
        return "AssistanceCard_reply{" +
                "name=" + getName() +
                ", card=" + card +
                '}';
    }
}
