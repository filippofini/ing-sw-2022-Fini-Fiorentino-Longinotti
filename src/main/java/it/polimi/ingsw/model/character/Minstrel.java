package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;

/**
 * Minstrel character card
 */
public class Minstrel extends Character_card {
    private int cost=1;

    public void minstrel_effect(){}

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
