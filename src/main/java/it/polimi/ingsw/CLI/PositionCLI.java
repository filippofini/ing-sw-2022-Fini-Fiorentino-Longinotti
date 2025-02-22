package it.polimi.ingsw.CLI;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.toServer.PositionReply;

public class PositionCLI {

    public static void choosePositionRequest(Client client, int upperLimit) {
        int choice;
        System.out.println("Choose the Position");
        System.out.println("Allowed range: 0 (inclusive) to " + upperLimit + " (exclusive).");
        System.out.println("Enter your choice: ");
        choice = InputParser.getInt();

        while (choice < 0 || choice >= upperLimit) {
            System.out.println("Number not allowed, please choose another number.");
            System.out.println("Enter your choice: ");
            choice = InputParser.getInt();
        }
        client.sendMessageToServer(new PositionReply(choice));
    }
}