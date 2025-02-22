package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IslandTest {
    private Board[] boards4;
    private Board[] boards3;

    @BeforeEach
    void setup() {
        boards4 = new Board[4];
        boards3 = new Board[3];
        for (int i = 0; i < boards4.length; i++) {
            boards4[i] = new Board(4, TowerColour.BLACK);
        }
        for (int i = 0; i < boards3.length; i++) {
            boards3[i] = new Board(3, TowerColour.BLACK);
        }
    }

    @Test
    void testIslandID() {
        Island island = new Island(boards4, 5, TowerColour.STARTER, new String[] {"P0","P1","P2","P3"});
        assertEquals(5, island.getIsland_ID(), "Island ID should match the constructor input");
    }

    @Test
    void testBasicInfluenceNoChange() {
        Island island = new Island(boards4, 1, TowerColour.STARTER, new String[] {"P0","P1","P2","P3"});
        boolean[] professors = {true, false, false, false, false};
        boards4[0].setArrProfessors(professors);

        int[] arrStudents = {3, 0, 0, 0, 0};
        island.setArrStudents(arrStudents);
        island.setPlayerController(0);
        island.setInfluenceController(3);
        island.setTower(1);

        // Same controller tries to calculate influence
        assertTrue(island.calculateInfluence(0, boards4), "Controller should remain the same");
        assertEquals(0, island.getPlayerController(), "PlayerController should not change");
    }

    @Test
    void testInfluenceChange4Players() {
        Island island = new Island(boards4, 2, TowerColour.STARTER, new String[] {"P0","P1","P2","P3"});
        boolean[] arrProfNew = {true, true, false, false, false};
        boards4[1].setArrProfessors(arrProfNew);

        int[] arrStudents = {2, 3, 0, 0, 1};
        island.setArrStudents(arrStudents);
        island.setPlayerController(0);
        island.setInfluenceController(4);
        island.setTower(1);

        // Player 1 attempts to get control
        assertFalse(island.calculateInfluence(1, boards4), "Control should change to player 1");
        assertEquals(1, island.getPlayerController(), "PlayerController should now be 1");
    }

    @Test
    void testProhibitionCardEffect() {
        Island island = new Island(boards4, 3, TowerColour.STARTER, new String[] {"P0","P1","P2","P3"});
        island.setProhibitionCard(true);
        int oldController = island.getPlayerController();
        // Influence must not change because of prohibition
        assertTrue(island.calculateInfluence(2, boards4), "No control change allowed when prohibition is active");
        assertEquals(oldController, island.getPlayerController(), "Controller should remain unchanged");
    }

    @Test
    void testColorProhibition() {
        Island island = new Island(boards4, 4, TowerColour.STARTER, new String[] {"P0","P1","P2","P3"});
        island.setProhibitionColour(true);
        island.setPlayerController(-1);
        boolean[] arrProf = {true, false, true, false, false};
        boards4[0].setArrProfessors(arrProf);

        int[] arrStudents = {2, 1, 2, 1, 0};
        island.setArrStudents(arrStudents);
        island.setInfluenceController(0);

        island.setTower(0);
        // Influence ignoring the prohibited color index (default 0)
        assertFalse(island.calculateInfluence(0, boards4), "Control should move to player 0 despite skipping the color");
        assertEquals(0, island.getPlayerController(), "Player 0 should now be the controller");
    }

    @Test
    void testExtraInfluence() {
        Island island = new Island(boards3, 1, TowerColour.STARTER, new String[] {"P0","P1","P2","P3"});
        boolean[] prof = {true, false, true, false, false};
        boards3[1].setArrProfessors(prof);

        island.setArrStudents(new int[] {0, 0, 2, 0, 0});
        island.setPlayerController(-1);
        island.setInfluenceController(0);
        island.setExtraInfluence(2);  // Extra boost

        assertFalse(island.calculateInfluence(1, boards3), "Control should move to player 1 with extra influence");
        assertEquals(1, island.getPlayerController(), "Player 1 should control the island now");
        assertTrue(island.calculateInfluence(1, boards3), "Controller should remain the same on re-check");
    }

    @Test
    void testAddStudentsAndMotherNature() {
        Island island = new Island(boards4, 2, TowerColour.STARTER, new String[] {"P0","P1","P2","P3"});
        Student student = new Student(DiskColor.YELLOW);
        island.addStudents(student);
        assertEquals(1, island.getArrStudents()[0], "One yellow student should be on the island");

        island.setMotherNature(true);
        assertTrue(island.isMotherNatureHere(), "Mother nature was set to true");
    }

    @Test
    void testSetAndGetControllerName() {
        Island island = new Island(boards4, 6, TowerColour.STARTER, new String[] {"P0","P1","P2","P3"});
        island.setPlayerController(2);
        assertEquals("P2", island.getControllerName(), "Controller name should be P2");
    }

    @Test
    void testIncrementStudents() {
        Island island = new Island(boards4, 7, TowerColour.STARTER, new String[] {"P0","P1","P2","P3"});
        island.incrementPos(2);
        assertEquals(1, island.getArrStudents()[2], "One student should be added to position 2");
    }
}