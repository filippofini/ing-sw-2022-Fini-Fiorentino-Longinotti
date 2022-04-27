package it.polimi.ingsw.model;

/**
 * This enumeration defines the color of the towers. 1 is grey, 2 is black, 3 is white.
 * 0 is just used to initialize the starting colour, that means no tower.
 */
public enum Tower_colour {
    STARTER(0), GREY(1), BLACK(2), WHITE(3) ;

    private final int tower_translate ;

    /**
     * Constructor of the class.
     * @param tower_translate The integer that represents the colour of the tower.
     */
    Tower_colour(int tower_translate){ this.tower_translate=tower_translate; }

    /**
     * This method returns the number representing the colour of the tower.
     * @return The number representing the colour of the tower.
     */
    public int getTower_translate(){ return tower_translate; }
}
