package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.character.Knight;
import it.polimi.ingsw.model.character.MagicMailman;
import it.polimi.ingsw.network.message.toClient.ChooseAssistantCardRequest;
import it.polimi.ingsw.network.message.toClient.ChooseIslandRequest;
import it.polimi.ingsw.network.message.toClient.MoveMnRequest;
import it.polimi.ingsw.network.message.toClient.DisplayIslandinfoRequest;
import it.polimi.ingsw.network.server.ClientHandler;

import java.util.List;

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
    private ClientHandler clienthandler;
    private boolean endgame;
    private GameController gameController;

    /**
     * Constructor of the class.
     * @param n_players The number of players in the game.
     * @param names The strings containing the names of the players.
     * @param wizard The array of int containing the numbers representing the wizards.
     * @param expert_mode {@code True} if expert mode is enabled, {@code False} if not.
     * @param Player_List The list of player for turn order.
     */
    public TurnController(int n_players, String[] names, int wizard[], boolean expert_mode, List<Player> Player_List){
        endgame=false;
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
        gameController.setCheck(true);
        GS.getGT().replenish_clouds(this);
        for(int i=0;i<n_players;i++){
            planning_phase_personal(player_order[i]);
        }
        Calculate_Player_order();
    }


    /**
     * This method represent the planning phase of the current player. Here the player decides which assistance card
     *  he wants to play.
     * @param i The ID of the player(?).
     */
    public void planning_phase_personal(int i){


       clienthandler.sendMessageToClient(new ChooseAssistantCardRequest(P_L.get(i),GS.getGT()));
        GS.getGT().choose_assistant(P_L.get(i),clienthandler.getAssistantCardChosen());
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

        for(int i=0;i<n_players;i++){
            if(endgame){
                break;
            }
            GS.setCurr_player(player_order[i]);
            stud_to_island=GS.getGT().getBoards()[player_order[i]].moveEntranceStudents(GS,clienthandler);

            //add all the student to the islands
            for(int j=0;j< stud_to_island.size();j++){

                clienthandler.sendMessageToClient(new ChooseIslandRequest( GS.getGT().getIslands(),stud_to_island.get(j)));
                GS.getGT().getIslands().get(clienthandler.getIslandToMove()).add_students(stud_to_island.get(j));
            }
            //TODO:want to play a card CLI?

            //If the played character card is the magic mailman, it increases the possible movement by 2
            if (played_cCard.equals(new MagicMailman())){
                played_cCard.effect(GS);
            }

            for(int j=0;j<GS.getGT().getHow_many_left();j++){
              clienthandler.sendMessageToClient(new DisplayIslandinfoRequest(GS.getGT().getIslands().get(j),j));
            }
            clienthandler.sendMessageToClient(new MoveMnRequest(GS.getGT().getMother_nature_pos(),P_L.get(GS.getCurr_player())));
            GS.getGT().move_mother_nature(clienthandler.getMnmovement());
            check_for_tower=GS.getGT().getIslands().get(GS.getGT().getMother_nature_pos()).calculate_influence(player_order[i],GS.getGT().getBoards());

            //if(!check_for_tower) means that you have gained the control of the island
            if(!check_for_tower){
                if(n_players==4){
                    if((player_order[i]==1 || player_order[i]==2) && GS.getGT().getBoards()[1].getN_towers()>0 ){
                        GS.getGT().getBoards()[1].setN_towers(GS.getGT().getBoards()[1].getN_towers()-1);
                        if(GS.getGT().getBoards()[1].getN_towers()==0){
                            endgame=true;
                        }
                    }

                    else if((player_order[i]==3 || player_order[i]==4) && GS.getGT().getBoards()[3].getN_towers()>0){
                        GS.getGT().getBoards()[3].setN_towers(GS.getGT().getBoards()[3].getN_towers()-1);
                        if(GS.getGT().getBoards()[3].getN_towers()==0){
                            endgame=true;
                        }
                    }
                }
                else{
                    if(GS.getGT().getBoards()[i].getN_towers()>0){
                    GS.getGT().getBoards()[player_order[i]].setN_towers(GS.getGT().getBoards()[player_order[i]].getN_towers()-1);
                        if(GS.getGT().getBoards()[i].getN_towers()==0){
                            endgame=true;
                        }
                    }
                }


            }
            if(endgame){
                break;
            }


            GS.getGT().merge(GS.getGT().getMother_nature_pos(),player_order[i],GS.getGT().getBoards());
            if(GS.getGT().getIslands().size()==3){
                endgame=true;
            }
            if(endgame){
                break;
            }
            tempCloud=GS.getGT().choose_cloud(clienthandler).getArr_students();
            GS.getGT().getBoards()[player_order[i]].setArrEntranceStudents(tempCloud);

            for(int n=0;n<3;n++){
                if(played_cCard.equals(new Knight())){
                    for(int m=0;m<GS.getGT().getIslands().size();m++){
                        GS.getGT().getIslands().get(m).setExtra_influence(0);
                    }
                }
            }


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
    public boolean getendgame(){
        return endgame;
    }

    public void setEndgame(boolean endgame) {
        this.endgame = endgame;
    }
    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}
