package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.ViewInterface;

public class UseCharacterCardRequest extends MessagesToClient{

    public UseCharacterCardRequest(){super(true);}

    @Override
    public void handleMessage(ViewInterface view) {
        view.UseCharacterCard();}
}
