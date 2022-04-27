package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the class {@link it.polimi.ingsw.model.Game_State}.
 */
class Game_StateTest {

    Game_State game_state = new Game_State(2, new String[]{"FF","HH"}, new int[]{1,2}, false);

    @Test
    public void testGetGT(){
        Game_State Gs = new Game_State(2, new String[]{"FF","HH"}, new int[]{1,2}, true);

        assertNotEquals(game_state.getGT(), Gs.getGT());
    }
}