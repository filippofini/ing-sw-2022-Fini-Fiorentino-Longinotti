package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;
import it.polimi.ingsw.model.Game_State;

/**
 * This class represents the knight character card.
 */
public class Knight extends Character_card {
    private final int ID_code=8;
    private int cost=2;
    private int uses=0;
    private boolean isUsed=false;

    /**
     * This method implements the effect of the knight card.
     * When the influence is calculated 2 additional points are counted.
     * @param game_state The game state.
     */
    @Override
    public void effect(Game_State game_state){
        for(int i=0;i< game_state.getGT().getIslands().size();i++){
            game_state.getGT().getIslands().get(i).setExtra_influence(2);
        }
        this.setUses();
        setUsed(true);
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

    public void setUsed(boolean used) {
        isUsed = used;
    }
    public boolean getUsed(){
        return  isUsed;
    }
}
