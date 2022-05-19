package it.polimi.ingsw.network.client;
import javafx.application.Application;

import it.polimi.ingsw.CLI.CLI;
import it.polimi.ingsw.GUI.GUI;


/**
 * This class is used to start the game client-side.
 * If the starting command has the argument "-cli", it starts the command line interface;
 * if it has nothing, it starts the graphical interface.
 */
public class Client_main {

    private final static String CLI_STARTER = "-cli";
    private final static String HELP = "-help";

    public static void main(String[] args) {
        if (args.length==0){
            Application.launch(GUI.class, args);
        } else if (args.length>1) {
            System.out.println("Too many arguments, insert " + HELP + " to see all the available graphical interface options.");
        } else {
            if (CLI_STARTER.equals(args[0])){
                CLI.main(args);
            } else if (HELP.equals(args[0])) {
                System.out.println("Insert " + CLI_STARTER + " to start the game in command line interface mode, otherwise insert nothing to start the GUI.");
            } else {
                System.out.println("Command not found, insert " + HELP + " to see the available graphical interface options.");
            }
        }
    }

}
