package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Game_State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the class {@link it.polimi.ingsw.model.character.Centaur}.
 */
class CentaurTest {

    Centaur centaur = new Centaur();
    Game_State game_state = new Game_State(2, new String[]{"FF", "HH"},new int[]{1,2},false, 1);

    @Test
    public void testSetUses1(){
        centaur.setUses();

        assertEquals(1, centaur.getUses());
    }
    @Test
    public void testSetUses2(){
        centaur.setUses();
        centaur.setUses();

        assertNotEquals(1, centaur.getUses());
    }

    @Test
    public void testID(){
        assertEquals(6, centaur.getID_code());
    }

    @Test
    public void testCost(){
        assertEquals(3, centaur.getCost());
    }

    @Test
    public void testEffect(){

        centaur.effect(game_state);
        game_state.getGT().getIslands().get(game_state.getGT().getMother_nature_pos()).calculate_influence(1, game_state.getGT().getBoards());

        assertEquals(0, game_state.getGT().getIslands().get(game_state.getGT().getMother_nature_pos()).getInfluence_controller());
    }
}