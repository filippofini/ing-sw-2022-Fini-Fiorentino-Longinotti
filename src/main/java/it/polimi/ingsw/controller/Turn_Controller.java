package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Disk_colour;
import it.polimi.ingsw.model.Game_State;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.List;
import java.util.Scanner;

public class Turn_Controller {
    private int[] player_order;
    private Game_State GS;
    private int n_players;
    List<Player> P_L;

    public Turn_Controller(int n_players, String[] names, int wizard[], boolean expert_mode, List<Player> Player_List){
        this.n_players=n_players;
        this.P_L=Player_List;
        player_order= new int[n_players];
        for(int i=0; i<n_players;i++){
            player_order[i]=i;
        }
        GS= new Game_State(n_players, names, wizard, expert_mode);

    }

    public void planning_phase_general(){
        GS.getGT().replenish_clouds();
        for(int i=0;i<n_players;i++){
            planning_phase_personal(player_order[i]);
        }
        Calculate_Player_order();
    }
    //TODO: this method is temporaneous since the gui need to show up only to the player that need to choose the assinstant
    public void planning_phase_personal(int i){

       GS.getGT().choose_assistant(P_L.get(i));

    }
    //TODO: there can be problems with mn position(it depends if we consider island_ID or island position in the list...for now i had chosen the second)
    public void action_phase(){
        List<Student> stud_to_island;
        int choice;
        boolean check_for_tower;
        Scanner sc= new Scanner(System.in);
        for(int i=0;i<n_players;i++){

            stud_to_island=GS.getGT().getBoards()[player_order[i]].moveEntranceStudents();
            /**
             * add all the student to the islands
             */
            for(int j=0;j< stud_to_island.size();j++){

                System.out.println("choose the island where to add the student:"+stud_to_island.get(j)+"\n");
                choice=sc.nextInt();
                while (choice<0 ||choice>=GS.getGT().getHow_many_left()){
                    System.out.println("This Island does not exit, please choose a valid number:\n");
                    choice=sc.nextInt();
                }
                GS.getGT().getIslands().get(choice).add_students(stud_to_island.get(j));
            }
            System.out.println("choose how many steps Mother Nature have to do:\n");
            System.out.println("Mother Nature position: Island["+GS.getGT().getMother_nature_pos()+"]\n");
            System.out.println("Number of steps possible:"+P_L.get(player_order[i]).getChosen_card().getValue()+"\n");
            for(int j=0;j<GS.getGT().getHow_many_left();j++){
                System.out.println("Island["+j+":]\n");
                for (int k=0;k<5; k++){
                    System.out.println(Disk_colour.values()[k] +": "+GS.getGT().getIslands().get(j).getArr_students()[k]+"\n");
                }
            }
            choice=sc.nextInt();
            while (choice<0 ||choice>=P_L.get(player_order[i]).getChosen_card().getValue()){
                System.out.println("it's not possible to do this number of steps, please choose a valid number:\n");
                choice=sc.nextInt();
            }
            GS.getGT().move_mother_nature(choice);
            check_for_tower=GS.getGT().getIslands().get(GS.getGT().getMother_nature_pos()).calculate_influence(player_order[i]);
            if(check_for_tower){
                GS.getGT().getBoards()[player_order[i]].setN_towers(GS.getGT().getBoards()[player_order[i]].getN_towers()-1);
            }
            else {
                if(P_L.size() == 2){
                    GS.getGT().getBoards()[player_order[i]].setN_towers(8) ;
                }
                else if(P_L.size() == 3){
                    GS.getGT().getBoards()[player_order[i]].setN_towers(6) ;
                }
                else if(P_L.size() == 4){
                    if((player_order[i] == 1 || player_order[i] == 3)){
                        GS.getGT().getBoards()[player_order[i]].setN_towers(8);
                    }
                }

            }
            GS.getGT().merge(GS.getGT().getMother_nature_pos(),player_order[i]);
            GS.getGT().choose_cloud();

        }

    }
    public int[] getPlayer_order() {
        return player_order;
    }

    public void Calculate_Player_order() {
        int min=P_L.get(0).getChosen_card().getValue();
        int player=0;
        int count=0;

        for(int i=0;i<n_players;i++){
            for(int j=0;j<n_players-count;j++){
                if(P_L.get(j).getChosen_card().getValue()<min){
                    min=P_L.get(j).getChosen_card().getValue();
                    player=P_L.get(j).getPlayer_ID();
                }
            }
            count++;
            player_order[i]=player;
        }
    }
}
