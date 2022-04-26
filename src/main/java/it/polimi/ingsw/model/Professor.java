package it.polimi.ingsw.model;

/**
 * This class represent the professors.
 * There is one professor of each colour. See more at: {@link it.polimi.ingsw.model.Disk_colour}.
 */
public class Professor {
    private final Disk_colour colour_prof;

    /**
     * Constructor of the class.
     * @param colour_prof The colour of the professor.
     */
    public Professor(Disk_colour colour_prof) {
        this.colour_prof = colour_prof;
    }

    /**
     * This method returns the colour of the professor.
     * @return An int representing the colour of the professor. See more at: {@link it.polimi.ingsw.model.Disk_colour}.
     */
    public int getColour_prof() {
        return colour_prof.getTranslateColour();
    }
}
