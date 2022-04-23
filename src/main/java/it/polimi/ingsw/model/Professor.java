package it.polimi.ingsw.model;

/**
 * Class professor
 */
public class Professor {
    private final Disk_colour colour_prof;

    /**
     * Constructor of the class
     * @param colour_prof colour of the professor
     */
    public Professor(Disk_colour colour_prof) {
        this.colour_prof = colour_prof;
    }

    public int getColour_prof() {
        return colour_prof.getTranslateColour();
    }
}
