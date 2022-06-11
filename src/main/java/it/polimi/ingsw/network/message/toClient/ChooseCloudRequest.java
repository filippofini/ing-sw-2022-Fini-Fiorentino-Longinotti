package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.ViewInterface;

import java.util.List;

/**
 * Message to choose a cloud.
 */
public class ChooseCloudRequest extends MessagesToClient{
    List<Cloud> clouds;
    Player player;
    public ChooseCloudRequest(List<Cloud> clouds,Player player) {
        super(true);
        this.clouds=clouds;
        this.player=player;
    }

    @Override
    public void handleMessage(ViewInterface view) { view.displayChooseCloudRequest(clouds,player);}

    @Override
    public String toString() { return "Asking to choose a cloud";}
}
