package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * This class represent the professors.
 * There is one professor for each colour. See more at: {@link DiskColor}.
 */
public class Professor implements Serializable {
    private final DiskColor colorProf;

    /**
     * Constructor of the class.
     * @param colorProf The colour of the professor.
     */
    public Professor(DiskColor colorProf) {
        this.colorProf = colorProf;
    }

    /**
     * This method returns the colour of the professor.
     * @return The int representing the colour of the professor. See more at: {@link DiskColor}.
     */
    public int getColorProf() {
        return colorProf.getTranslateColour();
    }
}
