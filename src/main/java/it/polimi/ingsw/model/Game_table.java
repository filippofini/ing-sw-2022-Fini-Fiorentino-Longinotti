package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * Class game table
 */
public class Game_table {
    private final int num_players;
    private int player_ID;
    private int current_player;
    private int island_counter=12;
    private int island_index;
    private Board[] boards;
    private LinkedList<Island> islands;
    private List<Cloud> clouds;
    private int mother_nature_pos;
    private int[] bag;
    private Character_card[] arr_character;
    private Turn turn;
    private Assistance_card[] discard_deck;

    /**
     * Constructor of the class
     * @param num_players number of players can be 2,3 or 4
     * @param turn first turn
     */
    public Game_table(int num_players, Turn turn){
        this.num_players = num_players;
        this.turn = turn;
        this.current_player = turn.getCurrent_player();

        bag = new int[5];
        for(int i=0;i<5;i++) {
            bag[i] = 2;
        }

        boards = new Board[num_players];
        for(int i=0;i<num_players;i++) {
            boards[i] = new Board(num_players,i+1, Tower_colour.values()[i]);
        }

        islands = new LinkedList<Island>();
        for(int i=0;i<12;i++){
            islands.add(new Island(turn.getCurrent_player(), boards, i+1, Tower_colour.STARTER));
        }

        setMother_nature_start();
        bag_island_start();

        clouds = new ArrayList<Cloud>();
        for(int i=0;i<num_players;i++){
            clouds.add(new Cloud());
        }
        cloud_start();

        discard_deck = new Assistance_card[num_players];
        for (int i = 0; i < num_players; i++) {
            discard_deck[i] = Assistance_card.STARTER;
        }

        //TODO: initialization of arr_character

    }


    /**
     * Check if an assistance card has been already played
     * @param chosen card chosen to be played
     * @return return false if card is already played
     */
    public boolean check_if_playable(Assistance_card chosen){
        boolean playable_card = true;
        for (int i = 0; i < num_players && playable_card; i++) {
            if(discard_deck[i].equals(chosen)){
                playable_card = false;
            }
        }
        return playable_card;
    }

    /**
     * Check if islands can be merged. Checks previous and next island
     * @param island_index index of the island to be checked.
     * @return return an array of two integers. index 0 is the previous island, index 1 is the next island. If one values is -1, that island can't be merged
     */
    private int[] check_merge(int island_index){
        int[] indexes = new int[]{-1,-1} ;

        /**
         * Check if there are islands prev or next to the current that can be merged
         */
        if(island_index<island_counter-1 && island_index>0) {
            if (islands.get(island_index).getTower() == islands.get(island_index + 1).getTower()) {
                indexes[1] = island_index+1;
            }
            if (islands.get(island_index).getTower() == islands.get(island_index-1).getTower()){
                indexes[0] = island_index-1;
            }
        }

        /**
         * Check the same thing for the last island of the list but goes to 0 to check the next
         */
        else if(island_index == island_counter-1){
            if (islands.get(island_index).getTower() == islands.get(0).getTower()) {
                indexes[1] = 0;
            }
            if (islands.get(island_index).getTower() == islands.get(island_index-1).getTower()){
                indexes[0] = island_index-1;
            }
        }

        /**
         * Check the first element of the list. If prev can be merged goes to the last element of the list
         */
        else if (island_index==0){
            if (islands.get(island_index).getTower() == islands.get(1).getTower()) {
                indexes[1] = 1;
            }
            if (islands.get(island_index).getTower() == islands.get(island_counter-1).getTower()){
                indexes[0] = island_counter-1;
            }
        }

        return indexes;
    }


    /**
     * Merge of close islands. Before merging, it checks what merges can be done
     * @param island_index the index of the island that will be final
     */
    public void merge(int island_index) {
        this.island_index = island_index;
        int[] toMerge_indexes = check_merge(island_index);

        if (toMerge_indexes[0] >= 0) {
            int[] students1 = islands.get(island_index).getArr_students();
            int[] students2 = islands.get(toMerge_indexes[0]).getArr_students();
            for (int i = 0; i < 5; i++) {
                students1[i] += students2[i];
            }
            islands.get(island_index).setArr_students(students1);
            islands.get(island_index).setTower(islands.get(island_index).getTower() + islands.get(toMerge_indexes[0]).getTower());

        }
        if (toMerge_indexes[1] >= 0) {
            int[] students1 = islands.get(island_index).getArr_students();
            int[] students2 = islands.get(toMerge_indexes[1]).getArr_students();
            for (int i = 0; i < 5; i++) {
                students1[i] += students2[i];
            }
            islands.get(island_index).setArr_students(students1);
            islands.get(island_index).setTower(islands.get(island_index).getTower() + islands.get(toMerge_indexes[1]).getTower());
        }

        islands.get(island_index).calculate_influence();

        boolean removed = false;
        if (toMerge_indexes[0] >= 0) {
            islands.remove(toMerge_indexes[0]);
            removed = true;
            island_counter--;
        }

        if (toMerge_indexes[1]>=0) {
            if (removed) {
                //If the removed island is after the next one to remove no problem
                if (toMerge_indexes[0] > toMerge_indexes[1]) {
                    islands.remove(toMerge_indexes[1]);
                }
                //If the removed island is before the next one to remove the index goes down by 1
                else{
                    islands.remove(toMerge_indexes[1] - 1);
                }
            }
            else{
                islands.remove(toMerge_indexes[1]);
            }
            island_counter--;
        }
    }

    /**
     * This move mother nature by moves decided by the current player
     * @param moves number of moves that the player decides, not the max possible moves
     */
    public void move_mother_nature(int moves){
        islands.get(mother_nature_pos).setMother_nature(false);
        if(mother_nature_pos+moves<island_counter) {
            islands.get(mother_nature_pos+moves).setMother_nature(true);
            setMother_nature_pos(mother_nature_pos+moves);
        }else {
            moves = (moves+mother_nature_pos)%island_counter;
            islands.get(moves).setMother_nature(true);
            setMother_nature_pos(moves);
        }
    }

    /**
     * Puts mother nature in a random island
     */
    private void setMother_nature_start() {
        Random rand = new Random();
        this.mother_nature_pos = rand.nextInt(12);
        islands.get(this.mother_nature_pos).setMother_nature(true);
    }

    /**
     * Puts the first students on the islands
     */
    private void bag_island_start(){

        Random rand = new Random();

        int tempRand;

        for(int i=0;i<12;i++){
            tempRand=rand.nextInt(5);

                while (bag[tempRand] == 0) {
                    tempRand = rand.nextInt(5);
                }


            if(i!= (this.mother_nature_pos+6)%12 && i!=this.mother_nature_pos) {
                bag[tempRand]--;
                islands.get((i + this.mother_nature_pos) % 12).getArr_students()[tempRand]++;
            }

        }
        for(int i=0;i<5;i++) {
            bag[i] = 24;
        }

    }

    /**
     * Puts the first students on the clouds
     */
    private void cloud_start(){
        Random rand = new Random();
        int temprand;
        if(num_players==2 || num_players==4){
            for(int i=0;i<num_players;i++){
                for(int j=0;j<3;j++){
                    temprand=rand.nextInt(5);
                    clouds.get(i).getArr_students()[temprand]++;
                    bag[temprand]--;
                }
            }
        }
        else if(num_players==3){
            for(int i=0;i<num_players;i++){
                for(int j=0;j<4;j++){
                    temprand=rand.nextInt(5);
                    clouds.get(i).getArr_students()[temprand]++;
                    bag[temprand]--;
                }
            }
        }
    }

    public void setMother_nature_pos(int mother_nature_pos) {
        this.mother_nature_pos = mother_nature_pos;
    }

    public int getMother_nature_pos() {
        return mother_nature_pos;
    }

    public List<Cloud> del_cloud(int cloud_index) {
        clouds.remove(cloud_index);
        return clouds;
    }

    public List<Cloud> getClouds() {
        return clouds;
    }

    public Board[] getBoards() {
        return boards;
    }

    public int getIsland_counter() {
        return island_counter;
    }

    public void setIsland_counter(int island_counter) {
        this.island_counter = island_counter;
    }

    public int getHow_many_left(){
        return 12-island_counter;
    }

    public int getPlayer_ID() {
        return player_ID;
    }

    public LinkedList<Island> getIslands() {
        return islands;
    }

    public int getNum_players() {
        return num_players;
    }

    public int getCurrent_player() {
        return current_player;
    }

    public int getIsland_index() {
        return island_index;
    }

    public int[] getBag() {
        return bag;
    }

    public Character_card[] getArr_character() {
        return arr_character;
    }

    public Turn getTurn() {
        return turn;
    }

    public Assistance_card[] getDiscard_deck() {
        return discard_deck;
    }
}
