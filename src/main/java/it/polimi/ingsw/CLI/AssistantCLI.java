package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.toServer.*;
import java.util.List;

/**
 * This class represents the choice for the assistant in the CLI.
 */
public class AssistantCLI {

    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";

    private static final String GREEN = "\u001B[32m";

    /**
     * Constructor of the class.
     * @param client The client.
     * @param player The current player.
     * @param GT The game table.
     */
    public static void chooseAssistantCard(Client client, Player player, GameTable GT) {
        int choice;
        Deck deck = player.getDeck();
        System.out.println("========================================");
        System.out.println("Choose the number of an assistant card:");
        System.out.println("========================================");
        for (int i = 0; i < deck.countElements(); i++) {
            System.out.println("[" + i + "] " + deck.getCards().get(i));
        }
        System.out.println("========================================");

        choice = InputParser.getInt();
        while (choice < 0 || choice >= deck.countElements() || !GT.checkIfPlayable(deck.getCards().get(choice), deck)) {
            System.out.println("Number not valid, please choose a number from the list:");
            choice = InputParser.getInt();
        }

        client.sendMessageToServer(new ChooseAssistantCardReply(choice));
        System.out.println( GREEN + "\nMother Nature start position: island[" + GT.getMotherNaturePos() + "]\n" + RESET);
        System.out.println(YELLOW + "\nWaiting for other players...\n" + RESET);
    }

    /**
     * This method shows the students on a character card.
     * @param client The client.
     * @param students The array of students on the card.
     */
    public static void showStudent(Client client, Student[] students) {
        int choice;
        System.out.println("========================================");
        System.out.println("Choose the number of a student for the character card:");
        System.out.println("========================================");
        for (int i = 0; i < students.length; i++) {
            System.out.println("[" + i + "] " + students[i].getEnumColour());
        }
        System.out.println("========================================");

        choice = InputParser.getInt();
        while (choice < 0 || choice >= students.length) {
            System.out.println("Number not valid, please choose a number from the list:");
            choice = InputParser.getInt();
        }

        client.sendMessageToServer(new ShowStudentsReply(students[choice].getColor()));
    }

    /**
     * This method sets the island where to move a student picked from the herald character card.
     * @param client The client.
     * @param islands The list of islands.
     */
    public static void heraldIsland(Client client, List<Island> islands) {
        System.out.println("========================================");
        System.out.println("Choose the island for the character card:");
        System.out.println("========================================");

        for (int i = 0; i < islands.size(); i++) {
            System.out.println("Island[" + i + "]:");
            for (int k = 0; k < DiskColor.values().length; k++) {
                System.out.println("  " + DiskColor.values()[k] + ": " + islands.get(i).getArrStudents()[k]);
            }
            System.out.println("----------------------------------------");
        }

        int choice = InputParser.getInt();
        while (choice < 0 || choice >= islands.size()) {
            System.out.println("This island does not exist, please choose a valid number:");
            choice = InputParser.getInt();
        }

        client.sendMessageToServer(new HeraldIslandReply(choice));
    }
}