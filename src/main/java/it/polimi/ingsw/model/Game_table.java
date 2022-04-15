package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Game_table {
    private int num_players;
    private int player_ID;
    private int island_counter=12;
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

    //This moves the list pointer to the right, if the list ends it starts from the beginning
    public void move_right(int moves, Double_linked_list.Node node){
        for (int i = 0; i < moves; i++) {
            if(node==null){
                node = islands.head;
            }
            node = node.next;
        }
    }

    public void move_left(int moves, Double_linked_list.Node node){
        for (int i = 0; i < moves; i++) {
            if(node==null){
                for (int j = 0; j < island_counter; j++) {
                    node = node.next;
                }
            }
            node = node.prev;
        }
    }

    public Double_linked_list merge(Double_linked_list.Node final_island, Double_linked_list.Node deleted_island){
        islands.merge_islands(islands.head, islands.head.next);
        island_counter--;
        return islands;
    }


    public List<Cloud> del_cloud(int cloud_index) {
        clouds.remove(cloud_index);
        return clouds;
    }
    public List<Cloud> getClouds() {
        return clouds;
    }

    public int getIsland_counter() {
        return island_counter;
    }

    public void setIsland_counter(int island_counter) {
        this.island_counter = island_counter;
    }

    public int getHow_many_merge(){
        return 12-island_counter;
    }
}
