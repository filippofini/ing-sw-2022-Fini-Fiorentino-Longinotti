package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    public void testStudentsColor1(){
        Student student = new Student(Disk_colour.YELLOW);

        assertEquals(0, student.getColour());
    }

    @Test
    public void testStudentsTrue(){
        Student student = new Student(Disk_colour.RED);
        student.Chosen();

        assertTrue(student.getIsChosen());
    }

    @Test
    public void testStudentsColor2(){
        Student student = new Student(Disk_colour.GREEN);

        assertSame(Disk_colour.GREEN, student.getEnumColour());
    }
}