package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;

public class Monk extends Character_card {
    private int cost=1;

    public void monk_effect(){}

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
