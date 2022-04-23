package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;

/**
 * Centaur character card
 */
public class Centaur extends Character_card {
    private int cost=3;
    private int uses=0;

    public void centaur_effect(){}

    public void setUses() {
        this.uses++;
        setCost(cost+1);
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
