package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.ViewInterface;

/**
 * This message is used to show the results of the game.
 */
public class ResultsNotify extends MessagesToClient {

    String FirstPlace;
    String SecondPlace;
    String ThirdPlace;

    String[] Winners;
    String[] Losers;

    public ResultsNotify(String FirstPlace, String SecondPlace) {
        super(true);
        this.FirstPlace = FirstPlace;
        this.SecondPlace = SecondPlace;
    }
    public ResultsNotify(String FirstPlace, String SecondPlace, String ThirdPlace) {
        super(true);
        this.FirstPlace = FirstPlace;
        this.SecondPlace = SecondPlace;
        this.ThirdPlace = ThirdPlace;
    }

    public ResultsNotify(String[] Winners, String[] Losers) {
        super(true);
        this.Winners = Winners;
        this.Losers = Losers;
    }

    @Override
    public void handleMessage(ViewInterface view) {
        view.displayResults();
    }

    @Override
    public String toString(){
        return "Shows the results of the game";
    }
}
