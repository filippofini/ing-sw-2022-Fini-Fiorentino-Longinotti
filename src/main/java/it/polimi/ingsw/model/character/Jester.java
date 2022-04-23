package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;

/**
 * Jester character card
 */
public class Jester extends Character_card {
    private int cost=1;

    public void jester_effect(){}

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
