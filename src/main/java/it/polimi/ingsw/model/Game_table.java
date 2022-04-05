package it.polimi.ingsw.model;

import java.util.List;

public class Game_table {
    private int num_players;
    private int player_ID;
    private Board[] boards;
    //private List<Island> islands;
    private List<Cloud> clouds;
    private int mother_nature;
    private int[] bag;
    private Character_card[] arr_character;

    public Game_table(){
        bag = new int[5];
        for(int i=0;i<5;i++) {
            bag[i] = 26;
        }
        //num_players = getNum_players  TODO: implement num_players getter.
        for(int i=0;i<num_players;i++) {
            player_ID = i+1; //maybe using getPlayer_ID???
            boards[i] = new Board(num_players,player_ID);
        }
    }



    /*
        public Island merge_island(Island island_to_merge,Island island_merged){
        TODO: implement merge of islands, implement island initialization
         }
        */

}
