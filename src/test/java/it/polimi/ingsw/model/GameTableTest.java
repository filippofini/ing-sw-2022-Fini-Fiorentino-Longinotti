package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.TurnController;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Revised tests for GameTable.
 */
class GameTableTest {

    private final String[] names = {"Alice", "Bob", "Charlie"};

    /**
     * Test that only one island contains Mother Nature at the game start.
     */
    @Test
    public void testMotherNatureInitialPlacement() {
        GameTable gameTable = new GameTable(2, names);
        int motherNatureCount = 0;
        for (Island island : gameTable.getIslands()) {
            if (island.isMotherNatureHere()) {
                motherNatureCount++;
            }
        }
        assertEquals(1, motherNatureCount);
    }

    /**
     * Test moveMotherNature works correctly with wrap around.
     */
    @Test
    public void testMoveMotherNatureBoundary() {
        GameTable gameTable = new GameTable(3, names);
        int initialPos = gameTable.getMotherNaturePos();
        int moves = gameTable.getIslandsCounter() + 3; // ensure wrap-around
        gameTable.moveMotherNature(moves);
        int expected = (initialPos + moves) % gameTable.getIslandsCounter();
        assertEquals(expected, gameTable.getMotherNaturePos());
    }

    /**
     * Test that merge() properly combines adjacent islands when controlled by the same player.
     */
    @Test
    public void testMergeIslands() {
        GameTable gameTable = new GameTable(3, names);
        LinkedList<Island> islands = gameTable.getIslands();
        // Set three consecutive islands to the same controller (simulate influence).
        int mergeIndex = 1;
        islands.get(mergeIndex).setPlayerController(1);
        int leftNeighbor = (mergeIndex - 1 + gameTable.getIslandsCounter()) % gameTable.getIslandsCounter();
        int rightNeighbor = (mergeIndex + 1) % gameTable.getIslandsCounter();
        islands.get(leftNeighbor).setPlayerController(1);
        islands.get(rightNeighbor).setPlayerController(1);

        // Record the sum of towers before merging.
        int totalTowers = islands.get(mergeIndex).getTower() +
                          islands.get(leftNeighbor).getTower() +
                          islands.get(rightNeighbor).getTower();

        // Merge islands.
        Board[] boards = gameTable.getBoards();
        gameTable.merge(mergeIndex, 1, boards);

        // After merge two islands are removed.
        int expectedCount = 12; // initial count for 3 players is 12
        expectedCount = expectedCount - 2;
        assertEquals(expectedCount, gameTable.getIslandsCounter());

        // Check that one of the islands controlled by player 1 now has the merged towers.
        boolean found = false;
        for (Island island : gameTable.getIslands()) {
            if (island.getPlayerController() == 1 && island.getTower() == totalTowers) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    /**
     * Test checkIfPlayable makes the correct decision based on discard deck and a given deck.
     */
    @Test
    public void testAssistanceCardPlayable() {
        GameTable gameTable = new GameTable(2, names);
        Deck deck = new Deck();
        // Initially, discard deck holds the STARTER card.
        // If we ask for a different card (e.g. CAT) it should be playable.
        assertTrue(gameTable.checkIfPlayable(AssistanceCard.CAT, deck));

        // Simulate that player 0 has already played CAT.
        AssistanceCard[] discardDeck = gameTable.getDiscardDeck();
        discardDeck[0] = AssistanceCard.CAT;
        // If the deck in hand contains more than only CAT, it becomes unplayable.
        assertFalse(gameTable.checkIfPlayable(AssistanceCard.CAT, deck));
    }

    /**
     * Test that clouds are replenished with the correct number of students.
     */
    @Test
    public void testReplenishClouds() {
        int numPlayers = 2;
        GameTable gameTable = new GameTable(numPlayers, names);
        // Set the bag to a known state with ample students.
        gameTable.setBag(new int[]{5, 5, 5, 5, 5});
        gameTable.testReplenishClouds();
        // In a 2-player game, each cloud should receive 3 students.
        for (Cloud cloud : gameTable.getClouds()) {
            int cloudStudents = Arrays.stream(cloud.getArrStudents()).sum();
            assertEquals(3, cloudStudents);
        }
    }

    /**
     * Test drawOne reduces bag count by one for the drawn student colour.
     */
    @Test
    public void testDrawOneStudent() {
        GameTable gameTable = new GameTable(2, names);
        int[] bagBefore = Arrays.copyOf(gameTable.getBag(), gameTable.getBag().length);
        int drawnColor = gameTable.drawOne();
        assertTrue(drawnColor >= 0 && drawnColor < 5);
        int[] bagAfter = gameTable.getBag();
        // Check that the count for the drawn colour decreased by one.
        assertEquals(bagBefore[drawnColor] - 1, bagAfter[drawnColor]);
    }
}