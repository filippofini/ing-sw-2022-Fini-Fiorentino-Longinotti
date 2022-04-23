package it.polimi.ingsw.model;

/**
 * Class student
 */
public class Student {
    //when extracted from the bag the position of the array in the bag becomes the colour of the student
    private Disk_colour colour;
    //to know in entrance if the student is chosen or not
    private boolean isChosen;

    /**
     * Constructor of the class
     * @param colour colour of the student
     */
    public Student(Disk_colour colour){
        this.colour =colour;
        isChosen=false;
    }


    public int getColour(){
        return colour.getTranslateColour();
    }

    public boolean getIsChosen(){
        return isChosen;
    }
    public void Chosen(){
        isChosen = true;
    }
    public Disk_colour getEnumColour(){
        return colour;
    }
}
