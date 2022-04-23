package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player = new Player("ff",1, Tower_colour.GREY,1);

    @Test
    public void testGetDeck(){
        Deck deck = new Deck();

        assertEquals(player.getDeck(), deck);
    }

    @Test
    public void testCoin(){
        player.setCoin(20);

        assertEquals(20, player.getCoin());
    }

}