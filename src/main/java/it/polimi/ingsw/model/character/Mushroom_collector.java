package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;

public class Mushroom_collector extends Character_card {
    private int cost=3;

    public void mushroom_collector_effect(){}

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}