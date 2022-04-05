package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game_State {
    private int n_players;
    private List<Player> players;
    private Game_table GT;
    // TODO: turn_Control TC;
    private int[] teams;
    private boolean chat;
    private boolean expert_mode;

    private String[] names;
    private int[] wizard;

    public Game_State(int n_players,String[] names,int wizard[], boolean expert_mode){
        this.n_players = n_players;
        this.names = names;
        this.wizard = wizard;
        this.expert_mode = expert_mode;

        players = new ArrayList<Player>();
        for(int i=0; i<n_players;i++){
            players.add(new Player( names[i], wizard[i], Tower_colour.values()[i], i+1));
        }
        GT = new Game_table(n_players);
        if(n_players == 4){
            teams = new int[4];
            chat = true;
        }

    }

}
