package it.polimi.ingsw.network.message.toServer;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.server.ClientHandlerInterface;
import it.polimi.ingsw.network.server.ServerInterface;

public class ChooseCharacterCardReply implements MessagesToServer{
    private final int choice;
    public ChooseCharacterCardReply(int choice){
        this.choice=choice;
    }
    public void handleMessage(ServerInterface server, ClientHandlerInterface clientHandler) {

        clientHandler.setChCardUsed(choice);
        clientHandler.setCanBeUsed(true);
    }

    @Override
    public String toString() {
        return "Character card chosen Received";
    }
}
