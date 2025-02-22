package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player = new Player("ff", 1, TowerColour.GREY, 1);

    @Test
    void testGetPlayerID() {
        assertEquals(1, player.getPlayer_ID(), "Player ID should be 1");
    }

    @Test
    void testGetAndSetCoin() {
        player.setCoin(10);
        assertEquals(10, player.getCoin(), "Coins should be updated to 10");
    }

    @Test
    void testGetTowerColor() {
        assertEquals(1, player.getTowerColor(), "Tower color translation should be 1");
    }

    @Test
    void testGetAndSetChosen() {
        assertFalse(player.isChosen(), "Chosen should be false by default");
        player.setChosen(true);
        assertTrue(player.isChosen(), "Chosen should be true after setting");
    }

    @Test
    void testGetAndSetMoves() {
        player.setMoves(3);
        assertEquals(3, player.getMoves(), "Moves should be updated to 3");
    }

    @Test
    void testGetAndSetChosenCard() {
        AssistanceCard ac = AssistanceCard.CAT;
        player.setChosenCard(ac);
        assertEquals(ac, player.getChosenCard(), "Chosen card should match the one set");
    }

    @Test
    void testDeckInitialization() {
        assertNotNull(player.getDeck(), "Player deck should be initialized");
        assertFalse(player.getDeck().getCards().isEmpty(), "Deck should contain assisting cards");
    }

    @Test
    void testEqualsMethod() {
        Player samePlayer = new Player("ff", 1, TowerColour.GREY, 1);
        assertEquals(player, samePlayer, "Players with same attributes should be equal");
    }
}