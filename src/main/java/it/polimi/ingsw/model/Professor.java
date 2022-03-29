package it.polimi.ingsw.model;

public class Professor {
    private final Disk_colour colour_prof;

    public Professor(Disk_colour colour_prof) {
        this.colour_prof = colour_prof;
    }

    public int getColour_prof() {
        return colour_prof.getTranslateColour();
    }
}
