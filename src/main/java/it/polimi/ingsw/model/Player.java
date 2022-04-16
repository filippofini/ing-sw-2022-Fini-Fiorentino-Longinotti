package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final int tower_colour;
    private final int wizard;
    private int coin=1;
    private final int player_ID;
    private Assistance_card chosen_card; //chosen card to play during the turn
    private List<Assistance_card> discard_deck;
    private final Deck deck;



    public Player(String name, int wizard, Tower_colour tower, int player_ID) {
        this.name = name;
        this.wizard = wizard;
        tower_colour = tower.getTower_translate();
        this.player_ID = player_ID;
        discard_deck = new ArrayList<Assistance_card>();
        deck = new Deck();
    }



    private void setChosen_card(Assistance_card chosen){
        this.chosen_card = chosen;
        boolean check = false; //this is temp, delete later.
        //TODO: finish this check for chosen card
        //boolean check = check_if_playable;  A REFERENCE TO GAME TABLE IS NEEDED
        if(check==false) {
            deck.remove_used_card(chosen_card);
            discard_deck.add(chosen_card);
        }
    }

    public int getPlayer_ID() {
        return player_ID;
    }

    public int getCoin() {
        return coin;
    }

    public int getTower_colour() {
        return tower_colour;
    }

    public String getName() {
        return name;
    }

    public List<Assistance_card> getDiscard_deck() {
        return discard_deck;
    }
}
