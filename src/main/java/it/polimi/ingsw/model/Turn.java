package it.polimi.ingsw.model;

import java.util.Random;

/**
 * Class turn
 */
public class Turn{

    private int current_player;

    private int player_next_turn;

    public Turn(){

        current_player=0;

    }


    public int getCurrent_player() {
        return current_player;
    }

    public void setCurrent_player(int current_player) {
        this.current_player = current_player;
    }
}
