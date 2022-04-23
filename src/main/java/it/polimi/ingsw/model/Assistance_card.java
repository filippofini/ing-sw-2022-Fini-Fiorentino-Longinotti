package it.polimi.ingsw.model;

/**
 * Class containing all the assistance cards
 */
public enum Assistance_card{
    STARTER(0,0), TORTOISE(1,1), ELEPHANT(2,1),
    BULLDOG(3,2),OCTOPUS(4,2),
    LIZARD(5,3), FOX(6,3),
    EAGLE(7,4), CAT(8,4),
    OSTRICH(9,5), LION(10,5);

    private final int value;
    private final int mother_nature_movement;

    /**
     * Constructor of the class
     * @param value value of the card
     * @param mother_nature_movement movement of mother nature
     */
    Assistance_card(int value, int mother_nature_movement) {
        this.value = value;
        this.mother_nature_movement = mother_nature_movement;
    }

    public int getMother_nature_movement() {
        return mother_nature_movement;
    }

    public int getValue() {
        return value;
    }

}
