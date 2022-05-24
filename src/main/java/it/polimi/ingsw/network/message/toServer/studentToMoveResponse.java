package it.polimi.ingsw.network.message.toServer;

import it.polimi.ingsw.enumerations.ClientHandlerPhase;
import it.polimi.ingsw.network.server.ClientHandlerInterface;
import it.polimi.ingsw.network.server.Server_interface;

public class studentToMoveResponse implements MessagesToServer{
    private final int num_stud;

    public studentToMoveResponse(int num_stud){this.num_stud=num_stud;}

    public void handleMessage(Server_interface server, ClientHandlerInterface clientHandler) {
        //TODO:change phase only when all the students are moved
        //clientHandler.setClientHandlerPhase(ClientHandlerPhase.WAITING_STUDENT_TO_TRANSFER);
        clientHandler.setstudToMove(num_stud);
    }

    @Override
    public String toString() {
        return "Student Number Received";
    }
}
