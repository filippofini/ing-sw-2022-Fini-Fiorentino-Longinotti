package it.polimi.ingsw.model;

/**
 * This class represent the students.
 * Each student has a colour. See more at: {@link it.polimi.ingsw.model.Disk_colour}.
 */
public class Student {
    //When extracted from the bag the position of the array in the bag becomes the colour of the student
    private Disk_colour colour;
    //To know in entrance if the student is chosen or not
    private boolean isChosen;

    /**
     * Constructor of the class.
     * @param colour The colour of the student.
     */
    public Student(Disk_colour colour){
        this.colour =colour;
        isChosen=false;
    }

    /**
     * This method returns the colour of the student.
     * @return The int representing the colour of the student. See more at: {@link it.polimi.ingsw.model.Disk_colour}.
     */
    public int getColour(){
        return colour.getTranslateColour();
    }

    /**
     * This method checks if a student is chosen.
     * @return {@code True} if the student is chosen, {@code False} if not.
     */
    public boolean getIsChosen(){
        return isChosen;
    }

    /**
     * This method sets to {@code True} the parameter chosen.
     */
    public void Chosen(){
        isChosen = true;
    }

    /**
     * This method returns the colour of the student.
     * @return The colour of the student.
     */
    public Disk_colour getEnumColour(){
        return colour;
    }
}
