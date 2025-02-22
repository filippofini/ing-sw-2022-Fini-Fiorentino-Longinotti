package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TowerColour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class FarmerTest {

    private Farmer farmer;
    private GameState gameState;
    private List<Player> players;

    @BeforeEach
    public void setUp() {
        farmer = new Farmer();
        players = new ArrayList<>();
        players.add(new Player("Player1", 1, TowerColour.GREY, 1));
        players.add(new Player("Player2", 2, TowerColour.GREY, 2));
        gameState = new GameState(2, new String[]{"Player1", "Player2"}, 0, players);
    }

    @Test
    public void testInitialCostAndUses() {
        // Verify that the initial cost and uses are as expected.
        assertEquals(2, farmer.getCost());
        assertEquals(0, farmer.getUses());
    }

    @Test
    public void testSetUsesIncrementsCostAndUses() {
        // Calling setUses should increment 'uses' and increase the cost by 1 each time.
        farmer.setUses();
        assertEquals(1, farmer.getUses());
        assertEquals(3, farmer.getCost());

        farmer.setUses();
        assertEquals(2, farmer.getUses());
        assertEquals(4, farmer.getCost());
    }

    @Test
    public void testFarmerEffectActivatesFarmerState() {
        // Initially, the target board should not have the farmer effect.
        assertFalse(gameState.getGameTable().getBoards()[gameState.getCurrPlayer()].isFarmerState());

        // After invoking effect, the state should be activated.
        farmer.effect(gameState);
        assertTrue(gameState.getGameTable().getBoards()[gameState.getCurrPlayer()].isFarmerState());
    }

    @Test
    public void testCardIdAndName() {
        // Check that the card's ID and name are correctly returned.
        assertEquals(2, farmer.getIDCode());
        assertEquals("FARMER", farmer.getName());
    }
}