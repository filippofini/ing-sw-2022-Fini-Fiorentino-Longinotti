package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.View_interface;

import java.io.Serializable;

/**
 * General features of a message from the server to the client.
 */
public abstract class MessagesToClient implements Serializable {
    private boolean timer;

    public MessagesToClient(boolean timer){
        this.timer = timer;
    }

    public abstract void handleMessage(View_interface view);

    public boolean hasTimer(){
        return timer;
    }
}
