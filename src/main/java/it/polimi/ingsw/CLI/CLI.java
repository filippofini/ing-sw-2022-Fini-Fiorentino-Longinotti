package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.List;

public class CLI implements View {

    private Client client;

    public static void main(String[] args) {
        CLI cli= new CLI();
        cli.setUpGame();
    }

    private void setUpGame(){
        boolean error = true;
        boolean firstTry = true;
        logo.print();
        MatchData.getInstance().setView(this);
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



    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }


    public void displayNicknameRequest(boolean retry, boolean alreadyTaken) {

        StartGame.displayNicknameRequest(client,retry,alreadyTaken);
    }

    public void displayGameModeRequest() {

        StartGame.displayGameModeRequest(client);
    }
    public void NumberOfPlayerRequest(){
        StartGame.NumberOfPlayersRequest(client);
    }
    public void WaitingMessage(){
        StartGame.WaitingMessage();
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
    public void chooseColourRequest(){
        ColourCLI.chooseColourRequest(client);
    }
    public void studentToMoveRequest(Board board){
        BoardCLI.studentToMoveRequest(client,board);
    }
    public void positionToMoveRequest(Board board, int choiceStudent){
        BoardCLI.positionToMoveRequest(client,board,choiceStudent);
    }
    public void displayDiningRoomColourFull( Board board,int choiceStudent){
        BoardCLI.displayDiningRoomColourFull(board,choiceStudent);
    }
    public void displayStudentChosenPreviously( Board board,int choiceStudent){
        BoardCLI.displayStudentChosenPreviously(board,choiceStudent);
    }
    public void displayEntranceStudents(Board board){
        BoardCLI.displayEntranceStudents();
    }

    public void displayTimeoutCloseConnection(){
        System.out.println("Timeout expired, you will be now disconnected");
        closeConnection();
    }
    public void closeConnection(){
        System.out.close();
        client.closeSocket();
    }

//called when the condition for the endgame are met,calculate and send the result to each player
    public void displayResults( List<Island> islands, List<Player> players,Board[] boards) {
        EndGameCLI.displayResults(client,islands,players,boards);
    }






}
