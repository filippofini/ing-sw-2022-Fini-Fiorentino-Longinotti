package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;

/**
 * Farmer character card
 */
public class Farmer extends Character_card {
    private int cost=2;

    public void farmer_effect(){}

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}