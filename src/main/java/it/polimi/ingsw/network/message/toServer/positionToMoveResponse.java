package it.polimi.ingsw.network.message.toServer;

import it.polimi.ingsw.enumerations.ClientHandlerPhase;
import it.polimi.ingsw.network.message.toClient.NameRequest;
import it.polimi.ingsw.network.server.ClientHandlerInterface;
import it.polimi.ingsw.network.server.Server_interface;

public class positionToMoveResponse implements MessagesToServer{
    private final int pos;

    public positionToMoveResponse(int pos){this.pos=pos;}

    public int getPos() {
        return pos;
    }

    public void handleMessage(Server_interface server, ClientHandlerInterface clientHandler) {

        clientHandler.setClientHandlerPhase(ClientHandlerPhase.WAITING_STUDENT_TO_TRANSFERE);
        clientHandler.setposToMove(pos);
    }

    @Override
    public String toString() {
        return "Position Received";
    }

}
