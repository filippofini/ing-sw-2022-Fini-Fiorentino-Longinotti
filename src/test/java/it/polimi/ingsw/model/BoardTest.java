package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the class {@link it.polimi.ingsw.model.Board}.
 */
class BoardTest {
    //Game with two players
    Board board = new Board(2, TowerColour.STARTER);
    //Game with three players
    Board board3 = new Board(3, TowerColour.STARTER);


    private Student[] array = new Student[7];

    @Test
    void testAdd_profTrue() {
        boolean[] arrProfessors = board.getArrProfessors();
        DiskColor color = DiskColor.RED;
        board.addProf(color);
        assertTrue(arrProfessors[DiskColor.RED.getTranslateColour()]);
    }

    @Test
    void testAdd_studentTrue() {
        int[] arrPositionStudents = board.getArrPositionStudents();
        DiskColor color = DiskColor.RED;
        arrPositionStudents[DiskColor.RED.getTranslateColour()] = 2;
        board.addStudent(color);
        assertEquals(3, arrPositionStudents[DiskColor.RED.getTranslateColour()]);
    }

    @Test
    void testCoinsEarned() {
        int[]arrPositionStudents = {3,6,9,3,3};
        boolean[][] track_coins = {{true,false,false},
                {false,false,false},
                {false,false,false},
                {false,false,false},{false,false,false}};


        board.setTrackCoins(track_coins);
        assertArrayEquals(track_coins,board.getTrackCoins());

        board.setArrPositionStudents(arrPositionStudents);

        assertEquals(4,board.coinsEarned());

    }

    @Test
    void testN_towers() {
        board.setNumTowers(6);
        assertEquals(6,board.getNumTowers());
    }

    @Test
    void testArrProfessors() {
        boolean[] arr1 = {true, true};
        boolean[] arr2 = {true, true};
        board.setArrProfessors(arr1);
        assertArrayEquals(arr2, board.getArrProfessors());
    }

    @Test
    void testArrEntranceStudents() {
        array[0] = new Student(DiskColor.RED);
        array[1] = new Student(DiskColor.BLUE);
        for (int i = 0; i < 7; i++) {
            array[0] = new Student(DiskColor.RED);
        }
        for (int i = 0; i < 7; i++) {
            board.setArrEntranceStudents(array[i],i);
        }

        assertArrayEquals(array,board.getArrEntranceStudents());
    }

    @Test
    void testSetTower() {
        board.setTower(4);
        assertEquals(4,board.getTower());
    }

    private GameState gameState;


    @BeforeEach
    void setUp() throws Exception {
        // Create a board. We pick 2 players so normally numPlayers=2 but we override it.
        board = new Board(2, TowerColour.STARTER);
        // Override the board.numPlayers to 0 so that (numPlayers + 1) equals 1
        Field numPlayersField = Board.class.getDeclaredField("numPlayers");
        numPlayersField.setAccessible(true);
        numPlayersField.set(board, 0);

        // Set a fresh entrance: ensure every seat is non-null and not chosen.
        Student[] entrance = new Student[board.getMaxEntranceStudents()];
        for (int i = 0; i < entrance.length; i++) {
            entrance[i] = new Student(DiskColor.YELLOW);
        }
        // Replace the board entrance array via reflection.
        Field entranceField = Board.class.getDeclaredField("arrEntranceStudents");
        entranceField.setAccessible(true);
        entranceField.set(board, entrance);

        // Create a minimal GameState with two dummy players.
        List<Player> dummyPlayers = new ArrayList<>();
        dummyPlayers.add(new Player("A", 1, TowerColour.GREY, 0));
        dummyPlayers.add(new Player("B", 2, TowerColour.BLACK, 1));
        String[] names = {"A", "B"};
        gameState = new GameState(2, names, 0, dummyPlayers);
    }

    @Test
    void testMoveEntranceStudentsDining() {
        // Set farmerState to true to check it is reset later
        board.setFarmerState(true);
        // Call the method with placementChoice 0 (place student in dining)
        // Use chosenIndex 0.
        List<Student> islands = board.moveEntranceStudents(gameState, 0, 0);

        // Since placement choice is 0 the student should be placed in dining.
        // The list returned should be empty.
        assertTrue(islands.isEmpty(), "Expected no students sent to islands for dining placement.");

        // Check that the entrance student at the chosen index is now marked as chosen.
        Student chosen = board.getArrEntranceStudents()[0];
        assertTrue(chosen.isChosen(), "Entrance student should be marked as chosen after dining placement.");

        // Check that the dining counter for the student color is incremented.
        int diningCount = board.getArrPositionStudents()[chosen.getColor()];
        assertEquals(1, diningCount, "Dining count should be incremented by one.");

        // Check that farmer effect has been reset.
        assertFalse(board.isFarmerState(), "Farmer effect should be disabled after placements.");
    }

    @Test
    void testMoveEntranceStudentsIsland() {
        // Set farmerState to true to check it is reset later.
        board.setFarmerState(true);
        // Record the original color.
        DiskColor origColor = board.getArrEntranceStudents()[0].getEnumColour();
        // Call the method with placementChoice 1 (send student to island)
        List<Student> islands = board.moveEntranceStudents(gameState, 0, 1);

        // For island placement the list should contain one student.
        assertEquals(1, islands.size(), "Expected one student sent to islands for island placement.");

        // Verify that the student returned is a copy with the same color.
        Student islandStudent = islands.get(0);
        assertEquals(origColor, islandStudent.getEnumColour(), "Island student should have the same color as entrance student.");

        // Check that the entrance student at the chosen index is now marked as chosen.
        Student chosen = board.getArrEntranceStudents()[0];
        assertTrue(chosen.isChosen(), "Entrance student should be marked as chosen after island placement.");

        // Check that farmer effect has been reset.
        assertFalse(board.isFarmerState(), "Farmer effect should be disabled after placements.");
    }
}


