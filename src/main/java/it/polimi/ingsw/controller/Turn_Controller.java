package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game_State;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class Turn_Controller {
    private int[] player_order;
    private Game_State GS;
    private int n_players;
    List<Player> P_L;

    public Turn_Controller(int n_players, String[] names, int wizard[], boolean expert_mode, List<Player> Player_List){
        this.n_players=n_players;
        this.P_L=Player_List;
        player_order= new int[n_players];
        for(int i=0; i<n_players;i++){
            player_order[i]=i;
        }
        GS= new Game_State(n_players, names, wizard, expert_mode);

    }

    public void planning_phase(){
        GS.getGT().replenish_clouds();
        //TODO:choose card method()
        Calculate_Player_order();
    }

    public void action_phase(){

    }
    public int[] getPlayer_order() {
        return player_order;
    }

    public void Calculate_Player_order() {
        int min=P_L.get(0).getChosen_card().getValue();
        int player=0;
        int count=0;

        for(int i=0;i<n_players;i++){
            for(int j=0;j<n_players-count;j++){
                if(P_L.get(j).getChosen_card().getValue()<min){
                    min=P_L.get(j).getChosen_card().getValue();
                    player=P_L.get(j).getPlayer_ID();
                }
            }
            count++;
            player_order[i]=player;
        }
    }
}
