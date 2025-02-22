// File: src/test/java/it/polimi/ingsw/model/character/HeraldTest.java
package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TowerColour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class HeraldTest {

    private List<Player> players;
    private GameState gameState;
    private Herald herald;

    @BeforeEach
    public void setUp() {
        players = new ArrayList<>();
        players.add(new Player("ff", 0, TowerColour.GREY, 1));
        players.add(new Player("gg", 1, TowerColour.GREY, 2));
        gameState = new GameState(2, new String[]{"FF", "HH"}, 0, players);
        herald = new Herald();
    }

    @Test
    public void testSetUses() {
        int initialCost = herald.getCost();
        assertEquals(0, herald.getUses(), "Initial uses should be 0");
        herald.setUses();
        assertEquals(1, herald.getUses(), "Uses should be 1 after one call");
        assertEquals(initialCost + 1, herald.getCost(), "Cost should increase by 1 after a use");
    }

    @Test
    public void testMultipleUsesIncreasesCost() {
        int initialCost = herald.getCost();
        herald.setUses();
        herald.setUses();
        assertEquals(2, herald.getUses(), "Uses should be 2 after two calls");
        assertEquals(initialCost + 2, herald.getCost(), "Cost should increase by 2 after two uses");
    }

    @Test
    public void testID() {
        assertEquals(3, herald.getIDCode(), "ID code should be 3");
    }

    @Test
    public void testCost() {
        assertEquals(3, herald.getCost(), "Initial cost should be 3");
    }

    @Test
    public void testEffectCalculatesInfluence() {
        // Choose an island index available in the game table context.
        int islandIndex = 1;
        herald.setIndexTo(islandIndex);
        Island island = gameState.getGameTable().getIslands().get(islandIndex);

        // Ensure the island starts with no controller.
        island.setPlayerController(-1);
        // Set a student on the island at color index 2.
        int[] students = {0, 0, 1, 0, 0};
        island.setArrStudents(students);

        // Grant the current player a professor for color index 2.
        Board[] boards = gameState.getGameTable().getBoards();
        boolean[] arrProfessors = new boolean[5];
        arrProfessors[2] = true;
        boards[gameState.getCurrPlayer()].setArrProfessors(arrProfessors);

        // Trigger the card effect which calls calculateInfluence.
        herald.effect(gameState);

        // Assert that the island control has changed to the current player.
        assertEquals(gameState.getCurrPlayer(), island.getPlayerController(),
                "Control of the island should move to the current player after influence calculation");
        // Confirm that the card's usage count has been incremented.
        assertEquals(1, herald.getUses(), "Uses should increment after effect");
    }
}