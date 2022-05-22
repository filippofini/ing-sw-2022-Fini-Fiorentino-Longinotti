package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.View_interface;

public class TextMessage extends MessagesToClient{
    private String message;

    public TextMessage(String message){
        super(false);
        this.message = message;
    }

    @Override
    public void handleMessage(View_interface view) {
        view.displayMessage(message);
    }
}
