package it.polimi.ingsw.model;

/**
 * Class containing the towers colours
 */
public enum Tower_colour {
    STARTER(0), GREY(1), BLACK(2), WHITE(3) ;

    private final int tower_translate ;

    /**
     * Constructor of the class
     * @param tower_translate integer that represent the colour
     */
    Tower_colour(int tower_translate){ this.tower_translate=tower_translate; }

    public int getTower_translate(){ return tower_translate; }
}
