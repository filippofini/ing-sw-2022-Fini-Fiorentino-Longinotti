package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;
import it.polimi.ingsw.model.Game_State;

/**
 * Farmer character card
 */
public class Farmer extends Character_card {
    private final int ID_code=2;
    private int cost=2;
    private int uses=0;
//TODO: ask to the other, not convinced so much
    public void effect(Game_State game_state){

        for(int j=0;j<5;j++){
            for(int i=0;i<game_state.getGT().getNum_players();i++){
                if(game_state.getGT().getBoards()[game_state.getCurr_player()].getArrPositionStudents()[j]==game_state.getGT().getBoards()[i].getArrPositionStudents()[j] && game_state.getCurr_player()!=i && game_state.getGT().getBoards()[i].getArrProfessors()[j]){
                    game_state.getGT().getBoards()[game_state.getCurr_player()].setprofessor(j,true);
                }
            }

        }
        setUses();
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