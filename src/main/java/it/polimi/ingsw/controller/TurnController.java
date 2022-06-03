package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.character.MagicMailman;

import java.util.List;
import java.util.Scanner;

/**
 * This class represents the controller of the turn. It manages the planning phase and the action phase of each player.
 * It controls and calculates the player order for the next round.
 */
public class TurnController {
    private int[] player_order;
    private GameState GS;
    private int n_players;
    List<Player> P_L;
    CharacterCard played_cCard;

    /**
     * Constructor of the class.
     * @param n_players The number of players in the game.
     * @param names The strings containing the names of the players.
     * @param wizard The array of int containing the numbers representing the wizards.
     * @param expert_mode {@code True} if expert mode is enabled, {@code False} if not.
     * @param Player_List The list of player for turn order.
     */
    public TurnController(int n_players, String[] names, int wizard[], boolean expert_mode, List<Player> Player_List){
        this.n_players=n_players;
        this.P_L=Player_List;
        player_order= new int[n_players];
        for(int i=0; i<n_players;i++){
            player_order[i]=i;
        }
        GS = new GameState(n_players, names, wizard, expert_mode,0);
    }

    /**
     * This method defines the planning phase by calculating the order of player
     * and replenishing the clouds at the beginning of the round
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
     * This method represent the planning phase of the current player. Here the player decides which assistance card
     *  he wants to play.
     * @param i The ID of the player(?).
     */
    public void planning_phase_personal(int i){

       GS.getGT().choose_assistant(P_L.get(i));

    }


    /**
     * This method represents the action phase of the round. It moves the students to the islands, moves mother nature
     * and checks if island can be conquered and merged.
     */
    public void action_phase(){
        List<Student> stud_to_island;
        int choice;
        boolean check_for_tower;
        int[] tempCloud;
        Scanner sc= new Scanner(System.in);
        for(int i=0;i<n_players;i++){
            GS.setCurr_player(player_order[i]);
            stud_to_island=GS.getGT().getBoards()[player_order[i]].moveEntranceStudents(GS);
            //TODO:setProfessors()
            GS.getGT().setProfessors();

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

            //If the played character card is the magic mailman, it increases the possible movement by 2
            if (played_cCard.equals(new MagicMailman())){
                played_cCard.effect(GS);
            }
            System.out.println("Number of steps possible:"+P_L.get(player_order[i]).getChosen_card().getValue()+"\n");
            for(int j=0;j<GS.getGT().getHow_many_left();j++){
                System.out.println("Island["+j+":]\n");
                for (int k=0;k<5; k++){
                    System.out.println(DiskColour.values()[k] +": "+GS.getGT().getIslands().get(j).getArr_students()[k]+"\n");
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

            //if(!check_for_tower) means that you have gained the control of the island
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
            tempCloud=GS.getGT().choose_cloud().getArr_students();
            GS.getGT().getBoards()[player_order[i]].setArrEntranceStudents(tempCloud);
            //TODO: setused getused in all character cards
            /*
            for(int n=0;n<3;n++){
                if(played_cCard.equals(new Knight())){
                    if((GS.getGT().getArr_character()[n].getUsed())){
                        GS.getGT().getArr_character()[n].setUsed(false);
                        for(int m=0;m<GS.getGT().getIslands().size();m++){
                            GS.getGT().getIslands().get(m).setExtra_influence(0);
                        }
                    }
                }
            }*/

        }

    }

    /**
     * This method calculates the order of the players for the turn.
     * The player with the lowest value of the played assistance card starts first.
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

    public GameState getGS() {
        return GS;
    }

    public void setGS(GameState GS) {
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
