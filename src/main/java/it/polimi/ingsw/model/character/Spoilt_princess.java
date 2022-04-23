package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;

/**
 * Spoilt princess character card
 */
public class Spoilt_princess extends Character_card {
    private int cost=2;

    public void spoilt_princess_effect(){}

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
