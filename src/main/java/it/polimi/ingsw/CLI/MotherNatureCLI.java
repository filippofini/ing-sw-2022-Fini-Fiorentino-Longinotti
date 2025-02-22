package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.toServer.MoveMnReply;

/**
 * This class represents the mother nature movement in the CLI.
 */
public class MotherNatureCLI {

    /**
     * This method requests the movement of mother nature.
     * @param client The client.
     * @param motherNaturePos The position of mother nature.
     * @param currentPlayer The current player.
     */
    public static void motherNatureMovementRequest(
            Client client,
            int motherNaturePos,
            Player currentPlayer
    ) {
        System.out.println("\n\n========================================");
        System.out.println("        Mother Nature Movement          ");
        System.out.println("========================================");
        System.out.println("Mother Nature is currently on Island [" + motherNaturePos + "]");
        System.out.println("You can move Mother Nature up to " + currentPlayer.getMoves() + " steps.");
        System.out.println("----------------------------------------");

        int choice = -1;
        while (choice < 1 || choice > currentPlayer.getMoves()) {
            System.out.print("Enter the number of steps (1 to " + currentPlayer.getMoves() + "): ");
            choice = InputParser.getInt();
            if (choice < 1 || choice > currentPlayer.getMoves()) {
                System.out.println("Invalid number of steps. Please try again.");
            }
        }

        System.out.println("========================================");
        System.out.println("You have chosen to move Mother Nature " + choice + " steps.");
        System.out.println("========================================");

        client.sendMessageToServer(new MoveMnReply(choice));
    }
}