package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;

/**
 * Magic mailman character card
 */
public class Magic_mailman extends Character_card {
    private int cost=1;
    private int uses=0;

    public void magic_mailman_effect(){}

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