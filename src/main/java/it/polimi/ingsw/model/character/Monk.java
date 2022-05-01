package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;

/**
 * Monk character card
 */
public class Monk extends Character_card {
    private final int ID_code=2;
    private int cost=1;
    private int uses=0;

    public void monk_effect(){}

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
