package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class Game_tableTest {
    //Game with two players
    Game_table game_table = new Game_table(2,new Turn(), new Player[]{new Player("ff", 0, Tower_colour.GREY, 1), new Player("gg", 1, Tower_colour.BLACK,2)});
    //Game with three players
    //Game_table game_table = new Game_table(3,new Turn(), new Player[]{new Player("ff", 0, Tower_colour.GREY, 1), new Player("gg", 1, Tower_colour.BLACK,2), new Player("hh", 2,Tower_colour.WHITE, 3)});


    @Test
    public void testCheck_if_playableFalse(){
        Assistance_card[] discard_deck= game_table.getDiscard_deck();
        discard_deck[1] = Assistance_card.CAT;

        assertFalse(game_table.check_if_playable(Assistance_card.CAT));
    }

    @Test
    public void testCheck_if_playableTrue(){

        assertTrue(game_table.check_if_playable(Assistance_card.CAT));
    }

    @Test
    public void testSet_mother_nature_start1(){
        LinkedList<Island> islands = game_table.getIslands();

        assertTrue(islands.get(game_table.getMother_nature_pos()).isMother_nature());

    }
    @Test
    public void testSet_mother_nature_start2(){
        LinkedList<Island> islands = game_table.getIslands();
        int get = game_table.getMother_nature_pos()-1;
        if (get+1==0) {
            get = get + 2;
        }
        assertFalse(islands.get(get).isMother_nature());

    }

    @Test
    public void testMove_mother_nature(){
        int pos = game_table.getMother_nature_pos();
        Random random = new Random();
        int moves = random.nextInt(50);
        game_table.move_mother_nature(moves);
        int final_pos = pos+moves;
        if(final_pos>=game_table.getIsland_counter()){
            final_pos = final_pos%game_table.getIsland_counter();
        }

        assertEquals(final_pos, game_table.getMother_nature_pos());
    }
}