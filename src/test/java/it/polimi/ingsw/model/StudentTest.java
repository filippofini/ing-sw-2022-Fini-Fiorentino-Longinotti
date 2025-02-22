package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void testDefaultValues() {
        Student student = new Student(DiskColor.YELLOW);
        assertEquals(0, student.getColor(), "Color translation should be 0 for YELLOW");
        assertFalse(student.isChosen(), "Student should not be chosen by default");
        assertSame(DiskColor.YELLOW, student.getEnumColour(), "Enum color should match YELLOW");
    }

    @Test
    void testChosenMethod() {
        Student student = new Student(DiskColor.RED);
        assertFalse(student.isChosen(), "Initially isChosen should be false");
        student.chosen();
        assertTrue(student.isChosen(), "Student should be set to chosen");
    }

    @Test
    void testAnotherColor() {
        Student student = new Student(DiskColor.GREEN);
        assertEquals(4, student.getColor(), "Color translation should be 4 for GREEN");
        assertSame(DiskColor.GREEN, student.getEnumColour(), "Enum color should match GREEN");
    }
}