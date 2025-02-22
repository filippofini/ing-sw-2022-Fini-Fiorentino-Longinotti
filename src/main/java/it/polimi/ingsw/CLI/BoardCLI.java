package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.toServer.*;

/**
 * This class represents the board in the CLI.
 */
public class BoardCLI {

    // ANSI escape codes for colors
    public static final String RESET = "\033[0m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";

    /**
     * Constructor of the class.
     * @param client The client.
     * @param board The board.
     */
    public static void studentToMoveRequest(Client client, Board board) {
        boolean validChoice = true;
        int choiceStudent;
        System.out.println(YELLOW + "Choose the number of one of the students to move:" + RESET);
        displayEntranceStudents(board);
        do {
            choiceStudent = InputParser.getInt();
            if (choiceStudent < board.getMaxEntranceStudents()) {
                validChoice = false;
            } else {
                System.out.println(RED + "Number not valid, please choose a number from the list" + RESET);
            }
        } while (validChoice);

        client.sendMessageToServer(new StudentToMoveReply(choiceStudent));
    }

    /**
     * This method requests the position to move the students.
     * @param client The client.
     * @param board The board.
     * @param choiceStudent The student chosen.
     */
    public static void positionToMoveRequest(Client client, Board board, int choiceStudent) {
        boolean validChoice = true;
        int choicePosition;

        if (!board.getArrEntranceStudents()[choiceStudent].isChosen()) {
            System.out.println(YELLOW + "Where do you want to move the student?" + RESET);
            System.out.println("Dining Room[0] - Island[1]");
            do {
                choicePosition = InputParser.getInt();
                if (choicePosition < 2) {
                    validChoice = false;
                } else {
                    System.out.println(RED + "Number not valid, please choose a valid option" + RESET);
                }
            } while (validChoice);
            client.sendMessageToServer(new PositionToMoveReply(choicePosition));
        }
    }

    /**
     * This method displays the dining room.
     * @param client The client.
     * @param board The board.
     * @param choiceStudent The students.
     */
    public static void displayDiningRoomColourFull(Client client, Board board, int choiceStudent) {
        System.out.println(RED + "Table of colour: " + board.getArrEntranceStudents()[choiceStudent].getEnumColour() + " is full, please choose another student" + RESET);
        client.sendMessageToServer(new DisplayDiningRoomColourFullReply());
    }

    /**
     * This method displays the students chosen previously.
     * @param client The client.
     */
    public static void displayStudentChosenPreviously(Client client) {
        System.out.println(RED + "Student chosen previously, please choose another student" + RESET);
        client.sendMessageToServer(new DisplayStudentChosenPreviouslyReply());
    }

    /**
     * This method displays the entrance of students.
     * @param board The board.
     */
    public static void displayEntranceStudents(Board board) {
        System.out.println(CYAN + "Entrance Students:" + RESET);
        for (int i = 0; i < board.getMaxEntranceStudents(); i++) {
            String color = getColor(board.getArrEntranceStudents()[i].getColor());
            System.out.println(color + board.getArrEntranceStudents()[i].getEnumColour() + "[" + i + "] " + RESET);
        }
    }

    /**
     * This method returns the ANSI color code for a given color.
     * @param color The color.
     * @return The ANSI color code.
     */
    private static String getColor(int color) {
        return switch (color) {
            case 0 -> YELLOW;
            case 1 -> RED;
            case 2 -> PURPLE;
            case 3 -> BLUE;
            case 4 -> GREEN;
            default -> RESET;
        };
    }
}