package it.polimi.ingsw.model;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the class {@link it.polimi.ingsw.model.Deck}.
 */
class DeckTest {

    private List<Assistance_card> cards;

    @Test
    public void testCount(){
        Deck deck = new Deck();
        assertEquals(10,10);
    }


    @Test
    void testRemove_used_card() {
        Deck deck = new Deck();
        deck.remove_used_card(Assistance_card.CAT);
        assertEquals(9,deck.count_elements());
    }

    @Test
    void testGetCards() {
        Deck deck = new Deck();
        cards = new ArrayList<Assistance_card>(Arrays.asList(
                Assistance_card.TORTOISE,
                Assistance_card.ELEPHANT,
                Assistance_card.BULLDOG,
                Assistance_card.OCTOPUS,
                Assistance_card.LIZARD,
                Assistance_card.FOX,
                Assistance_card.EAGLE,
                Assistance_card.CAT,
                Assistance_card.OSTRICH,
                Assistance_card.LION));
     assertEquals(cards,deck.getCards());
    }

}