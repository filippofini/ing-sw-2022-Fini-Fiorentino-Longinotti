package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the class {@link GameState}.
 */
/*
class GameStateTest {

    List<Player> players = new ArrayList<>();

    GameState game_state = new GameState(2, new String[]{"FF","HH"}, new int[]{1,2}, false, 1,);
    GameState game_state2 = new GameState(4,new String[]{"FF","HH","GG","LL"}, new int[]{1,2,3,4}, false, 1,);

    private Player[] players = new Player[2];

    @Test
    public void testGetGT(){
        GameState Gs = new GameState(2, new String[]{"FF","HH"}, new int[]{1,2}, true, 1);

        assertNotEquals(game_state.getGT(), Gs.getGT());
    }

    @Test
    public void testGetPlayers() {
        players[0]= new Player("FF",1, TowerColour.STARTER,1);
        players[1]= new Player("HH",2, TowerColour.GREY,2);
        assertArrayEquals(players,game_state.getPlayers());
    }

    @Test
    void testValue(){
       game_state.getPlayers()[1].setChosen_card(AssistanceCard.ELEPHANT);

       assertNotEquals(30, game_state.getPlayers()[1].getChosen_card().getValue());
    }
}

 */