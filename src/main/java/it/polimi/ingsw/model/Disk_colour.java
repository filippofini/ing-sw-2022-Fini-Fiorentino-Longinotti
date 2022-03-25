package it.polimi.ingsw.model;

public enum Disk_colour {
    YELLOW(0), RED(1), PINK(2), BLUE(3), GREEN(4);
    private final int translateColour;

    private Disk_colour(int translateColour){
        this.translateColour=translateColour;
    }
    public int getTranslateColour(){
        return translateColour;
    }
}
