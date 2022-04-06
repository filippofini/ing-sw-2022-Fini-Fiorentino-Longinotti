package it.polimi.ingsw.model;

public class Student {
    //when extracted from the bag the position of the array in the bag becomes the colour of the student
    private Disk_colour colour;
    //to know in entrace if the student is chosen or not
    private boolean isChosen;
    public Student(Disk_colour colour){
        this.colour =colour;
        isChosen=false;
    }
    public int getColour(){
        return colour.getTranslateColour();
    }

    public boolean getisChosen(){
        return isChosen;
    }
    public void Chosen(){
        isChosen = true;
    }
    public Disk_colour getEnumColour(){
        return colour;
    }
}
