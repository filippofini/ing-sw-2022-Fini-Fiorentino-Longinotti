package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;
import it.polimi.ingsw.model.Game_State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//100% COVERAGE

/**
 * This class tests the class {@link it.polimi.ingsw.model.character.Herbs_grandma}.
 */
class Herbs_grandmaTest {
    Herbs_grandma grandma = new Herbs_grandma();
    Game_State game_state = new Game_State(2, new String[]{"FF", "HH"},new int[]{1,2},false);

    @Test
    public void testSetUses1(){
        grandma.setUses();

        assertEquals(1, grandma.getUses());
    }
    @Test
    public void testSetUses2(){
        grandma.setUses();
        grandma.setUses();

        assertNotEquals(1, grandma.getUses());
    }

    @Test
    public void testID(){
        assertEquals(5, grandma.getID_code());
    }

    @Test
    public void testCost(){
        assertEquals(1, grandma.getCost());
    }

    @Test
    public void testEffect1(){
        grandma.setIndex_to(2);
        grandma.effect(game_state);

        assertEquals(true, game_state.getGT().getIslands().get(1).isProhibition_card());
    }

    @Test
    public void testEffect2(){
        grandma.setIndex_to(4);
        grandma.effect(game_state);
        grandma.setProhibition_cards();

        assertNotEquals(true, game_state.getGT().getIslands().get(1).isProhibition_card());
    }
}