package it.polimi.ingsw.model;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    List<Assistance_card> cards1 = new ArrayList<>(Arrays.asList(
            Assistance_card.TORTOISE,
            Assistance_card.ELEPHANT,Assistance_card.CAT));

    List<Assistance_card> cards2 = new ArrayList<>(Arrays.asList(Assistance_card.ELEPHANT,Assistance_card.CAT));
    
    @Test
    public void testCount(){
        Deck deck = new Deck();
        assertEquals(10,10);
    }


    @Test
    void testremove_used_card() {
        cards1.remove(Assistance_card.TORTOISE);
        assertEquals(cards2,cards1);
    }

}