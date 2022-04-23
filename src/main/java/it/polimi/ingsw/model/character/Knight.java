package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;

/**
 * Knight character card
 */
public class Knight extends Character_card {
    private int cost=2;
    private int uses=0;

    public void knight_effect(){}

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
