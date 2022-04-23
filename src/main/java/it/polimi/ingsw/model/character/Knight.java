package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;

/**
 * Knight character card
 */
public class Knight extends Character_card {
    private int cost=2;

    public void knight_effect(){}

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
