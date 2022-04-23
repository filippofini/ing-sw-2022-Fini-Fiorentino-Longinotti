package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


class Game_tableTest {
    //Game with two players
    Game_table game_table = new Game_table(2, new Turn());
    //Game with two players
    //Game_table game_table = new Game_table(3, new Turn());


    @Test
    public void testCheck_if_playableFalse() {
        Assistance_card[] discard_deck = game_table.getDiscard_deck();
        discard_deck[1] = Assistance_card.CAT;

        assertFalse(game_table.check_if_playable(Assistance_card.CAT));
    }

    @Test
    public void testCheck_if_playableTrue() {

        assertTrue(game_table.check_if_playable(Assistance_card.CAT));
    }

    @Test
    public void testSet_mother_nature_start1() {
        LinkedList<Island> islands = game_table.getIslands();

        assertTrue(islands.get(game_table.getMother_nature_pos()).isMother_nature());

    }

    @Test
    public void testSet_mother_nature_start2() {
        LinkedList<Island> islands = game_table.getIslands();
        int get = game_table.getMother_nature_pos() - 1;
        if (get + 1 == 0) {
            get = get + 2;
        }
        assertFalse(islands.get(get).isMother_nature());

    }

    @Test
    public void testMove_mother_nature() {
        int pos = game_table.getMother_nature_pos();
        Random random = new Random();
        int moves = random.nextInt(50);
        game_table.move_mother_nature(moves);
        int final_pos = pos + moves;
        if (final_pos >= game_table.getIsland_counter()) {
            final_pos = final_pos % game_table.getIsland_counter();
        }

        assertEquals(final_pos, game_table.getMother_nature_pos());
    }

    @Test
    public void testMerge1() {
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(1).setTower(2);
        islands.get(8).setTower(1);
        islands.get(2).setTower(1);
        islands.get(3).setTower(1);

        game_table.merge(2);
        assertEquals(11, islands.size());
    }

    @Test
    public void testMerge2() {
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(2).setTower(1);
        islands.get(3).setTower(1);
        islands.get(1).setTower(1);

        game_table.merge(2);
        assertEquals(10, islands.size());
    }

    @Test
    public void testMerge3() {
        //Merge to first of the list
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(0).setTower(1);
        islands.get(1).setTower(1);
        islands.get(11).setTower(1);

        game_table.merge(0);
        assertEquals(10, islands.size());
    }

    @Test
    public void testMerge4() {
        //Merge to last of the list
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(11).setTower(1);
        islands.get(0).setTower(1);
        islands.get(10).setTower(1);

        game_table.merge(11);
        assertEquals(10, islands.size());
    }

    @Test
    public void testMergeDouble(){
        //Merge twice
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(11).setTower(1);
        islands.get(0).setTower(1);
        islands.get(10).setTower(1);
        game_table.merge(11);

        islands.get(7).setTower(3);
        islands.get(8).setTower(3);
        game_table.merge(7);

        assertEquals(9, islands.size());
    }

    @Test
    public void testMergeTriple(){
        //Merge three times
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(11).setTower(1);
        islands.get(0).setTower(1);
        islands.get(10).setTower(1);
        game_table.merge(11);

        islands.get(7).setTower(3);
        islands.get(8).setTower(3);
        game_table.merge(7);

        islands.get(2).setTower(2);
        islands.get(1).setTower(2);
        islands.get(3).setTower(2);
        game_table.merge(2);

        assertEquals(7, islands.size());
    }

    @Test
    public void testNoMerge(){
        //No merge
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(3).setTower(2);
        islands.get(4).setTower(1);
        game_table.merge(3);

        assertEquals(12, islands.size());
    }

    @Test
    public void testDraw_char1(){
      Character_card[] drawn = game_table.getArr_character();

      assertNotEquals(null, drawn[0]);
    }

    @Test
    public void testDraw_char2(){
        Character_card[] drawn = game_table.getArr_character();

        assertNotEquals(null, drawn[1]);
    }

    @Test
    public void testDraw_char3(){
        Character_card[] drawn = game_table.getArr_character();

        assertNotEquals(null, drawn[2]);
    }
}