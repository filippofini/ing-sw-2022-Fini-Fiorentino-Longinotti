package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private Deck deck;
    private List<AssistanceCard> expectedCards;

    @BeforeEach
    void setUp() {
        deck = new Deck();
        expectedCards = new ArrayList<>(Arrays.asList(
                AssistanceCard.TORTOISE,
                AssistanceCard.ELEPHANT,
                AssistanceCard.BULLDOG,
                AssistanceCard.OCTOPUS,
                AssistanceCard.LIZARD,
                AssistanceCard.FOX,
                AssistanceCard.EAGLE,
                AssistanceCard.CAT,
                AssistanceCard.OSTRICH,
                AssistanceCard.LION));
    }

    @Test
    void testInitialDeckCount() {
        assertEquals(10, deck.countElements(), "Deck should initialize with 10 cards");
    }

    @Test
    void testGetCardsContentAndOrder() {
        assertEquals(expectedCards, deck.getCards(), "Deck should contain the expected cards in order");
    }

    @Test
    void testRemoveUsedCard() {
        deck.removeUsedCard(AssistanceCard.CAT);
        expectedCards.remove(AssistanceCard.CAT);
        assertEquals(9, deck.countElements(), "Deck should have 9 cards after removing one");
        assertEquals(expectedCards, deck.getCards(), "Deck cards should match expected list after removal");
    }

    @Test
    void testRemoveNonExistingCard() {
        // Remove the card twice and verify that count decrease only once.
        deck.removeUsedCard(AssistanceCard.FOX);
        int countAfterFirstRemoval = deck.countElements();
        deck.removeUsedCard(AssistanceCard.FOX);
        assertEquals(countAfterFirstRemoval, deck.countElements(), "Removing a non-existing card should not change the count");
    }

    @Test
    void testDeckEquality() {
        Deck deck2 = new Deck();
        // They should be equal initially.
        assertEquals(deck, deck2, "Two new decks should be equal");

        // After removal from one, they should not be equal.
        deck.removeUsedCard(AssistanceCard.OCTOPUS);
        assertNotEquals(deck, deck2, "Decks should not be equal after a removal from one deck");
    }
}