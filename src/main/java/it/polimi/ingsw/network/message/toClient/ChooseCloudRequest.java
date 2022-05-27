package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.ViewInterface;

/**
 * Message to choose a cloud.
 */
public class ChooseCloudRequest extends MessagesToClient{

    public ChooseCloudRequest() {super(true);}

    @Override
    public void handleMessage(ViewInterface view) { view.displayChooseCloudRequest();}

    @Override
    public String toString() { return "Asking to choose a cloud";}
}
