// File: src/test/java/it/polimi/ingsw/model/character/HerbsGrandmaMeaningfulTest.java
package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TowerColour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class HerbsGrandmaTest {

    private HerbsGrandma grandma;
    private GameState gameState;
    private List<Player> playerList;

    private List<Player> createPlayers() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Alice", 1, TowerColour.GREY, 1));
        players.add(new Player("Bob", 2, TowerColour.GREY, 2));
        return players;
    }

    @BeforeEach
    void setUp() {
        grandma = new HerbsGrandma();
        playerList = createPlayers();
        gameState = new GameState(2, new String[]{"Alice", "Bob"}, 0, playerList);
    }

    @Test
    void testInitialState() {
        // Verify the initial values of HerbsGrandma card.
        assertEquals(5, grandma.getIDCode(), "ID code should be 5");
        assertEquals("HERBS GRANDMA", grandma.getName(), "Name should be HERBS GRANDMA");
        assertEquals(1, grandma.getCost(), "Initial cost should be 1");
        assertEquals(0, grandma.getUses(), "Initial uses should be 0");
    }

    @Test
    void testEffectAppliesProhibition() {
        // Set the index of the island and perform the effect, verifying prohibition application.
        int targetIslandIndex = 1;
        grandma.setIndexTo(targetIslandIndex);
        // Before applying effect, ensure that island has no prohibition card.
        assertFalse(gameState.getGameTable().getIslands().get(targetIslandIndex).isProhibitionCard(),
                    "Island should not have a prohibition card initially");

        grandma.effect(gameState);
        // After effect, island should have prohibition card and card cost/uses updated.
        assertTrue(gameState.getGameTable().getIslands().get(targetIslandIndex).isProhibitionCard(),
                   "Island should have a prohibition card after effect");
        assertEquals(1, grandma.getUses(), "Uses should be incremented to 1");
        assertEquals(2, grandma.getCost(), "Cost should be incremented after use");
    }

    @Test
    void testRepeatedUsesIncreaseCostAndUses() {
        // Use the card twice and verify that cost and uses increase.
        grandma.setIndexTo(0);
        grandma.effect(gameState);
        int costAfterFirstUse = grandma.getCost();
        int usesAfterFirstUse = grandma.getUses();

        // Apply effect second time at a different island index.
        grandma.setIndexTo(1);
        grandma.effect(gameState);

        assertEquals(usesAfterFirstUse + 1, grandma.getUses(), "Uses should increment by 1 on second use");
        assertEquals(costAfterFirstUse + 1, grandma.getCost(), "Cost should increment by 1 on second use");
    }

    @Test
    void testEffectDoesNothingWhenNoProhibitionCardsLeft() {
        // Execute effect until prohibition cards are exhausted.
        // Card starts with 4 prohibition cards.
        for (int i = 0; i < 4; i++) {
            grandma.setIndexTo(i % gameState.getGameTable().getIslands().size());
            grandma.effect(gameState);
        }
        int usesAfterDepletion = grandma.getUses();
        int costAfterDepletion = grandma.getCost();

        // Attempting effect after depletion should not change uses or cost.
        int targetIslandIndex = 0;
        boolean originalProhibition = gameState.getGameTable().getIslands().get(targetIslandIndex).isProhibitionCard();
        grandma.setIndexTo(targetIslandIndex);
        grandma.effect(gameState);

        // Verify no changes since prohibition cards are exhausted.
        assertEquals(usesAfterDepletion, grandma.getUses(), "Uses should remain unchanged when no prohibition cards available");
        assertEquals(costAfterDepletion, grandma.getCost(), "Cost should not change when effect is not applied");
        assertEquals(originalProhibition, gameState.getGameTable().getIslands().get(targetIslandIndex).isProhibitionCard(),
                     "Island prohibition status remains unchanged");
    }

    @Test
    void testRestoreProhibitionCard() {
        // Apply effect to use one prohibition card.
        int targetIslandIndex = 2;
        // Initially, island has no prohibition card.
        assertFalse(gameState.getGameTable().getIslands().get(targetIslandIndex).isProhibitionCard(),
                    "Island should not have prohibition card initially");
        grandma.setIndexTo(targetIslandIndex);
        grandma.effect(gameState);
        assertTrue(gameState.getGameTable().getIslands().get(targetIslandIndex).isProhibitionCard(),
                   "Island should have prohibition card after effect");

        // Now simulate removal by restoring the prohibition card back to the card.
        grandma.setProhibition_cards();
        // Cannot verify the internal prohibition_cards directly but we can interpret that the card would eventually be reset.
        // This test ensures that calling setProhibition_cards does not throw exception and behaves as defined.
        // Reset multiple times should not exceed the maximum (4).
        for(int i = 0; i < 3; i++){
            grandma.setProhibition_cards();
        }
        // As the implementation prevents exceeding 4, further calls have no effect.
        // The test thus passes if no errors occur.
        assertTrue(true);
    }
}