package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.ViewInterface;

public class TextMessage extends MessagesToClient{
    private String message;

    public TextMessage(String message){
        super(false);
        this.message = message;
    }

    @Override
    public void handleMessage(ViewInterface view) {
        view.displayMessage(message);
    }
}
