package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.message.toClient.*;
import it.polimi.ingsw.network.server.ClientHandler;
import java.util.List;


/**
 * This class represents the controller of the turn. It manages the planning phase and the action phase of each player.
 * It controls and calculates the player order for the next round.
 */
public class TurnController {
    private final int[] playerOrder;
    private final GameState GS;
    private final int numPlayers;
    List<Player> playersList;
    CharacterCard played_cCard;
    List<ClientHandler> clientHandlerList;
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
    public TurnController(int n_players, String[] names, int wizard[], boolean expert_mode, List<Player> Player_List,List<ClientHandler> clienthandler){
        endgame=false;
        this.numPlayers =n_players;
        this.playersList =Player_List;
        playerOrder = new int[n_players];
        this.clientHandlerList =clienthandler;
        for(int i=0; i<n_players;i++){
            playerOrder[i]=i;
        }
        GS = new GameState(n_players, names, wizard,0, playersList);
    }

    /**
     * This method defines the planning phase by calculating the order of player
     * and replenishing the clouds at the beginning of the round
     */
    public void planningPhaseGeneral(){
        GS.getGameTable().replenishClouds(this);
        for(int i = 0; i< numPlayers; i++){
            planning_phase_personal(playerOrder[i]);
        }
        Calculate_Player_order();
    }


    /**
     * This method represent the planning phase of the current player. Here the player decides which assistance card
     *  he wants to play.
     * @param i The ID of the player(?).
     */
    public void planning_phase_personal(int i){

        if(playersList.get(i).getDeck().getCards().size()>0){
            clientHandlerList.get(i).sendMessageToClient(new ChooseAssistantCardRequest(playersList.get(i),GS.getGameTable()));
       GS.getGameTable().chooseAssistant(playersList.get(i), clientHandlerList.get(i).getAssistantCardChosen());}
        else if(playersList.get(i).getDeck().getCards().size()==0){
            endgame=true;
        }

    }


    /**
     * This method represents the action phase of the round. It moves the students to the islands, moves mother nature
     * and checks if island can be conquered and merged.
     * It's also used to play character cards.
     */
    public void action_phase(){
        List<Student> stud_to_island;
        boolean check_for_tower;
        int[] tempCloud;
        Student[] tempCCStud=new Student[4] ;

        for(int i = 0; i< numPlayers; i++){

            if(endgame){
                break;
            }
            GS.setCurrPlayer(playerOrder[i]);

            if (gameController.getGameMode() == GameMode.EXPERT) {
                clientHandlerList.get(playerOrder[i]).sendMessageToClient(new UseCharacterCardRequest());
                if (clientHandlerList.get(playerOrder[i]).getUseCharacterCard() == 1) {
                    clientHandlerList.get(playerOrder[i]).sendMessageToClient(new ChooseCharacterCardRequest(playersList.get(playerOrder[i]), GS.getGameTable().getCharacterCards()));
                    if (clientHandlerList.get(playerOrder[i]).getCanBeUsed()) {
                        if(GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].getIDCode()==1){
                            for(int l=0;l<4;l++){
                                tempCCStud[l]=new Student(GS.getGameTable().getBoards()[i].inverseColor(GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].getStudents()[l]));
                            }
                            clientHandlerList.get(playerOrder[i]).sendMessageToClient(new ShowStudentRequest(tempCCStud));
                            GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].setChosenStudent(clientHandlerList.get(playerOrder[i]).getMonkStudent());
                            clientHandlerList.get(playerOrder[i]).sendMessageToClient(new HeraldIslandRequest(GS.getGameTable().getIslands()));
                            GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].setIndexTo(clientHandlerList.get(playerOrder[i]).getHeraldIsland());
                            GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].effect(getGS());
                        }
                        else if(GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].getIDCode()==2){

                            GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].effect(getGS());
                        }
                        else if(GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].getIDCode()==3){

                            clientHandlerList.get(playerOrder[i]).sendMessageToClient(new HeraldIslandRequest(GS.getGameTable().getIslands()));
                            GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].setIndexTo(clientHandlerList.get(playerOrder[i]).getHeraldIsland());
                            GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].effect(getGS());
                        }
                        else if(GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].getIDCode()==4){

                            GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].effect(getGS());
                        }
                        else if(GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].getIDCode()==5){

                            clientHandlerList.get(playerOrder[i]).sendMessageToClient(new HeraldIslandRequest(GS.getGameTable().getIslands()));
                            GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].setIndexTo(clientHandlerList.get(playerOrder[i]).getHeraldIsland());
                            GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].effect(getGS());
                        }
                        else if(GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].getIDCode()==6){

                            GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].effect(getGS());
                        }
                        else if(GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].getIDCode()==8){

                            GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].effect(getGS());
                        }
                        else if(GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].getIDCode()==11){
                            for(int l=0;l<4;l++){
                                tempCCStud[l]=new Student(GS.getGameTable().getBoards()[i].inverseColor(GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].getStudents()[l]));
                            }
                            clientHandlerList.get(playerOrder[i]).sendMessageToClient(new ShowStudentRequest(tempCCStud));
                            GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].setChosenStudent(clientHandlerList.get(playerOrder[i]).getMonkStudent());
                            GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].setCurrentPlayer(playerOrder[i]);
                            GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()].effect(getGS());
                        }
                        played_cCard=GS.getGameTable().getCharacterCards()[clientHandlerList.get(playerOrder[i]).getChCardUsed()];
                    }
                }
            }

            stud_to_island=GS.getGameTable().getBoards()[playerOrder[i]].moveEntranceStudents(GS, clientHandlerList.get(playerOrder[i]));


            //add all the student to the islands
            for(int j=0;j< stud_to_island.size();j++){

                clientHandlerList.get(playerOrder[i]).sendMessageToClient(new ChooseIslandRequest( GS.getGameTable().getIslands(),stud_to_island.get(j)));
                GS.getGameTable().getIslands().get(clientHandlerList.get(playerOrder[i]).getIslandToMove()).addStudents(stud_to_island.get(j));
            }




            for(int j = 0; j<GS.getGameTable().getHowManyLeft(); j++){
              clientHandlerList.get(playerOrder[i]).sendMessageToClient(new DisplayIslandInfoRequest((GS.getGameTable().getIslands().get(j)),j));
            }

            clientHandlerList.get(playerOrder[i]).sendMessageToClient(new MoveMnRequest(GS.getGameTable().getMotherNaturePos(), playersList.get(GS.getCurrPlayer())));
            GS.getGameTable().moveMotherNature(clientHandlerList.get(playerOrder[i]).getMnmovement());


            check_for_tower=GS.getGameTable().getIslands().get(GS.getGameTable().getMotherNaturePos()).calculateInfluence(playerOrder[i],GS.getGameTable().getBoards());

            //if(!check_for_tower) means that you have gained the control of the island
            if(!check_for_tower){
                if(numPlayers ==4){
                    if((playerOrder[i]==1 || playerOrder[i]==2) && GS.getGameTable().getBoards()[1].getNumTowers()>0 ){
                        GS.getGameTable().getBoards()[1].setNumTowers(GS.getGameTable().getBoards()[1].getNumTowers()-1);
                        if(GS.getGameTable().getBoards()[1].getNumTowers()==0){
                            endgame=true;
                        }
                    }

                    else if((playerOrder[i]==3 || playerOrder[i]==4) && GS.getGameTable().getBoards()[3].getNumTowers()>0){
                        GS.getGameTable().getBoards()[3].setNumTowers(GS.getGameTable().getBoards()[3].getNumTowers()-1);
                        if(GS.getGameTable().getBoards()[3].getNumTowers()==0){
                            endgame=true;
                        }
                    }
                }
                else{
                    if(GS.getGameTable().getBoards()[i].getNumTowers()>0){
                    GS.getGameTable().getBoards()[playerOrder[i]].setNumTowers(GS.getGameTable().getBoards()[playerOrder[i]].getNumTowers()-1);
                        if(GS.getGameTable().getBoards()[i].getNumTowers()==0){
                            endgame=true;
                        }
                    }
                }


            }
            if(endgame){
                break;
            }


            GS.getGameTable().merge(GS.getGameTable().getMotherNaturePos(), playerOrder[i],GS.getGameTable().getBoards());

            if(GS.getGameTable().getIslands().size()==3){
                endgame=true;
            }
            if(endgame){
                break;
            }
            tempCloud=GS.getGameTable().chooseCloud(clientHandlerList.get(playerOrder[i])).getArrStudents();

            GS.getGameTable().getBoards()[playerOrder[i]].setArrEntranceStudents(tempCloud);

            if(clientHandlerList.get(playerOrder[i]).getUseCharacterCard()==1 && played_cCard!=null){
                if(played_cCard.getIDCode()==8){
                    for(int m = 0; m<GS.getGameTable().getIslands().size(); m++){
                        GS.getGameTable().getIslands().get(m).setExtraInfluence(0);
                    }
                }
                if(played_cCard.getIDCode()==6){
                    for(int m = 0; m<GS.getGameTable().getIslands().size(); m++){
                        GS.getGameTable().getIslands().get(m).setIncludeTowers(true);
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
        int min=-1;
        int player=0;

        for(int i = 0; i< numPlayers; i++){
            for(int j = 0; j< numPlayers; j++){
                if((playersList.get(j).getChosenCard().getValue()<min  || min==-1) && playersList.get(j).isChosen()==false){
                    min= playersList.get(j).getChosenCard().getValue();
                    player= playersList.get(j).getPlayer_ID();

                }
            }

            playersList.get(player).setChosen(true);

            min=-1;
            playerOrder[i]=player;

        }
        for(int i = 0; i< numPlayers; i++){
            playersList.get(i).setChosen(false);
        }
    }

    /**
     * This method returns the game state.
     * @return The game state.
     */
    public GameState getGS() {
        return GS;
    }

    /**
     * This method returns the end game.
     * @return The end game.
     */
    public boolean getendgame(){
        return endgame;
    }

    /**
     * This method sets the end game.
     * @param endgame The end game.
     */
    public void setEndgame(boolean endgame) {
        this.endgame = endgame;
    }

    /**
     * This method sets the game controller.
     * @param gameController The game controller.
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

}
