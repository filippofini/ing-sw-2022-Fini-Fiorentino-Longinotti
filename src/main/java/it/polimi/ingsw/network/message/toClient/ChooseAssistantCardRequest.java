package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.ViewInterface;

/**
 * Message to choose an assistant card.
 */
public class ChooseAssistantCardRequest extends MessagesToClient {

    public ChooseAssistantCardRequest(){ super(true);}


    @Override
    public void handleMessage(ViewInterface view) { view.displayChooseAssistantCardRequest();}

    @Override
    public String toString() {return "Asking to choose an assistant card";}

}
