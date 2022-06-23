package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.ViewInterface;

/**
 * Message to request the position.
 */
public class ChoosePositionRequest extends MessagesToClient{
    int upperLimit;
    ChoosePositionRequest(int upperLimit){
        super(true);
        this.upperLimit=upperLimit;
    }
    @Override
    public void handleMessage(ViewInterface view) {
        view.choosePositionRequest(upperLimit);
    }

    @Override
    public String toString(){
        return "Asking to choose a position";
    }

}
