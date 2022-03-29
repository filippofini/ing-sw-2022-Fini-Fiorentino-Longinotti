package it.polimi.ingsw.model;

public class Player {
    private final String name;
    private final int tower_colour;
    private final int wizard;
    private int coin=1;
    private final int player_ID;
    private Assistence_card chosen_card; //chosen card to play during the turn



    public Player(String name, int wizard, Tower_colour tower, int player_ID) {
        this.name = name;
        this.wizard = wizard;
        tower_colour = tower.getTower_translate();
        this.player_ID = player_ID;
    }

}
