package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.ViewInterface;

/**
 * Message to inform that the game is ended.
 */
//TODO: Inserire la possibilità di sapere il motivo per cui il giocatore ha vinto.
public class EndGameNotify extends MessagesToClient{

    public EndGameNotify() {
        super(true);
    }


    @Override
    public void handleMessage(ViewInterface view) {
        view.displayEndGameNotify();
    }

    @Override
    public String toString() {return "The game is ended";}
}
