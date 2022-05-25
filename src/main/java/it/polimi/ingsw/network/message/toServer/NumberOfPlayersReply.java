package it.polimi.ingsw.network.message.toServer;

import it.polimi.ingsw.enumerations.ClientHandlerPhase;
import it.polimi.ingsw.network.message.toClient.WaitingInTheLobbyMessage;
import it.polimi.ingsw.network.server.ClientHandlerInterface;
import it.polimi.ingsw.network.server.ServerInterface;

/**
 * Class to communicate the number of players desired.
 */
public class NumberOfPlayersReply implements MessagesToServer{
    private final int n_players;

    public NumberOfPlayersReply(int n_players){
        this.n_players = n_players;
    }

    @Override
    public void handleMessage(ServerInterface server, ClientHandlerInterface clientHandler) {
        if (clientHandler.getClientHandlerPhase() == ClientHandlerPhase.WAITING_NUMBER_OF_PLAYERS) {
            clientHandler.sendMessageToClient(new WaitingInTheLobbyMessage());
            clientHandler.setClientHandlerPhase(ClientHandlerPhase.WAITING_IN_THE_LOBBY);
            clientHandler.setNumberOfPlayersForNextGame(n_players);
        }
    }

    @Override
    public String toString() {
        return "Received number of players";
    }
}
