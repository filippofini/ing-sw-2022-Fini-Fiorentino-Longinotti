package it.polimi.ingsw.model;

public class Student {
    //when extracted from the bag the position of the array in the bag becomes the colour of the student
    private Disk_colour colour;
    public Student(Disk_colour colour){
        this.colour =colour;
    }
    public int getColour(){
        return colour.getTranslateColour();
    }
}
