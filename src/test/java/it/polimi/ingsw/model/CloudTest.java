package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CloudTest {

    @Test
    public void testInitialState() {
        Cloud cloud = new Cloud(10);
        int[] students = cloud.getArrStudents();
        // Verify that all values are 0 initially
        for (int count : students) {
            assertEquals(0, count, "Each student count should be initialized to 0");
        }
    }

    @Test
    public void testSingleIncrement() {
        Cloud cloud = new Cloud(5);
        // Increment a single student in position 2
        cloud.setArrStudents(2);
        int[] students = cloud.getArrStudents();
        // Only position 2 should be incremented once
        for (int i = 0; i < students.length; i++) {
            if (i == 2) {
                assertEquals(1, students[i], "Position 2 should be incremented to 1");
            } else {
                assertEquals(0, students[i], "Other positions should remain 0");
            }
        }
    }

    @Test
    public void testMultipleIncrementsSamePosition() {
        Cloud cloud = new Cloud(3);
        // Increment the same position (position 4) three times
        cloud.setArrStudents(4);
        cloud.setArrStudents(4);
        cloud.setArrStudents(4);
        int[] students = cloud.getArrStudents();
        assertEquals(3, students[4], "Position 4 should be incremented three times");
    }

    @Test
    public void testMultiplePositionsIncrement() {
        Cloud cloud = new Cloud(1);
        // Increment different positions
        cloud.setArrStudents(0);
        cloud.setArrStudents(3);
        cloud.setArrStudents(4);
        cloud.setArrStudents(3); // Increment position 3 second time

        int[] expected = {1, 0, 0, 2, 1};
        assertArrayEquals(expected, cloud.getArrStudents(), "The students array should reflect proper increments");
    }

    @Test
    public void testCloudID() {
        int id = 7;
        Cloud cloud = new Cloud(id);
        assertEquals(id, cloud.getCloudID(), "Cloud ID should be the same as the one passed in the constructor");
    }

    @Test
    public void testSetInvalidPosition() {
        Cloud cloud = new Cloud(1);
        // Expect an ArrayIndexOutOfBoundsException when using an invalid index
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            cloud.setArrStudents(10);
        }, "Should throw exception for invalid index exceeding array bounds");

        // Also test negative index
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            cloud.setArrStudents(-1);
        }, "Should throw exception for negative index");
    }
}