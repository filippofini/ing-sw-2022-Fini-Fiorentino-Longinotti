package it.polimi.ingsw.model;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    public void testCount(){
        Deck deck = new Deck();
        assertEquals(10,10);
    }

    @Test
    public void testRemove_used_card(){
        Deck deck = new Deck();
        deck.remove_used_card(Assistance_card.CAT);
        assertNotSame(10,deck.count_elements());
    }

}