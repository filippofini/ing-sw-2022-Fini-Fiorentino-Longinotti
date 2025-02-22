// File: src/test/java/it/polimi/ingsw/model/character/CentaurTest.java
package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TowerColour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CentaurTest {
    private Centaur centaur;
    private GameState gameState;
    private List<Player> players;

    @BeforeEach
    void setUp() {
        // Set up two players for the game state.
        players = new ArrayList<>();
        players.add(new Player("Player1", 1, TowerColour.GREY, 1));
        players.add(new Player("Player2", 2, TowerColour.GREY, 2));

        // Create a game state with the players.
        gameState = new GameState(2, new String[]{"Player1", "Player2"}, 0, players);

        // Create a new Centaur instance.
        centaur = new Centaur();
    }

    @Test
    void testInitialProperties() {
        // Verify initial properties of the Centaur card.
        assertEquals(0, centaur.getUses(), "Initial usage count should be 0.");
        assertEquals(3, centaur.getCost(), "Initial cost should be 3.");
        assertEquals(6, centaur.getIDCode(), "ID code should be 6.");
        assertEquals("CENTAUR", centaur.getName(), "The name of the card should be CENTAUR.");
    }

    @Test
    void testSetUsesIncrementsUsageAndCost() {
        // Call setUses once.
        centaur.setUses();
        assertEquals(1, centaur.getUses(), "Usage count should become 1 after one use.");
        assertEquals(4, centaur.getCost(), "Cost should increase to 4 after one use.");

        // Call setUses a second time.
        centaur.setUses();
        assertEquals(2, centaur.getUses(), "Usage count should become 2 after two uses.");
        assertEquals(5, centaur.getCost(), "Cost should increase to 5 after two uses.");
    }

    @Test
    void testEffectExcludesTowersInInfluenceCalculation() {
        // Call the effect which should modify the island where Mother Nature is located.
        centaur.effect(gameState);
        int motherNatureIndex = gameState.getGameTable().getMotherNaturePos();

        // We assume that each island has a method isIncludeTowers() to check this flag.
        boolean includeTowers = gameState.getGameTable()
                                         .getIslands()
                                         .get(motherNatureIndex)
                                         .isIncludeTowers();

        // The effect of Centaur should disable tower inclusion.
        assertFalse(includeTowers, "The effect should set towers inclusion to false on the selected island.");
    }
}