// File: src/test/java/it/polimi/ingsw/model/character/MonkTest.java
package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TowerColour;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MonkTest {

    // Creates a list of players prepared for tests
    private List<Player> createPlayers() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player1", 1, TowerColour.GREY, 1));
        players.add(new Player("Player2", 2, TowerColour.GREY, 2));
        return players;
    }

    Monk monk = new Monk(new int[]{1, 0, 0, 2, 1});
    GameState gameState = new GameState(2, new String[]{"Player1", "Player2"}, 1, createPlayers());

    @Test
    @DisplayName("Set Uses: Increments uses and cost on first use")
    public void testSetUsesOnce() {
        // Capture initial cost and uses
        int initialCost = monk.getCost();
        int initialUses = monk.getUses();

        monk.setUses();

        assertEquals(initialUses + 1, monk.getUses(), "Uses should increment by 1.");
        assertEquals(initialCost + 1, monk.getCost(), "Cost should increment by 1.");
    }

    @Test
    @DisplayName("Set Uses: Increments uses and cost correctly after multiple activations")
    public void testSetUsesMultiple() {
        monk.setUses();  // first use
        monk.setUses();  // second use

        assertEquals(2, monk.getUses(), "Uses should be 2 after two activations.");
        // cost should be increased twice from initial cost 1: cost = 3 after two uses
        assertEquals(3, monk.getCost(), "Cost should equal 3 after two activations.");
    }

    @Test
    @DisplayName("ID and Cost: Verify monk card identification and base cost")
    public void testIDAndCost() {
        assertEquals(1, monk.getIDCode(), "Monk ID should be 1.");
        assertEquals(1, new Monk(new int[]{1, 0, 0, 2, 1}).getCost(), "Initial cost for a new monk should be 1.");
    }

    @Test
    @DisplayName("Effect: Moves a student from card to island and adjusts student counts")
    public void testEffect() {
        // Prepare monk for effect: choose island index and student index
        monk.setIndexTo(5);
        monk.setChosenStudent(3);

        // Sum of students before effect; should be 4 initially
        int sumBefore = 0;
        for (int count : monk.getStudents()) {
            sumBefore += count;
        }
        assertEquals(4, sumBefore, "Initial total number of students should be 4.");

        monk.effect(gameState);

        // After effect, one student is moved from the card and another is drawn
        int sumAfter = 0;
        for (int count : monk.getStudents()) {
            sumAfter += count;
        }
        // Total should remain equal to the original count
        assertEquals(4, sumAfter, "Total number of students should remain 4 after effect.");
    }
}