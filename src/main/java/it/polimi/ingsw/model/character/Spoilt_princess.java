package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;
import it.polimi.ingsw.model.Game_State;

/**
 * Spoilt princess character card
 */
public class Spoilt_princess extends Character_card {
    private final int ID_code=11;
    private int cost=2;
    private int uses=0;

    /**
     *
     * @param game_state The game state.
     */
    @Override
    public void effect(Game_State game_state){

    }

    /**
     * This method is used when a card has been used.
     * It increases the uses and the cost by 1.
     */
    public void setUses() {
        this.uses++;
        setCost(cost+1);
    }

    /**
     * This method returns the cost.
     * @return The cost.
     */
    public int getCost() {
        return cost;
    }

    /**
     * This method sets the cost.
     * @param cost The new cost of the card.
     */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * This method returns the uses of the card.
     * @return The uses of the card.
     */
    public int getUses() {
        return uses;
    }

    /**
     * This method return the ID code of the card.
     * @return The ID code of the card.
     */
    public int getID_code() {
        return ID_code;
    }
}
