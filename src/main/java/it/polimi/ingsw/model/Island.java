package it.polimi.ingsw.model;

public class Island {
    private boolean mother_nature;
    private int tower;
    private int influence;
    private int player_controller;
    private Island next_island;
    private Island previous_island;
    private int island_ID;
    private int[5] arr_students;

    public Island() {

    }

    public boolean check_mn() { return true; }

    public int check_controller() { return player_controller  ;}

    public int calculate_influence() {return influence;}

    public void add_tower() {}

}

