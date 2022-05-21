package it.polimi.ingsw.network.message.toServer;

import it.polimi.ingsw.enumerations.ClientHandlerPhase;
import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.network.message.toClient.NameRequest;
import it.polimi.ingsw.network.server.ClientHandlerInterface;
import it.polimi.ingsw.network.server.Server_interface;

/**
 * Message with the chosen game mode by the client.
 */
public class GameModeReply implements MessagesToServer{
    private final GameMode gameMode;

    public GameModeReply(GameMode gameMode){
        this.gameMode = gameMode;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    @Override
    public void handleMessage(Server_interface server, ClientHandlerInterface clientHandler){
        if (clientHandler.getClientHandlerPhase() == ClientHandlerPhase.WAITING_GAME_MODE){
            clientHandler.setGameMode(gameMode);
            clientHandler.setClientHandlerPhase(ClientHandlerPhase.WAITING_NICKNAME);
            clientHandler.sendMessageToClient(new NameRequest(false, false));
        }
    }

    @Override
    public String toString() {
        return "Received game mode: " + gameMode.name().replace("_"," ");
    }
}
