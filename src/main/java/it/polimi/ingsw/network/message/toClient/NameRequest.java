package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.ViewInterface;

/**
 * Message to request a name from the new player.
 */
public class NameRequest extends MessagesToClient {

    private final boolean isRetry;

    public NameRequest(boolean is_retry) {
        super(true);
        this.isRetry = is_retry;
    }

    @Override
    public void handleMessage(ViewInterface view) {
        view.displayNicknameRequest(isRetry);
    }

    @Override
    public String toString() {
        return "asking nickname";
    }
}
