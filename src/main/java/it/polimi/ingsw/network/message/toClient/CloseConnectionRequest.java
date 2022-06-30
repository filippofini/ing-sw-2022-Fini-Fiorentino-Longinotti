package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.ViewInterface;

/**
 * Message to request the closing of a connection.
 */
public class CloseConnectionRequest extends MessagesToClient{
    public CloseConnectionRequest(){
        super(true);
    }

    @Override
    public void handleMessage(ViewInterface view) {
        view.closeConnection();}

    @Override
    public String toString(){
        return "Asking to close the connection";
    }


}
