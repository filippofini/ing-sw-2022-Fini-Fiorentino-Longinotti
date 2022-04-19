package it.polimi.ingsw.model;

import java.util.List;

public class Island {
    private int current_player;
    private Board[] boards;
    private boolean mother_nature;
    private int tower;
    private int influence_controller;
    private int player_controller;
    private int island_ID;
    private int[] arr_students;

    public Island(int current_player, Board[] boards, int island_ID) {
        this.island_ID = island_ID;
        this.current_player = current_player;
        this.boards = boards;
        influence_controller = 0;
        arr_students = new int[5];
        mother_nature = false;
    }


    public int check_controller() { return player_controller;}

    public void calculate_influence() {
        int temp_influence = 0;
        for (int i = 0; i<5;i++) {
            if (boards[current_player].getArrProfessors()[i]) {
                temp_influence+= arr_students[i];
            }
        }

        if (current_player == player_controller) {
            temp_influence+= tower;
        }
        if (temp_influence > influence_controller) {
            player_controller = current_player;
            tower = 1;
            influence_controller = temp_influence;

        }

    }

    public void add_tower() {}

    public void setCurrent_player(int current_player) {
        this.current_player = current_player;
    }


    public int getCurrent_player() {
        return current_player;
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

