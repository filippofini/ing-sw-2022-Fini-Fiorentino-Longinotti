// File: src/test/java/it/polimi/ingsw/model/character/KnightTest.java
package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TowerColour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KnightTest {
    private List<Player> playersList;
    private GameState gameState;
    private Knight knight;

    // Helper method to initialize players list.
    private List<Player> createPlayers() {
        List<Player> list = new ArrayList<>();
        list.add(new Player("Player1", 1, TowerColour.GREY, 1));
        list.add(new Player("Player2", 2, TowerColour.GREY, 2));
        return list;
    }

    @BeforeEach
    public void setUp(){
        playersList = createPlayers();
        gameState = new GameState(2, new String[]{"Player1", "Player2"}, 0, playersList);
        knight = new Knight();
    }

    // Test initial state of Knight.
    @Test
    public void testInitialState(){
        // cost should be 2 and uses should be 0 on initialization.
        assertEquals(2, knight.getCost(), "Initial cost should be 2");
        assertEquals(0, knight.getUses(), "Initial uses should be 0");
    }

    // Test that Knight.effect correctly adds extra influence to all islands and updates usage.
    @Test
    public void testKnightEffectUpdatesIslandsAndUsage(){
        knight.effect(gameState);
        // Verify that knight usage increased and cost increased as well.
        assertEquals(1, knight.getUses(), "Knight uses should increment by 1 after effect");
        assertEquals(3, knight.getCost(), "Knight cost should increase by 1 after effect");

        // Verify that for every island, extraInfluence is set to 2.
        // Assumes each island has a getExtraInfluence() method.
        gameState.getGameTable().getIslands().forEach(island ->
                assertEquals(2, island.getExtraInfluence(), "Each island must have extra influence of 2")
        );
    }

    // Test multiple uses updating cost and usage.
    @Test
    public void testMultipleUsesUpdatesCostAndUsage(){
        knight.setUses();  // First use: cost from 2 to 3, uses become 1.
        knight.setUses();  // Second use: cost from 3 to 4, uses become 2.
        knight.setUses();  // Third use: cost from 4 to 5, uses become 3.

        assertEquals(3, knight.getUses(), "Knight uses should be 3 after three calls");
        assertEquals(5, knight.getCost(), "Knight cost should be 5 after three uses");
    }

    // Test ID code is as expected.
    @Test
    public void testGetIDCode(){
        assertEquals(8, knight.getIDCode(), "Knight ID code must be 8");
    }
}