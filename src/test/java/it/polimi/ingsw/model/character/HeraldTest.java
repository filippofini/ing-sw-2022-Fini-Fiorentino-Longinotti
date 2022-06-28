package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.GameState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the class {@link it.polimi.ingsw.model.character.Herald}.
 */
class HeraldTest {
    Herald herald = new Herald();
    //GameState game_state = new GameState(2, new String[]{"FF", "HH"},new int[]{1,2},false, 1);

    @Test
    public void testSetUses1(){
        herald.setUses();

        assertEquals(1, herald.getUses());
    }
    @Test
    public void testSetUses2(){
        herald.setUses();
        herald.setUses();

        assertNotEquals(1, herald.getUses());
    }

    @Test
    public void testID(){
        assertEquals(3,herald.getID_code());
    }

    @Test
    public void testCost(){
        assertEquals(3, herald.getCost());
    }

    /*
    @Test
    public void testEffect(){
        herald.setIndex_to(5);
        herald.effect(game_state);

        assertEquals(false, game_state.getGT().getIslands().get(4).isMother_nature());
    }

     */
}