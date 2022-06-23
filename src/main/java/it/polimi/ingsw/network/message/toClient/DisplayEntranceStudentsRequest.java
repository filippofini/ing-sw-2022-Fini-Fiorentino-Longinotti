package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.view.ViewInterface;

/**
 * Message to display the entrance of students.
 */
public class DisplayEntranceStudentsRequest extends MessagesToClient {
    Board board;
    DisplayEntranceStudentsRequest(Board board){
        super(true);
        this.board=board;
    }
    @Override
    public void handleMessage(ViewInterface view) {
        view.displayEntranceStudents(board);}

    @Override
    public String toString(){
        return "Asking to which island move the students";
    }
}
