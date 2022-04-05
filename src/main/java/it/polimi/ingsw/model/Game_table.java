package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Game_table {
    private int num_players;
    private int player_ID;
    private Board[] boards;
    private List<Island> islands;
    private List<Cloud> clouds;
    private int mother_nature;
    private int[] bag;
    private Character_card[] arr_character;
    private Turn turn;

    public Game_table(int num_players,Turn turn){
        this.num_players = num_players;
        this.turn = turn;
        bag = new int[5];
        for(int i=0;i<5;i++) {
            bag[i] = 26;
        }
        for(int i=0;i<num_players;i++) {
            boards[i] = new Board(num_players,i+1);
        }
        islands = new ArrayList<Island>();
        for(int i=0;i<12;i++){
            islands.add(new Island(turn.getCurrent_player(), boards));
        }

        clouds = new ArrayList<Cloud>();
        for(int i=0;i<num_players;i++){
            clouds.add(new Cloud(num_players));
        }
        //TODO: initialization of arr_character

    }

    public Board[] getBoards() {
        return boards;
    }



        public List<Island> merge_island(Island island_to_merge, Island island_merged){

            return islands;

         }


}
