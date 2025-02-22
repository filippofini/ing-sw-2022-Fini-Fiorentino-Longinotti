package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * This class represent the students.
 * Each student has a colour. See more at: {@link DiskColor}.
 */
public class Student implements Serializable {
    //When extracted from the bag the position of the array in the bag becomes the colour of the student
    private final DiskColor color;
    //To know in entrance if the student is chosen or not
    private boolean isChosen;

    /**
     * Constructor of the class.
     * @param color The colour of the student.
     */
    public Student(DiskColor color){
        this.color = color;
        isChosen = false;
    }

    /**
     * This method returns the colour of the student.
     * @return The int representing the colour of the student. See more at: {@link DiskColor}.
     */
    public int getColor(){
        return color.getTranslateColour();
    }

    /**
     * This method checks if a student is chosen.
     * @return {@code True} if the student is chosen, {@code False} if not.
     */
    public boolean isChosen(){
        return isChosen;
    }

    /**
     * This method sets to {@code True} the parameter chosen.
     */
    public void chosen(){
        isChosen = true;
    }

    /**
     * This method returns the colour of the student.
     * @return The colour of the student.
     */
    public DiskColor getEnumColour(){
        return color;
    }
}
