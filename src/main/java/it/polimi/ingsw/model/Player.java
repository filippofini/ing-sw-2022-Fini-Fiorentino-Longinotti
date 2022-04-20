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
    private final Deck deck;



    public Player(String name, int wizard, Tower_colour tower_colour, int player_ID) {
        this.name = name;
        this.wizard = wizard;
        this.tower_colour = tower_colour.getTower_translate();
        this.player_ID = player_ID;
        deck = new Deck();
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

    public int getWizard() {
        return wizard;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public Assistance_card getChosen_card() {
        return chosen_card;
    }

    public void setChosen_card(Assistance_card chosen_card) {
        this.chosen_card = chosen_card;
    }

    public Deck getDeck() {
        return deck;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return tower_colour == player.tower_colour && wizard == player.wizard && coin == player.coin && player_ID == player.player_ID && Objects.equals(name, player.name) && chosen_card == player.chosen_card  && Objects.equals(deck, player.deck);
    }

}
