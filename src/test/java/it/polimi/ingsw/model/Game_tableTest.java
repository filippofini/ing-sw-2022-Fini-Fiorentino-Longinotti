package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.sql.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

//THE TESTS DON'T RUN ALL TOGETHER DUE TO RANDOM
/**
 * This class tests the class {@link it.polimi.ingsw.model.Game_table}.
 */
class Game_tableTest {

    private Board[] board;
    //private <List>Cloud cloud;

    @Test
    public void testCheck_if_playableFalse() {
        //Game with two players
        Game_table game_table = new Game_table(2, new Turn());
        assertTrue(true);
        Assistance_card[] discard_deck = game_table.getDiscard_deck();
        discard_deck[1] = Assistance_card.CAT;

        assertFalse(game_table.check_if_playable(Assistance_card.CAT));
    }

    @Test
    public void testCheck_if_playableTrue() {
        //Game with two players
        Game_table game_table = new Game_table(2, new Turn());
        assertTrue(game_table.check_if_playable(Assistance_card.CAT));
    }

    @Test
    public void testSet_mother_nature_start1() {
        //Game with two players
        Game_table game_table = new Game_table(2, new Turn());
        LinkedList<Island> islands = game_table.getIslands();

        assertTrue(islands.get(game_table.getMother_nature_pos()).isMother_nature());

    }

    @Test
    public void testSet_mother_nature_start2() {
        //Game with two players
        Game_table game_table = new Game_table(2, new Turn());
        LinkedList<Island> islands = game_table.getIslands();
        int get = game_table.getMother_nature_pos() - 1;
        if (get + 1 == 0) {
            get = get + 2;
        }
        assertFalse(islands.get(get).isMother_nature());

    }

    @Test
    public void testMove_mother_nature1() {
        //Game with two players
        Game_table game_table = new Game_table(2, new Turn());
        int pos = game_table.getMother_nature_pos();
        //Random random = new Random();
        int moves = 3;
        game_table.move_mother_nature(moves);
        int final_pos = pos + moves;
        if (final_pos >= game_table.getIsland_counter()) {
            final_pos = final_pos % game_table.getIsland_counter();
        }

        assertEquals(final_pos, game_table.getMother_nature_pos());
    }

    @Test
    public void testMove_mother_nature2() {
        //Game with two players
        Game_table game_table = new Game_table(2, new Turn());
        int pos = game_table.getMother_nature_pos();
        //Random random = new Random();
        int moves = 30;
        game_table.move_mother_nature(moves);
        int final_pos = pos + moves;
        if (final_pos >= game_table.getIsland_counter()) {
            final_pos = final_pos % game_table.getIsland_counter();
        }

        assertEquals(final_pos, game_table.getMother_nature_pos());
    }

    @Test
    public void testMerge1() {
        //Game with three players
        Game_table game_table = new Game_table(3, new Turn());
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(1).setTower(2);
        islands.get(8).setTower(1);
        islands.get(2).setTower(1);
        islands.get(3).setTower(1);

        game_table.merge(2, 1, board);
        assertEquals(11, islands.size());
    }

    @Test
    public void testMerge2() {
        //Game with three players
        Game_table game_table = new Game_table(3, new Turn());
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(2).setTower(1);
        islands.get(3).setTower(1);
        islands.get(1).setTower(1);

        game_table.merge(2, 1, board);
        assertEquals(10, islands.size());
    }

    @Test
    public void testMerge3() {
        //Game with two players
        Game_table game_table = new Game_table(2, new Turn());
        //Merge to first of the list
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(0).setTower(1);
        islands.get(1).setTower(1);
        islands.get(11).setTower(1);

        game_table.merge(0, 1, board);
        assertEquals(10, islands.size());
    }

    @Test
    public void testMerge4() {
        //Game with three players
        Game_table game_table = new Game_table(3, new Turn());
        //Merge to last of the list
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(11).setTower(1);
        islands.get(0).setTower(1);
        islands.get(10).setTower(1);

        game_table.merge(11, 1, board);
        assertEquals(10, islands.size());
    }

    @Test
    public void testMergeDouble() {
        //Game with three players
        Game_table game_table = new Game_table(3, new Turn());
        //Merge twice
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(11).setTower(1);
        islands.get(0).setTower(1);
        islands.get(10).setTower(1);
        game_table.merge(11, 1, board);

        islands.get(7).setTower(3);
        islands.get(8).setTower(3);
        game_table.merge(7, 1, board);

        assertEquals(9, islands.size());
    }

    @Test
    public void testMergeTriple() {
        //Game with three players
        Game_table game_table = new Game_table(3, new Turn());
        //Merge three times
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(11).setTower(1);
        islands.get(0).setTower(1);
        islands.get(10).setTower(1);
        game_table.merge(11, 1, board);

        islands.get(7).setTower(3);
        islands.get(8).setTower(3);
        game_table.merge(7, 1, board);

        islands.get(2).setTower(2);
        islands.get(1).setTower(2);
        islands.get(3).setTower(2);
        game_table.merge(2, 1, board);

        assertEquals(7, islands.size());
    }

    @Test
    public void testNoMerge() {
        //Game with two players
        Game_table game_table = new Game_table(2, new Turn());
        //No merge
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(3).setTower(2);
        islands.get(4).setTower(1);
        game_table.merge(3, 1, board);

        assertEquals(12, islands.size());
    }

    @Test
    public void testDraw_char1() {
        //Game with two players
        Game_table game_table = new Game_table(2, new Turn());
        Character_card[] drawn = game_table.getArr_character();

        assertNotEquals(null, drawn[0]);
    }

    @Test
    public void testDraw_char2() {
        //Game with two players
        Game_table game_table = new Game_table(2, new Turn());
        Character_card[] drawn = game_table.getArr_character();

        assertNotEquals(null, drawn[1]);
    }

    @Test
    public void testDraw_char3() {
        //Game with two players
        Game_table game_table = new Game_table(2, new Turn());
        Character_card[] drawn = game_table.getArr_character();

        assertNotEquals(null, drawn[2]);
    }

    @Test
    public void testGetTurn() {
        Turn turn = new Turn();
        Game_table game_table = new Game_table(2, turn);
        assertEquals(turn, game_table.getTurn());
    }

    @Test
    public void testGetBag() {
        int sum = 0;
        Game_table game_table = new Game_table(3, new Turn());
        int[] arr = game_table.getBag();
        for (int i = 0; i < 5; i++) {
            sum = sum + arr[i];
        }
        assertEquals(108, sum);
    }

    @Test
    public void testGetHow_many_left() {
        Game_table game_table = new Game_table(2, new Turn());
        int cont = game_table.getIsland_counter();
        assertEquals(12 - cont, game_table.getHow_many_left());
    }


    @Test
    void testReplenish_clouds1() {
        Game_table game_table = new Game_table(2,new Turn());
        int[] bag = {3,0,4,10,2};
        game_table.setBag(bag);
        game_table.replenish_clouds();
        assertNotNull(game_table.getClouds());


    }

    @Test
    void testReplenish_cloud2() {

        Game_table game_table = new Game_table(2,new Turn());
        int[] bag = {0,0,0,0,0};
        game_table.setBag(bag);
        game_table.replenish_clouds();
        assertNotNull(game_table.getClouds());

    }

    @Test
    void testReplenish_cloud3() {

        Game_table game_table = new Game_table(3,new Turn());
        int[] bag = {3,0,4,10,2};
        game_table.setBag(bag);
        game_table.replenish_clouds();
        assertNotNull(game_table.getClouds());

    }

    @Test
    void testReplenish_cloud4() {

        Game_table game_table = new Game_table(3,new Turn());
        int[] bag = {0,0,0,0,0};
        game_table.setBag(bag);
        game_table.replenish_clouds();
        assertNotNull(game_table.getClouds());

    }

    }