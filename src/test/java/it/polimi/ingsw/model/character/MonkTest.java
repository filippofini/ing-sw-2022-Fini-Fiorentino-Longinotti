package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Game_State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the class {@link it.polimi.ingsw.model.character.Monk}.
 */
class MonkTest {
    Monk monk = new Monk(new int[]{1,0,0,2,1});
    Game_State game_state = new Game_State(2, new String[]{"FF", "HH"},new int[]{1,2},false);

    @Test
    public void testSetUses1(){
        monk.setUses();

        assertEquals(1, monk.getUses());
    }
    @Test
    public void testSetUses2(){
        monk.setUses();
        monk.setUses();

        assertNotEquals(1, monk.getUses());
    }

    @Test
    public void testID(){
        assertEquals(1,monk.getID_code());
    }

    @Test
    public void testCost(){
        assertEquals(1, monk.getCost());
    }

    @Test
    public void testEffect(){
        int sum=0;
        monk.setIndex_to(5);
        monk.setChosen_student(3);
        monk.effect(game_state);
        int[] arr_stud = monk.getStudents();

        for (int i = 0; i < 5; i++) {
            sum = sum+arr_stud[i];
        }

        assertEquals(4, sum);
    }
}