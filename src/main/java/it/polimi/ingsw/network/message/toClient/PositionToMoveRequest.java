package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.ViewInterface;


/**
 * Message to ask the number of moves of mother nature.
 */
public class PositionToMoveRequest extends  MessagesToClient{

    public PositionToMoveRequest(){super(true);}

    @Override
    public void handleMessage(ViewInterface view) {
        view.displayPositionToMoveRequest();
    }

    @Override
    public String toString(){
        return "Asking the desired number of moves";
    }
}
