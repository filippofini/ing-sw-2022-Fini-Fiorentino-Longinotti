package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.ViewInterface;


/**
 * Message to inform that the game is started.
 */
public class StartGameNotify extends MessagesToClient{

    public StartGameNotify() {
        super(true);
    }

    @Override
    public void handleMessage(ViewInterface view) {
        view.displayStartGameNotify();
    }

    @Override
    public String toString() { return "The game is started";}
}
