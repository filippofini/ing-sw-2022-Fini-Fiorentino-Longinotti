package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.view.ViewInterface;

public class displayEntranceStudentsRequest extends MessagesToClient {
    Board board;
    displayEntranceStudentsRequest(Board board){
        super(true);
        this.board=board;
    }
    @Override
    public void handleMessage(ViewInterface view) {
        view.displayEntranceStudents(board);}
}
