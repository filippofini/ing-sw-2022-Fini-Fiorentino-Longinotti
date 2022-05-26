package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.ViewInterface;

/**
 * Message to show to the client which island has conquered.
 */
public class IslandConqueredNotify extends MessagesToClient{

    int[] conquered;

    public IslandConqueredNotify(int[] conquered) {
        super(true);
        this.conquered = conquered;
    }

    @Override
    public void handleMessage(ViewInterface view) {
        view.displayConqueredIslands(conquered);
    }

    @Override
    public String toString(){
        return "Sending player the numbers of conquered island";
    }
}
