package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    //Game with two players
    Board board = new Board(2, 1, Tower_colour.STARTER);
    //Game with three players
    //Board board = new Board(2,1,Tower_colour.STARTER);

    @Test
    void testAdd_profTrue() {
        boolean[] arrProfessors = board.getArrProfessors();
        Disk_colour color = Disk_colour.RED;
        board.add_prof(color);
        assertTrue(arrProfessors[Disk_colour.RED.getTranslateColour()]);
    }

    @Test
    void testAdd_studentTrue() {
        int[] arrPositionStudents = board.getArrPositionStudents();
        Disk_colour color = Disk_colour.RED;
        arrPositionStudents[Disk_colour.RED.getTranslateColour()] = 2;
        board.add_student(color);
        assertEquals(3, arrPositionStudents[Disk_colour.RED.getTranslateColour()]);
    }

    @Test
    void testMoveEntranceStudents() {
    }

    @Test
    void testCoinsEarned() {
    }
}