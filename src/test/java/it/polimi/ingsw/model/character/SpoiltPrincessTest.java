package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TowerColour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpoiltPrincessTest {

    private SpoiltPrincess princess;
    private GameState gameState;
    private final int initialCost = 2;
    private final int[] initialStudents = new int[]{0,0,1,0,3}; // total = 4

    @BeforeEach
    public void setUp(){
        // Clone the initial students array to avoid side effects across tests.
        princess = new SpoiltPrincess(initialStudents.clone());

        List<Player> playersList = new ArrayList<>();
        playersList.add(new Player("ff", 1, TowerColour.GREY, 1));
        playersList.add(new Player("gg", 2, TowerColour.GREY, 2));

        gameState = new GameState(2, new String[]{"FF", "HH"}, 1, playersList);
    }

    @Test
    public void testInitialState() {
        // Check the initial conditions set by the constructor.
        assertEquals(initialCost, princess.getCost(), "Initial cost should be 2");
        assertEquals(0, princess.getUses(), "Initial uses should be 0");
        assertArrayEquals(initialStudents, princess.getStudents(), "Initial students array mismatch");
        assertEquals(11, princess.getIDCode(), "ID code must be 11");
        assertEquals("SPOILT PRINCESS", princess.getName(), "Name mismatch");
    }

    @Test
    public void testSetUsesAndCostIncrement() {
        // Each usage should increase uses and cost.
        princess.setUses();
        assertEquals(1, princess.getUses(), "Uses should be incremented to 1");
        assertEquals(initialCost + 1, princess.getCost(), "Cost should increase by 1 after one use");

        princess.setUses();
        assertEquals(2, princess.getUses(), "Uses should now be 2");
        assertEquals(initialCost + 2, princess.getCost(), "Cost should increase by 2 after two uses");
    }

    @Test
    public void testEffectUpdatesStudentsAndUses() {
        // Prepare the card by choosing a student index and setting the current player.
        int chosenStudent = 2;
        princess.setChosenStudent(chosenStudent);
        princess.setCurrentPlayer(1);

        // Calculate the sum of students before effect.
        int sumBefore = 0;
        for (int student : princess.getStudents()){
            sumBefore += student;
        }

        // Apply the effect.
        princess.effect(gameState);

        // Calculate the sum of students after effect.
        int sumAfter = 0;
        for (int student : princess.getStudents()){
            sumAfter += student;
        }

        // The total number of students on the card should remain the same.
        assertEquals(sumBefore, sumAfter, "Total students on the card should remain unchanged after effect");

        // Check that the usage count and cost have been updated.
        assertEquals(1, princess.getUses(), "Usage count should increment after effect");
        assertEquals(initialCost + 1, princess.getCost(), "Cost should increase by 1 after effect");
    }
}