package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.View_interface;

public class NameRequest extends MessagesToClient{
    private boolean is_retry;
    private boolean taken;

    public NameRequest(boolean is_retry, boolean taken){
        super(true);
        this.is_retry = is_retry;
        this.taken = taken;
    }

    @Override
    public void handleMessage(View_interface view) {
        view.displayNicknameRequest(is_retry, taken);
    }

    @Override
    public String toString(){
        return "asking nickname" + ((taken) ? " because the old one was already taken" : "");
    }
}
