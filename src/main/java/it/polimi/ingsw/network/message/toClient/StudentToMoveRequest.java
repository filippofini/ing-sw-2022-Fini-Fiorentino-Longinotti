package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.ViewInterface;

/**
 * Message to ask the student to be moved.
 */
public class StudentToMoveRequest extends MessagesToClient{

    public StudentToMoveRequest(){super(true);}

    @Override
    public void handleMessage(ViewInterface view) {
        view.displayStudentToMoveRequest();
    }

    @Override
    public String toString(){
        return "Asking which student to move";
    }
}
