package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class CLI implements View {

    private Client client;
    private Thread inputObserverOutOfTurn;

    public static void main(String[] args) {
        CLI cli= new CLI();
        cli.setUpGame();
    }

    private void setUpGame(){
        boolean error = true;
        boolean firstTry = true;
        Logo.print();
        //MatchData.getInstance().setView(this);
        while (error) {
            client = StartGame.InitialConnection(this, firstTry);
            try {
                client.start();
                error = false;
            } catch (IOException e) {
                firstTry = false;
            }
        }
    }




    public void displayMessage(String message) {
        System.out.println(message);
    }


    public void displayNicknameRequest(boolean retry, boolean alreadyTaken) {

        StartGame.displayNicknameRequest(client,retry,alreadyTaken);
    }
    public static Predicate<Integer> conditionOnIntegerRange(int min, int max){
        return p -> p >= min && p <= max;
    }

    public void displayGameModeRequest() {

        StartGame.displayGameModeRequest(client);
    }
    public void displayNumberOfPlayersRequest(){
        StartGame.NumberOfPlayersRequest(client);
    }
    public void displayWaitingMessage(){
        StartGame.displayWaitingMessage();
    }
    public void motherNatureMovementRequest(int Mn_pos, Player Current_Player){
        MnCLI.motherNatureMovementRequest(client,Mn_pos,Current_Player);
    }
    public void displayIslandInfo(Island island, int islandID){
        IslandCLI.displayIslandInfo(island,islandID);
    }
    public void MoveStudentToIslandRequest(List<Island> islands, Student stud_to_island){
        IslandCLI.MoveStudentToIslandRequest(client,islands,stud_to_island);
    }
    public void displayColourRequest(){
        ColourCLI.chooseColourRequest(client);
    }
    public void displayStudentToMoveRequest(Board board){
        BoardCLI.studentToMoveRequest(client,board);
    }
    public void displayWhereToMoveStudents(Board board, int choiceStudent){
        BoardCLI.positionToMoveRequest(client,board,choiceStudent);
    }
    public void displayDiningRoomColourFull( Board board,int choiceStudent){
        BoardCLI.displayDiningRoomColourFull(board,choiceStudent);
    }
    public void displayStudentChosenPreviously( Board board,int choiceStudent){
        BoardCLI.displayStudentChosenPreviously(board,choiceStudent);
    }
    public void displayEntranceStudents(Board board){
        BoardCLI.displayEntranceStudents(board);
    }

    public void displayTimeoutCloseConnection(){
        System.out.println("Timeout expired, you will be now disconnected");
        closeConnection();
    }
    public void displayChooseAssistantCardRequest( Player player,GameTable GT){
        AssistantCLI.chooseAssistantCard(client,player,GT);

    }
    public void choosePositionRequest(int upperLimit){
        PositionCLI.choosePositionRequest(client,upperLimit);
    }
    public void displayChooseCloudRequest( List<Cloud> clouds,Player player){
        CloudCLI.chooseCloud(client,clouds,player);
    }
    public void closeConnection(){
        System.out.close();
        client.closeSocket();
    }
    public void handleCloseConnection(boolean wasConnected) {
        displayUnreachableServer(wasConnected);
        if (inputObserverOutOfTurn != null && inputObserverOutOfTurn.isAlive())
            inputObserverOutOfTurn.interrupt();
    }
    private void displayUnreachableServer(boolean wasConnected){
        System.out.println((wasConnected ? "The server is not reachable anymore" : "The server is unreachable at the moment") + ". Try again later.");
    }

//called when the condition for the endgame are met,calculate and send the result to each player
    public void displayResults( List<Island> islands, List<Player> players,Board[] boards) {
        EndGameCLI.displayResults(client,islands,players,boards);
    }






}
