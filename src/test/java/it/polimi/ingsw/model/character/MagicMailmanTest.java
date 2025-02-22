package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.AssistanceCard;
import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TowerColour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MagicMailmanTest {

    private MagicMailman magicMailman;
    private GameState gameState;
    private Player player0;
    private Player player1;

    @BeforeEach
    public void setUp() {
        magicMailman = new MagicMailman();
        player0 = new Player("ff", 1, TowerColour.GREY, 0);
        player1 = new Player("gg", 2, TowerColour.GREY, 1);
        List<Player> players = new ArrayList<>();
        players.add(player0);
        players.add(player1);
        gameState = new GameState(2, new String[]{"FF", "GG"}, 0, players);
    }

    @Test
    public void testInitialCostAndUses() {
        assertEquals(1, magicMailman.getCost(), "Initial cost should be 1");
        assertEquals(0, magicMailman.getUses(), "Initial uses count should be 0");
    }

    @Test
    public void testSetUsesUpdatesUsesAndCostCorrectly() {
        // First use: should increment uses to 1, cost from 1 to 2.
        magicMailman.setUses();
        assertEquals(1, magicMailman.getUses(), "Uses should be 1 after one call");
        assertEquals(2, magicMailman.getCost(), "Cost should be increased to 2 after one use");

        // Second use: uses become 2 and cost from 2 to 3.
        magicMailman.setUses();
        assertEquals(2, magicMailman.getUses(), "Uses should be 2 after two calls");
        assertEquals(3, magicMailman.getCost(), "Cost should be increased to 3 after two uses");
    }

    @Test
    public void testGetIDCodeAndName() {
        assertEquals(4, magicMailman.getIDCode(), "ID code should be 4");
        assertEquals("MAGIC MAILMAN", magicMailman.getName(), "Name should be MAGIC MAILMAN");
    }

    @Test
    public void testEffectIncreasesMotherNatureMovementByTwo() {
        // Ensure a chosen card is set. The AssistanceCard.CAT is assumed
        // to return a fixed mother nature movement value.
        player0.setChosenCard(AssistanceCard.CAT);
        int baseMovement = player0.getChosenCard().getMotherNatureMovement();
        // Call effect to increase moves by 2.
        magicMailman.effect(gameState);
        assertEquals(baseMovement + 2, player0.getMoves(), "Moves should be increased by 2 after effect");
    }
}