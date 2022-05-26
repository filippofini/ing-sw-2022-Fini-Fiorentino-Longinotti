package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.ViewInterface;

/**
 * Message to choose if move student to board or island.
 */
public class ChooseIslandOrBoardRequest extends MessagesToClient{

    public ChooseIslandOrBoardRequest() {
        super(true);
    }

    @Override
    public void handleMessage(ViewInterface view) {
        view.displayWhereToMoveStudents();
    }

    @Override
    public String toString(){
        return "Asking where to move the students";
    }
}
