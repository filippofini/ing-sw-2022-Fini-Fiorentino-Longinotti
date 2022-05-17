package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Game_State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KnightTest {
    
    Knight knight = new Knight();
    Game_State game_state = new Game_State(2, new String[]{"FF", "HH"},new int[]{1,2},false, 1);

    @Test
    public void testSetUses1(){
        knight.setUses();

        assertEquals(1, knight.getUses());
    }
    @Test
    public void testSetUses2(){
        knight.setUses();
        knight.setUses();

        assertNotEquals(1, knight.getUses());
    }

    @Test
    public void testID(){
        assertEquals(8,knight.getID_code());
    }

    @Test
    public void testCost(){
        assertEquals(2, knight.getCost());
    }

    @Test
    public void testEffect(){
        knight.effect(game_state);
        game_state.getGT().getIslands().get(game_state.getGT().getMother_nature_pos()).calculate_influence(1, game_state.getGT().getBoards());


        assertEquals(2, game_state.getGT().getIslands().get(game_state.getGT().getMother_nature_pos()).getInfluence_controller());
    }

}