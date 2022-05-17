package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Assistance_card;
import it.polimi.ingsw.model.Game_State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//100% COVERAGE

/**
 * This class tests the class {@link it.polimi.ingsw.model.character.Magic_mailman}.
 */
class Magic_mailmanTest {
    Magic_mailman magic_mailman = new Magic_mailman();
    Game_State game_state = new Game_State(2, new String[]{"FF", "HH"},new int[]{1,2},false,1);

    @Test
    public void testSetUses1(){
        magic_mailman.setUses();

        assertEquals(1, magic_mailman.getUses());
    }

    @Test
    public void testSetUses2(){
        magic_mailman.setUses();
        magic_mailman.setUses();

        assertNotEquals(1, magic_mailman.getUses());
    }


    @Test
    public void testID(){
        assertEquals(4, magic_mailman.getID_code());
    }

    @Test
    public void testCost(){
        assertEquals(1, magic_mailman.getCost());
    }

    @Test
    public void testEffect(){
        game_state.getPlayers()[0].setChosen_card(Assistance_card.CAT);
        magic_mailman.effect(game_state);

        assertEquals(6, game_state.getPlayers()[0].getMoves());
    }
}