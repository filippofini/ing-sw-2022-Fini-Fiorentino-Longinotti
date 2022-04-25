package it.polimi.ingsw.model;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {


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

}