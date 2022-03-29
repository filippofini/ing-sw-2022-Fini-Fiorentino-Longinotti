package it.polimi.ingsw.model;

public enum Tower_colour {
    GREY(0), BLACK(1), WHITE(2) ;

    private final int tower_translate ;

    Tower_colour(int tower_translate){ this.tower_translate=tower_translate; }
    public int getTower_translate(){ return tower_translate; }

}
