package it.polimi.ingsw.model;

/**
 * This enumeration defines the color of students.
 * 0 is yellow, 1 is red, 2 is pink, 3 is blue, 4 is green.
 */
public enum DiskColour {
    YELLOW(0), RED(1), PINK(2), BLUE(3), GREEN(4);
    private final int translateColour;

    /**
     * Constructor of the class
     * @param translateColour The number representing the colour of the disk.
     */
    private DiskColour(int translateColour){
        this.translateColour=translateColour;
    }

    /**
     * This method returns the number representing the colour of the disk.
     * @return The number representing the colour of the disk.
     */
    public int getTranslateColour(){
        return translateColour;
    }

    public static String printColour(int colour){
        if(colour==0){
            return "YELLOW";
        }
        if(colour==1){
            return "RED";
        }
        if(colour==2){
            return "PINK";
        }
        if(colour==3){
            return "BLUE";
        }
        else{
            return "GREEN";
        }

    }
}
