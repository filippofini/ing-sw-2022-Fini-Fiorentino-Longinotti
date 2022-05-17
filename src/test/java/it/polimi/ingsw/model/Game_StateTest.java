package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the class {@link it.polimi.ingsw.model.Game_State}.
 */
class Game_StateTest {

    Game_State game_state = new Game_State(2, new String[]{"FF","HH"}, new int[]{1,2}, false, 1);
    Game_State game_state2 = new Game_State(4,new String[]{"FF","HH","GG","LL"}, new int[]{1,2,3,4}, false, 1);

    private Player[] players = new Player[2];

    @Test
    public void testGetGT(){
        Game_State Gs = new Game_State(2, new String[]{"FF","HH"}, new int[]{1,2}, true, 1);

        assertNotEquals(game_state.getGT(), Gs.getGT());
    }

    @Test
    public void testGetPlayers() {
        players[0]= new Player("FF",1,Tower_colour.STARTER,1);
        players[1]= new Player("HH",2,Tower_colour.GREY,2);
        assertArrayEquals(players,game_state.getPlayers());
    }

    @Test
    void testValue(){
       game_state.getPlayers()[1].setChosen_card(Assistance_card.ELEPHANT);

       assertNotEquals(30, game_state.getPlayers()[1].getChosen_card().getValue());
    }
}