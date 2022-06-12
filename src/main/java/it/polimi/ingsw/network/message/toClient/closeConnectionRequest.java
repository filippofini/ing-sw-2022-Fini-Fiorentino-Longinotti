package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.ViewInterface;

public class closeConnectionRequest extends MessagesToClient{
    closeConnectionRequest(){
        super(true);
    }
    public void handleMessage(ViewInterface view) {
        view.closeConnection();}
}
