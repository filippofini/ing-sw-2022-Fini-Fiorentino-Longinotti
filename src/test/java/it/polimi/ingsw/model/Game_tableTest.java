package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

//THESE TESTS DON'T WORK WITH INITIALIZATION OF STUDENTS IN ISLANDS AND CLOUDS
class Game_tableTest {

    @Test
    public void testCheck_if_playableFalse(){
        Game_table game_table = new Game_table(2,new Turn(), new Player[]{new Player("ff", 0, Tower_colour.GREY, 1), new Player("gg", 1, Tower_colour.BLACK,2)});
        Assistance_card[] discard_deck= game_table.getDiscard_deck();
        discard_deck[1] = Assistance_card.CAT;

        assertFalse(game_table.check_if_playable(Assistance_card.CAT));
    }

    @Test
    public void testCheck_if_playableTrue(){
        Game_table game_table = new Game_table(2,new Turn(), new Player[]{new Player("ff", 0, Tower_colour.GREY, 1), new Player("gg", 1, Tower_colour.BLACK,2)});

        assertTrue(game_table.check_if_playable(Assistance_card.CAT));
    }

    @Test
    public void testSet_mother_nature_start1(){
        Game_table game_table = new Game_table(2,new Turn(), new Player[]{new Player("ff", 0, Tower_colour.GREY, 1), new Player("gg", 1, Tower_colour.BLACK,2)});
        LinkedList<Island> islands = game_table.getIslands();

        assertTrue(islands.get(game_table.getMother_nature_pos()).isMother_nature());

    }
    @Test
    public void testSet_mother_nature_start2(){
        Game_table game_table = new Game_table(2,new Turn(), new Player[]{new Player("ff", 0, Tower_colour.GREY, 1), new Player("gg", 1, Tower_colour.BLACK,2)});
        LinkedList<Island> islands = game_table.getIslands();

        assertFalse(islands.get(game_table.getMother_nature_pos()-1).isMother_nature());

    }

    @Test
    public void testMove_mother_nature(){
        Game_table game_table = new Game_table(2,new Turn(), new Player[]{new Player("ff", 0, Tower_colour.GREY, 1), new Player("gg", 1, Tower_colour.BLACK,2)});
        int pos = game_table.getMother_nature_pos();
        int moves = 58;
        game_table.move_mother_nature(moves);
        int final_pos = pos+moves;
        if(pos+moves>=game_table.getIsland_counter()){
            moves = pos + moves;
            while (moves > game_table.getIsland_counter()) {
                moves = moves - game_table.getIsland_counter();
            }
            final_pos = moves;
        }

        assertEquals(final_pos, game_table.getMother_nature_pos());
    }
}