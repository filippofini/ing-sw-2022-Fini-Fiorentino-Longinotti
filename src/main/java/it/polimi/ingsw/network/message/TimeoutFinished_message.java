package it.polimi.ingsw.network.message;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.View_interface;

public class TimeoutFinished_message extends Message{
    public TimeoutFinished_message(String name) {
        super(name,MessageType.TIMEOUTFINISHED);
    }
public void handleMessage(View_interface view) {
        view.displayTimeoutFinished_message();
}
}
