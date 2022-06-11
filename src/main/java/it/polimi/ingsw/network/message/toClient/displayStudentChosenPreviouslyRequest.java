package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.view.ViewInterface;

public class displayStudentChosenPreviouslyRequest extends MessagesToClient {
    Board board;
    int choiceStudent;
    public displayStudentChosenPreviouslyRequest(Board board,int choiceStudent){
        super(true);
        this.board=board;
        this.choiceStudent=choiceStudent;
    }
    public void handleMessage(ViewInterface view) {
        view.displayStudentChosenPreviously(board,choiceStudent);}

}
