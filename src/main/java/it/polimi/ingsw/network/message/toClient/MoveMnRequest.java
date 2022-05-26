package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.ViewInterface;


/**
 * Message to ask to move mother nature.
 */
public class MoveMnRequest extends MessagesToClient{

    public MoveMnRequest(){
        super(true);
    }


    @Override
    public void handleMessage(ViewInterface view) {
        view.displayMoveMnRequest();
    }

    @Override
    public String toString(){
        return "Asking to move mother nature";
    }
}
