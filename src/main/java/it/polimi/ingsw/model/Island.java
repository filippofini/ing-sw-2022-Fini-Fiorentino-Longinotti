package it.polimi.ingsw.model;

import java.util.List;

/**
 * Class island
 */
public class Island {

    private Board[] boards;
    private boolean mother_nature;
    private int tower;
    private int influence_controller;
    private int player_controller;
    private int island_ID;
    private int[] arr_students;

    /**
     * Constructor of the class
     * @param boards reference to boards
     * @param island_ID starting ID of the island, it's not very useful
     * @param tower_colour puts island starter to the island, that means no tower
     */
    public Island( Board[] boards, int island_ID, Tower_colour tower_colour) {
        this.island_ID = island_ID;

        this.boards = boards;
        this.tower = tower_colour.getTower_translate();
        influence_controller = 0;
        arr_students = new int[5];
        mother_nature = false;
        player_controller=-1;
    }

    /**
     * Calculate the influence of the player in the island
     * @param current_player current player of the turn
     * @param Boards array of the boards
     * @return false if the control of the island changed
     */
    public boolean calculate_influence(int current_player,Board[] Boards) {
        boolean same_player=true;
        int temp_influence = 0;
        for (int i = 0; i<5;i++) {
            if (boards[current_player].getArrProfessors()[i]) {
                temp_influence+= arr_students[i];
            }
        }

        if (temp_influence > influence_controller) {
            if(current_player!=player_controller){
                if(boards.length==4){
                    if(current_player==1 || current_player==2){
                        Boards[1].setN_towers(Boards[1].getN_towers()+tower);
                        player_controller = current_player;
                        tower = 1;
                        influence_controller = temp_influence;
                        same_player=false;
                    }
                    else if(current_player==3 || current_player==4){
                        Boards[3].setN_towers(Boards[3].getN_towers()+tower);
                        player_controller = current_player;
                        tower = 1;
                        influence_controller = temp_influence;
                        same_player=false;
                    }
                }
                else{
                    Boards[player_controller].setN_towers(Boards[player_controller].getN_towers()+tower);
                    player_controller = current_player;
                    tower = 1;
                    influence_controller = temp_influence;
                    same_player=false;
                }

            }
            else {
                influence_controller = temp_influence;
            }
        }
        return same_player;
    }


    /**
     * Add students to the island
     * @param transfer list of students to be added
     */
    public void add_students(Student transfer) {
            arr_students[transfer.getColour()]++;
    }


    public int check_controller() { return player_controller;}

    public void add_tower(int current_player) {
        this.tower = boards[current_player].getTower();
    }

    public Board[] getBoards() {
        return boards;
    }

    public boolean isMother_nature() {
        return mother_nature;
    }

    public int getTower() {
        return tower;
    }

    public int getInfluence_controller() {
        return influence_controller;
    }

    public int getPlayer_controller() {
        return player_controller;
    }

    public int getIsland_ID() {
        return island_ID;
    }

    public int[] getArr_students() {
        return arr_students;
    }


    public void setBoards(Board[] boards) {
        this.boards = boards;
    }

    public void setMother_nature(boolean mother_nature) {
        this.mother_nature = mother_nature;
    }

    public void setTower(int tower) {
        this.tower = tower;
    }

    public void setInfluence_controller(int influence_controller) {
        this.influence_controller = influence_controller;
    }

    public void setPlayer_controller(int player_controller) {
        this.player_controller = player_controller;
    }

    public void setIsland_ID(int island_ID) {
        this.island_ID = island_ID;
    }

    public void setArr_students(int[] arr_students) {
        this.arr_students = arr_students;
    }
}

