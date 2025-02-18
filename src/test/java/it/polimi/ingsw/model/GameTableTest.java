package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * This class tests the class {@link GameTable}.
 */
class GameTableTest {

    private Board[] board;
    private Deck deck = new Deck();
    private List<Player> player = new ArrayList<Player>();

    private List<Player> addPList(){
        player.add(new Player("ff",1,TowerColour.GREY,1));
        player.add(new Player("gg",2,TowerColour.GREY,2));
        return player;
    }

    //private TurnController turnController = new TurnController(2, new String[]{"ff", "hh"}, new int[]{1,2},false, player));


    @Test
    public void testCheck_if_playableFalse() {
        //Game with two players
        GameTable game_table = new GameTable(2);
        assertTrue(true);
        AssistanceCard[] discard_deck = game_table.getDiscardDeck();
        discard_deck[1] = AssistanceCard.CAT;

        assertFalse(game_table.checkIfPlayable(AssistanceCard.CAT, deck));
    }

    @Test
    public void testCheck_if_playableTrue() {
        //Game with two players
        GameTable game_table = new GameTable(2);
        assertTrue(game_table.checkIfPlayable(AssistanceCard.CAT,deck));
    }

    @Test
    public void testSet_mother_nature_start1() {
        //Game with two players
        GameTable game_table = new GameTable(2);
        LinkedList<Island> islands = game_table.getIslands();

        assertTrue(islands.get(game_table.getMotherNaturePos()).isMotherNatureHere());

    }

    @Test
    public void testSet_mother_nature_start2() {
        //Game with two players
        GameTable game_table = new GameTable(2);
        LinkedList<Island> islands = game_table.getIslands();
        int get = game_table.getMotherNaturePos() - 1;
        if (get + 1 == 0) {
            get = get + 2;
        }
        assertFalse(islands.get(get).isMotherNatureHere());

    }

    @Test
    public void testMove_mother_nature1() {
        //Game with two players
        GameTable game_table = new GameTable(2);
        int pos = game_table.getMotherNaturePos();
        int moves = 3;
        game_table.moveMotherNature(moves);
        int final_pos = pos + moves;
        if (final_pos >= game_table.getIslandsCounter()) {
            final_pos = final_pos % game_table.getIslandsCounter();
        }

        assertEquals(final_pos, game_table.getMotherNaturePos());
    }

    @Test
    public void testMove_mother_nature2() {
        //Game with two players
        GameTable game_table = new GameTable(2);
        int pos = game_table.getMotherNaturePos();
        int moves = 30;
        game_table.moveMotherNature(moves);
        int final_pos = pos + moves;
        if (final_pos >= game_table.getIslandsCounter()) {
            final_pos = final_pos % game_table.getIslandsCounter();
        }

        assertEquals(final_pos, game_table.getMotherNaturePos());
    }

    @Test
    public void testMerge1() {
        //Game with three players
        GameTable game_table = new GameTable(3);
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(1).setPlayerController(2);
        islands.get(8).setPlayerController(1);
        islands.get(2).setPlayerController(1);
        islands.get(3).setPlayerController(1);

        game_table.merge(2, 1, board);
        assertEquals(11, islands.size());
    }

    @Test
    public void testMerge2() {
        //Game with three players
        GameTable game_table = new GameTable(3);
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(2).setPlayerController(1);
        islands.get(3).setPlayerController(1);
        islands.get(1).setPlayerController(1);

        game_table.merge(2, 1, board);
        assertEquals(10, islands.size());
    }

    @Test
    public void testMerge3() {
        //Game with two players
        GameTable game_table = new GameTable(2);
        //Merge to first of the list
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(0).setPlayerController(1);
        islands.get(1).setPlayerController(1);
        islands.get(11).setPlayerController(1);

        game_table.merge(0, 1, board);
        assertEquals(10, islands.size());
    }

    @Test
    public void testMerge4() {
        //Game with three players
        GameTable game_table = new GameTable(3);
        //Merge to last of the list
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(11).setPlayerController(1);
        islands.get(0).setPlayerController(1);
        islands.get(10).setPlayerController(1);

        game_table.merge(11, 1, board);
        assertEquals(10, islands.size());
    }

    @Test
    public void testMergeDouble() {
        //Game with three players
        GameTable game_table = new GameTable(3);
        //Merge twice
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(11).setPlayerController(1);
        islands.get(0).setPlayerController(1);
        islands.get(10).setPlayerController(1);
        game_table.merge(11, 1, board);

        islands.get(7).setPlayerController(3);
        islands.get(8).setPlayerController(3);
        game_table.merge(7, 1, board);

        assertEquals(9, islands.size());
    }

    @Test
    public void testMergeTriple() {
        //Game with three players
        GameTable game_table = new GameTable(3);
        //Merge three times
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(11).setPlayerController(1);
        islands.get(0).setPlayerController(1);
        islands.get(10).setPlayerController(1);
        game_table.merge(11, 1, board);

        islands.get(7).setPlayerController(3);
        islands.get(8).setPlayerController(3);
        game_table.merge(7, 1, board);

        islands.get(2).setPlayerController(2);
        islands.get(1).setPlayerController(2);
        islands.get(3).setPlayerController(2);
        game_table.merge(2, 1, board);

        assertEquals(7, islands.size());
    }

    @Test
    public void testNoMerge() {
        //Game with two players
        GameTable game_table = new GameTable(2);
        //No merge
        LinkedList<Island> islands = game_table.getIslands();
        islands.get(3).setPlayerController(2);
        islands.get(4).setPlayerController(1);
        game_table.merge(3, 1, board);

        assertEquals(12, islands.size());
    }

    @Test
    public void testDraw_char1() {
        //Game with two players
        GameTable game_table = new GameTable(2);
        CharacterCard[] drawn = game_table.getCharacterCards();

        assertNotEquals(null, drawn[0]);
    }

    @Test
    public void testDraw_char2() {
        //Game with two players
        GameTable game_table = new GameTable(2);
        CharacterCard[] drawn = game_table.getCharacterCards();

        assertNotEquals(null, drawn[1]);
    }

    @Test
    public void testDraw_char3() {
        //Game with two players
        GameTable game_table = new GameTable(2);
        CharacterCard[] drawn = game_table.getCharacterCards();

        assertNotEquals(null, drawn[2]);
    }

    @Test
    public void testGetBag() {
        int sum = 0;
        GameTable game_table = new GameTable(3);
        int[] arr = game_table.getBag();
        for (int i = 0; i < 5; i++) {
            sum = sum + arr[i];
        }
        if (sum == 93)
            assertEquals(93, sum);
        else if (sum==89)
            assertEquals(89, sum);
        else
            assertEquals(85, sum);

    }

    @Test
    public void testGetHow_many_left() {
        GameTable game_table = new GameTable(2);
        int cont = game_table.getIslandsCounter();
        assertEquals(cont, game_table.getHowManyLeft());
    }


    @Test
    void testReplenish_clouds1() {
        GameTable game_table = new GameTable(2);
        int[] bag = {3,0,4,10,2};
        game_table.setBag(bag);
        //game_table.replenish_clouds();
        assertNotNull(game_table.getClouds());


    }

    @Test
    void testReplenish_cloud2() {

        GameTable game_table = new GameTable(2);
        int[] bag = {0,0,0,0,0};
        game_table.setBag(bag);
        //game_table.replenish_clouds();
        assertNotNull(game_table.getClouds());

    }

    @Test
    void testReplenish_cloud3() {

        GameTable game_table = new GameTable(3);
        int[] bag = {3,0,4,10,2};
        game_table.setBag(bag);
        //game_table.replenish_clouds();
        assertNotNull(game_table.getClouds());

    }

    @Test
    void testReplenish_cloud4() {

        GameTable game_table = new GameTable(3);
        int[] bag = {0,0,0,0,0};
        game_table.setBag(bag);
        //game_table.replenish_clouds();
        assertNotNull(game_table.getClouds());

    }

    /*
    @Test
    void testChooseCloud(){
        GameTable game_table = new GameTable(3,new Turn());
        Cloud cloud;
        cloud = game_table.choose_cloud();

        assertEquals(1, cloud.getCloud_ID());
    }

     */

}