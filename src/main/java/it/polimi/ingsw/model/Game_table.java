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

    public Game_table(int num_players){
        bag = new int[5];
        this.num_players = num_players;
        for(int i=0;i<5;i++) {
            bag[i] = 26;
        }
        for(int i=0;i<num_players;i++) {
            boards[i] = new Board(num_players,i+1);
        }
        islands = new ArrayList<Island>();
        for(int i=0;i<12;i++){
            islands.add(new Island());
        }

        clouds = new ArrayList<Cloud>();
        for(int i=0;i<num_players;i++){
            clouds.add(new Cloud(num_players));
        }
        //TODO: initialization of arr_character

    }



    /*
        public Island merge_island(Island island_to_merge,Island island_merged){
        TODO: implement merge of islands
         }
        */

}
