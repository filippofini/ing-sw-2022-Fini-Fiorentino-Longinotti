package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player {
    private final String name;
    private final int tower_colour;
    private final int wizard;
    private int coin=1;
    private final int player_ID;
    private Assistance_card chosen_card; //chosen card to play during the turn
    private Assistance_card discard_deck;
    private final Deck deck;
    private Game_table game_table;
    private Game_State game_state;//don't know if this is correct



    public Player(String name, int wizard, Tower_colour tower, int player_ID) {
        this.name = name;
        this.wizard = wizard;
        tower_colour = tower.getTower_translate();
        this.player_ID = player_ID;
        deck = new Deck();
    }

    //TODO: check if this is correct
    //this may be wrong
    private void setChosen_card(Assistance_card chosen){
        this.chosen_card = chosen;
        game_table = game_state.getGT(); //this could also be wrong
        boolean check = game_table.check_if_playable(chosen);
        if(check==false) {
            deck.remove_used_card(chosen_card);
            discard_deck = chosen_card;
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

    public Assistance_card getDiscard_deck() {
        return discard_deck;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return tower_colour == player.tower_colour && wizard == player.wizard && coin == player.coin && player_ID == player.player_ID && Objects.equals(name, player.name) && chosen_card == player.chosen_card && discard_deck == player.discard_deck && Objects.equals(deck, player.deck);
    }

}
