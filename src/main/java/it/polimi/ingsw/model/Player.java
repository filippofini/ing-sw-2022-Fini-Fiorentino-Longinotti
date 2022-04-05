package it.polimi.ingsw.model;

public class Player {
    private final String name;
    private final int tower_colour;
    private final int wizard;
    private int coin=1;
    private final int player_ID;
    private Assistance_card chosen_card; //chosen card to play during the turn
    private final Deck deck=new Deck();



    public Player(String name, int wizard, Tower_colour tower, int player_ID) {
        this.name = name;
        this.wizard = wizard;
        tower_colour = tower.getTower_translate();
        this.player_ID = player_ID;
    }
    //TODO: CHOSEN CARD AND REMOVAL
    //private void set_chosen_card(Assistance_card chosen){
    //    this.chosen_card = chosen.getChosen_card();
    //    deck.remove_used_card(chosen_card);
    //}

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

    public void setChosen_card(Assistance_card chosen_card) {
        this.chosen_card = chosen_card;
    }
}
