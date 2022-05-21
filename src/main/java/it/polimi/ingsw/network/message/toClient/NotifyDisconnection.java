package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.View_interface;

/**
 * Message to notify the disconnection of a player.
 */
public class NotifyDisconnection extends MessagesToClient{

    String name;

    public NotifyDisconnection(String name) {
        super(false);
        this.name = name;
    }

    @Override
    public void handleMessage(View_interface view) {
        view.displayDisconnection(name);
    }

    public String toString(){
        return "Notifying the disconnection of " + name;
    }
}
