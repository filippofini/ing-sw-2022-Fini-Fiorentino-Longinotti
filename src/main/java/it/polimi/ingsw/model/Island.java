package it.polimi.ingsw.model;

import java.util.List;

public class Island {
    private int current_player;
    private Board[] boards;
    private boolean mother_nature;
    private int tower;
    private int influence_controller;
    private int player_controller;
    private Island next_island;
    private Island previous_island;
    private int island_ID;
    private int[] arr_students;

    public Island(int current_player, Board[] boards) {
        this.current_player = current_player;
        this.boards = boards;
        influence_controller =0;
        arr_students = new int[5];


    }

    public boolean check_mn() { return true; }

    public int check_controller() { return player_controller  ;}

    public void calculate_influence() {
        int temp_influence =0;
        for (int i = 0; i<5;i++) {
            if (boards[current_player].getArrProfessors()[i]==true) {
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
}

