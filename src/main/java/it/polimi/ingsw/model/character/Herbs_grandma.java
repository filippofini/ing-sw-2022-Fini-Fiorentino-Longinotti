package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;
import it.polimi.ingsw.model.Game_State;

/**
 * This class represents the herbs grandma character card.
 * This card starts with 4 prohibition cards that can be put on the island.
 */
public class Herbs_grandma extends Character_card {
    private int cost=1;
    private int uses=0;
    private int prohibition_cards = 4;
    private int index_to;

    /**
     * This method implements the effect of the herbs grandma card.
     * When played, a prohibition card is moved to the selected island.
     * If mother nature moves on that island the influence is not calculated and the towers are not placed.
     * The prohibition card is moved again into this card.
     * @param game_state The game state.
     */
    @Override
    public void effect(Game_State game_state){
        if (prohibition_cards==0) {
            return;
        }
        else {
            prohibition_cards--;
            game_state.getGT().getIslands().get(index_to).setProhibition_card(true);
            this.setUses();
        }
    }

    /**
     * This method sets the index of the island into which the prohibition card will be moved to.
     * @param index_to The index of the island into which the prohibition card will be moved to.
     */
    public void setIndex_to(int index_to) {
        this.index_to = index_to-1;
    }
}