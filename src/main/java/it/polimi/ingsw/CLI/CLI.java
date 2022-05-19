package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.View;

import java.io.IOException;

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

    public void TimeoutCloseConnectionMessage(){
        System.out.println("Timeout expired, you will be now disconnected");
        System.out.close();
        client.closeSocket();
    }

    //TODO
    @Override
    public void displayResults() {

    }






}
