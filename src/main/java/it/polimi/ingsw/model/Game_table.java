package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Game_table {
    private int num_players;
    private int player_ID;
    private Board[] boards;
    private Double_linked_list islands;
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
        islands = new Double_linked_list();
        for(int i=0;i<12;i++){
            islands.append(new Island(turn.getCurrent_player(), boards));
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

    public Double_linked_list getIslands() {
        return islands;
    }

    public Double_linked_list merge(){
        //Now the merge gets the first island and the next one
        //TODO: fix the merge giving correct input to the function
        islands.merge_islands(islands.head, islands.head.next);
        return islands;
    }


    public List<Cloud> del_cloud(int cloud_index) {
        clouds.remove(cloud_index);
        return clouds;
    }
    public List<Cloud> getClouds() {
        return clouds;
    }

    ;
}
