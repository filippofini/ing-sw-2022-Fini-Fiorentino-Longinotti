package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.AssistanceCard;
import it.polimi.ingsw.model.GameState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//100% COVERAGE

/**
 * This class tests the class {@link MagicMailman}.
 */
class MagicMailmanTest {
    MagicMailman magic_mailman = new MagicMailman();
    GameState game_state = new GameState(2, new String[]{"FF", "HH"},new int[]{1,2},false,1);

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
        game_state.getPlayers()[0].setChosen_card(AssistanceCard.CAT);
        magic_mailman.effect(game_state);

        assertEquals(6, game_state.getPlayers()[0].getMoves());
    }
}