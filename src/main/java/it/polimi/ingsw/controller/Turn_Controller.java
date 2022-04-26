package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Disk_colour;
import it.polimi.ingsw.model.Game_State;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.List;
import java.util.Scanner;

/**
 * Class turn controller
 */
public class Turn_Controller {
    private int[] player_order;
    private Game_State GS;
    private int n_players;
    List<Player> P_L;

    /**
     * Constructor of the class
     * @param n_players number of players
     * @param names strings containing the names of the players
     * @param wizard array of int containing the numbers of the wizards
     * @param expert_mode true if expert mode is enabled, false otherwise
     * @param Player_List list of player for turn priority
     */
    public Turn_Controller(int n_players, String[] names, int wizard[], boolean expert_mode, List<Player> Player_List){
        this.n_players=n_players;
        this.P_L=Player_List;
        player_order= new int[n_players];
        for(int i=0; i<n_players;i++){
            player_order[i]=i;
        }
        GS = new Game_State(n_players, names, wizard, expert_mode);
    }

    /**
     * General method that defines the planning phase
     */
    public void planning_phase_general(){
        GS.getGT().replenish_clouds();
        for(int i=0;i<n_players;i++){
            planning_phase_personal(player_order[i]);
        }
        Calculate_Player_order();
    }

    //TODO: this method is temporary because the gui need to show up just to the player that has to choose the assistant
    /**
     * Method for the planning phase of each player
     * @param i ID of the player(?)
     */
    public void planning_phase_personal(int i){

       GS.getGT().choose_assistant(P_L.get(i));

    }

    //TODO: there can be problems with mn position(it depends if we consider island_ID or island position in the list...for now I'm choosing the second)
    /**
     * Method for the action phase
     */
    public void action_phase(){
        List<Student> stud_to_island;
        int choice;
        boolean check_for_tower;
        Scanner sc= new Scanner(System.in);
        for(int i=0;i<n_players;i++){

            stud_to_island=GS.getGT().getBoards()[player_order[i]].moveEntranceStudents();
            //add all the student to the islands
            for(int j=0;j< stud_to_island.size();j++){

                System.out.println("Choose the island where to add the student:"+stud_to_island.get(j)+"\n");
                choice=sc.nextInt();
                while (choice<0 ||choice>=GS.getGT().getHow_many_left()){
                    System.out.println("This Island does not exit, please choose a valid number:\n");
                    choice=sc.nextInt();
                }
                GS.getGT().getIslands().get(choice).add_students(stud_to_island.get(j));
            }
            System.out.println("Choose how many steps Mother Nature have to do:\n");
            System.out.println("Mother Nature position: Island["+GS.getGT().getMother_nature_pos()+"]\n");
            System.out.println("Number of steps possible:"+P_L.get(player_order[i]).getChosen_card().getValue()+"\n");
            for(int j=0;j<GS.getGT().getHow_many_left();j++){
                System.out.println("Island["+j+":]\n");
                for (int k=0;k<5; k++){
                    System.out.println(Disk_colour.values()[k] +": "+GS.getGT().getIslands().get(j).getArr_students()[k]+"\n");
                }
                System.out.println("Owned by: player "+GS.getGT().getIslands().get(j).getPlayer_controller()+"\n");
                System.out.println("Influence: "+GS.getGT().getIslands().get(j).getInfluence_controller()+"\n");
            }
            choice=sc.nextInt();
            while (choice<0 ||choice>=P_L.get(player_order[i]).getChosen_card().getValue()){
                System.out.println("It's not possible to do this number of steps, please choose a valid number:\n");
                choice=sc.nextInt();
            }
            GS.getGT().move_mother_nature(choice);
            check_for_tower=GS.getGT().getIslands().get(GS.getGT().getMother_nature_pos()).calculate_influence(player_order[i],GS.getGT().getBoards());
            /**
             * if(!check_for_tower) means that you have gained the control of the island
             */

            if(!check_for_tower){
                if(n_players==4){
                    if(player_order[i]==1 || player_order[i]==2){
                        GS.getGT().getBoards()[1].setN_towers(GS.getGT().getBoards()[1].getN_towers()-1);
                    }
                    else if(player_order[i]==3 || player_order[i]==4){
                        GS.getGT().getBoards()[3].setN_towers(GS.getGT().getBoards()[3].getN_towers()-1);
                    }
                }
                else{
                    GS.getGT().getBoards()[player_order[i]].setN_towers(GS.getGT().getBoards()[player_order[i]].getN_towers()-1);
                }

            }

            GS.getGT().merge(GS.getGT().getMother_nature_pos(),player_order[i],GS.getGT().getBoards());
            GS.getGT().choose_cloud();

        }

    }

    /**
     * Calculates the order of the players for the turn
     */
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

    public int[] getPlayer_order() {
        return player_order;
    }

    public void setPlayer_order(int[] player_order) {
        this.player_order = player_order;
    }

    public Game_State getGS() {
        return GS;
    }

    public void setGS(Game_State GS) {
        this.GS = GS;
    }

    public int getN_players() {
        return n_players;
    }

    public void setN_players(int n_players) {
        this.n_players = n_players;
    }

    public List<Player> getP_L() {
        return P_L;
    }

    public void setP_L(List<Player> p_L) {
        P_L = p_L;
    }
}
