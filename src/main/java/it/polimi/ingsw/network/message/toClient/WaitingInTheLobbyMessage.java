package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.View_interface;

/**
 * Message to notify that the player is waiting to start the game.
 */
public class WaitingInTheLobbyMessage extends MessagesToClient{

    public WaitingInTheLobbyMessage() {
        super(false);
    }

    @Override
    public void handleMessage(View_interface view) {
        view.displayWaitingInTheLobbyMessage();
    }

    @Override
    public String toString(){
        return "Sending waiting in the lobby message";
    }
}
