package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;
import it.polimi.ingsw.model.Game_State;
import it.polimi.ingsw.model.Game_table;
import it.polimi.ingsw.model.Player;

/**
 * Magic mailman character card
 */
public class Magic_mailman extends Character_card {
    private int cost=1;
    private int uses=0;

    @Override
    public void effect(Game_State game_state){
       game_state.getPlayers()[game_state.getGT().getCurrent_player()].setMoves(game_state.getPlayers()[game_state.getGT().getCurrent_player()].getChosen_card().getMother_nature_movement()+2);
        setUses();
    }

    public void setUses() {
        this.uses++;
        setCost(cost+1);
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}