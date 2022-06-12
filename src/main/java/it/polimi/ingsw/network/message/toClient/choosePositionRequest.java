package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.ViewInterface;

public class choosePositionRequest extends MessagesToClient{
    int upperLimit;
    choosePositionRequest(int upperLimit){
        super(true);
        this.upperLimit=upperLimit;
    }
    @Override
    public void handleMessage(ViewInterface view) {
        view.choosePositionRequest(upperLimit);}
}
