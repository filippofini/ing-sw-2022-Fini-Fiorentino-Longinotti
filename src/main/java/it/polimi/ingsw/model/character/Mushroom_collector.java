package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;

/**
 * Mushroom collector character card
 */
public class Mushroom_collector extends Character_card {
    private final int ID_code=9;
    private int cost=3;
    private int uses=0;

    public void mushroom_collector_effect(){}

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
