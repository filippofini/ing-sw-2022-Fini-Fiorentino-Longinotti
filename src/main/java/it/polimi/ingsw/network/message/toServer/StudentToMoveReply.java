package it.polimi.ingsw.network.message.toServer;

import it.polimi.ingsw.network.server.ClientHandlerInterface;
import it.polimi.ingsw.network.server.ServerInterface;

public class StudentToMoveReply implements MessagesToServer{
    private final int num_stud;

    public StudentToMoveReply(int num_stud){this.num_stud=num_stud;}

    public void handleMessage(ServerInterface server, ClientHandlerInterface clientHandler) {
        //TODO:change phase only when all the students are moved
        //clientHandler.setClientHandlerPhase(ClientHandlerPhase.WAITING_STUDENT_TO_TRANSFER);
        clientHandler.setstudToMove(num_stud);
    }

    @Override
    public String toString() {
        return "Student Number Received";
    }
}
