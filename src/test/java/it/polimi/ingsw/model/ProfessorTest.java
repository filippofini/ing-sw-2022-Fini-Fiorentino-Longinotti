package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProfessorTest {

    @Test
    void colorTranslation() {
        Professor prof = new Professor(DiskColor.RED);
        assertEquals(1, prof.getColorProf());
    }

    @Test
    void colorTranslationForAll() {
        for (DiskColor color : DiskColor.values()) {
            Professor prof = new Professor(color);
            assertEquals(color.getTranslateColour(), prof.getColorProf());
        }
    }
}