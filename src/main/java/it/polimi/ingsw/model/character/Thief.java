package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;

/**
 * Thief character card
 */
public class Thief extends Character_card {
    private final int ID_code=12;
    private int cost=3;
    private int uses=0;

    public void thief_effect(){}

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
