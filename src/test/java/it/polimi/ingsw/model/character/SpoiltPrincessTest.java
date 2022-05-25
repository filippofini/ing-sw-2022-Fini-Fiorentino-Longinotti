package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.GameState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the class {@link SpoiltPrincess}.
 */
class SpoiltPrincessTest {
    SpoiltPrincess spoilt_princess = new SpoiltPrincess(new int[]{0,0,1,0,3});
    GameState game_state = new GameState(2, new String[]{"FF", "HH"},new int[]{1,2},false,1);

    @Test
    public void testSetUses1(){
        spoilt_princess.setUses();

        assertEquals(1, spoilt_princess.getUses());
    }
    @Test
    public void testSetUses2(){
        spoilt_princess.setUses();
        spoilt_princess.setUses();

        assertNotEquals(1, spoilt_princess.getUses());
    }

    @Test
    public void testID(){
        assertEquals(11,spoilt_princess.getID_code());
    }

    @Test
    public void testCost(){
        assertEquals(2, spoilt_princess.getCost());
    }

    @Test
    public void testEffect(){
        int sum=0;
        spoilt_princess.setChosen_student(2);
        spoilt_princess.setCurrent_player(1);
        spoilt_princess.effect(game_state);
        int[] arr_stud = spoilt_princess.getStudents();

        for (int i = 0; i < 5; i++) {
            sum = sum+arr_stud[i];
        }

        assertEquals(4, sum);
    }
}