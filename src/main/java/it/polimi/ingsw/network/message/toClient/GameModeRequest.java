package it.polimi.ingsw.network.message.toClient;


import it.polimi.ingsw.view.View_interface;

/**
 * Message to ask the game mode.
 */
public class GameModeRequest extends MessagesToClient{

    public GameModeRequest() {
        super(true);
    }

    @Override
    public void handleMessage(View_interface view) {
        view.displayGameModeRequest();
    }

    @Override
    public String toString(){
        return "Asking the game mode";
    }
}
