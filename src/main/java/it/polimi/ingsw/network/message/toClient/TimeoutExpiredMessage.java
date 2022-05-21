package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.View_interface;

/**
 * Message to notify that the timeout has expired.
 */
public class TimeoutExpiredMessage extends MessagesToClient{
    public TimeoutExpiredMessage(){
        super(false);
    }

    @Override
    public void handleMessage(View_interface view){
        view.displayTimeoutExpiredMessage();
    }
}
