package it.polimi.ingsw.CLI;

public class CLI {

    private Client client;
    private Thread OutOfTurnMessages;

    public static void main(String[] args) {
        CLI cli= new CLI();
        cli.setUpGame();
    }

    private void setUpGame(){
        logo.print();
    }






}
