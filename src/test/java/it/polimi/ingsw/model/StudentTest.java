package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the class {@link it.polimi.ingsw.model.Student}.
 */
class StudentTest {

    @Test
    public void testStudentsColor1(){
        Student student = new Student(DiskColor.YELLOW);

        assertEquals(0, student.getColor());
    }

    @Test
    public void testStudentsTrue(){
        Student student = new Student(DiskColor.RED);
        student.chosen();

        assertTrue(student.isChosen());
    }

    @Test
    public void testStudentsColor2(){
        Student student = new Student(DiskColor.GREEN);

        assertSame(DiskColor.GREEN, student.getEnumColour());
    }
}