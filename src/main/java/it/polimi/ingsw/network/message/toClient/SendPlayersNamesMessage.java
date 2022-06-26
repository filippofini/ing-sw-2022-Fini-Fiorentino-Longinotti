package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.ViewInterface;

import java.util.List;

/**
 * Message to notify all the player nickname of the game.
 */
public class SendPlayersNamesMessage extends MessagesToClient{

    String player_name;
    List<String> other_names;

    public SendPlayersNamesMessage(String player_name, List<String> other_names) {
        super(true);
        this.player_name = player_name;
        this.other_names = other_names;
    }

    @Override
    public void handleMessage(ViewInterface view) {
        //view.setNicknames(player_name, other_names);
        //other_names.add(player_name);
        //view.displayPlayersReadyToStartMessage(other_names);
    }


    @Override
    public String toString(){
        return "Sending info about players in game";
    }
}
