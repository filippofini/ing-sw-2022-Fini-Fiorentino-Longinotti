package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.toServer.*;
import it.polimi.ingsw.view.View;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * This class represents the CLI main.
 */
public class CLI implements View {

    private Client client;
    private Thread inputObserverOutOfTurn;

    // ANSI escape codes for colors and formatting
    private static final String RESET = "\u001B[0m";
    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";
    private static final String BOLD = "\u001B[1m";
    private static final String UNDERLINE = "\u001B[4m";

    public static void main(String[] args) {
        CLI cli = new CLI();
        cli.setUpGame();
    }

    /**
     * This method sets up a game.
     */
    private void setUpGame() {
        boolean error = true;
        boolean firstTry = true;
        Logo.print();
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

    /**
     * This method display a message.
     * @param message The message.
     */
    public void displayMessage(String message) {
        System.out.println(GREEN + message + RESET);
    }

    /**
     * This method display a nickname request.
     * @param retry {@code True} if it's not the first time that the nickname is asked.
     */
    public void displayNicknameRequest(boolean retry) {
        StartGame.displayNicknameRequest(client, retry);
    }

    /**
     * This method returns the condition on an integer range.
     * @param min The min.
     * @param max The max.
     * @return The condition on an integer range.
     */
    public static Predicate<Integer> conditionOnIntegerRange(int min, int max) {
        return p -> p >= min && p <= max;
    }

    /**
     * This method displays the game mode requests.
     */
    public void displayGameModeRequest() {
        StartGame.displayGameModeRequest(client);
    }

    /**
     * This method displays the number of players requests.
     */
    public void displayNumberOfPlayersRequest() {
        StartGame.NumberOfPlayersRequest(client);
    }

    /**
     * This method displays the waiting message.
     */
    public void displayWaitingMessage() {
        StartGame.displayWaitingMessage(client);
    }

    /**
     * This method displays the movement of mother nature requests.
     * @param Mn_pos The position of mother nature.
     * @param Current_Player The current player.
     */
    public void motherNatureMovementRequest(int Mn_pos, Player Current_Player) {
        MotherNatureCLI.motherNatureMovementRequest(
                client,
                Mn_pos,
                Current_Player
        );
    }

    /**
     * This method displays the island info.
     * @param island The island.
     * @param islandID The ID of the island.
     */
    public void displayIslandInfo(Island island, int islandID) {
        IslandCLI.displayIslandInfo(client, island, islandID);
    }

    /**
     * This method displays move student to island requests.
     * @param islands The list of islands.
     * @param stud_to_island The student to the island.
     */
    public void MoveStudentToIslandRequest(
            List<Island> islands,
            Student stud_to_island
    ) {
        IslandCLI.MoveStudentToIslandRequest(client, islands, stud_to_island);
    }

    /**
     * This method displays the colour requests.
     */
    public void displayColourRequest() {
        ColourCLI.chooseColourRequest(client);
    }

    /**
     * This method displays the students to move requests.
     * @param board The board.
     */
    public void displayStudentToMoveRequest(Board board) {
        BoardCLI.studentToMoveRequest(client, board);
    }

    /**
     * This method displays the number of players requests.
     * @param board The board.
     * @param choiceStudent The student.
     */
    public void displayWhereToMoveStudents(Board board, int choiceStudent) {
        BoardCLI.positionToMoveRequest(client, board, choiceStudent);
    }

    /**
     * This method displays the dining room with colours.
     * @param board The board.
     * @param choiceStudent The student.
     */
    public void displayDiningRoomColourFull(Board board, int choiceStudent) {
        BoardCLI.displayDiningRoomColourFull(client, board, choiceStudent);
    }

    /**
     * This method displays the student chosen previously.
     * @param board The board.
     * @param choiceStudent The student.
     */
    public void displayStudentChosenPreviously(Board board, int choiceStudent) {
        BoardCLI.displayStudentChosenPreviously(client);
    }

    /**
     * This method displays the entrance of students.
     * @param board The board.
     */
    public void displayEntranceStudents(Board board) {
        BoardCLI.displayEntranceStudents(board);
    }

    /**
     * This method notifies that the time to send a response is over.
     */
    public void displayTimeoutCloseConnection() {
        System.out.println(RED + "Timeout expired, you will be now disconnected" + RESET);
        closeConnection();
        client.sendMessageToServer(new TimeoutExpiredReply());
    }

    /**
     * This method displays the request of the choice for the assistant card.
     * @param player The player.
     * @param GT The game table.
     */
    public void displayChooseAssistantCardRequest(Player player, GameTable GT) {
        AssistantCLI.chooseAssistantCard(client, player, GT);
    }

    /**
     * This method request a position.
     * @param upperLimit The max moves available.
     */
    public void choosePositionRequest(int upperLimit) {
        PositionCLI.choosePositionRequest(client, upperLimit);
    }

    /**
     * This method displays the request of the choice for a cloud.
     * @param clouds The list of clouds.
     */
    public void displayChooseCloudRequest(List<Cloud> clouds) {
        CloudCLI.chooseCloud(client, clouds);
    }

    /**
     * This method close the connection.
     */
    public void closeConnection() {
        client.closeSocket();
    }

    /**
     * This method handle the closing of connections.
     * @param wasConnected Boolean to check if there was a connection.
     */
    public void handleCloseConnection(boolean wasConnected) {
        displayUnreachableServer();
        if (
                inputObserverOutOfTurn != null && inputObserverOutOfTurn.isAlive()
        ) inputObserverOutOfTurn.interrupt();
    }

    /**
     * This method displays the message that the server is unreachable.
     */
    private void displayUnreachableServer() {
        System.out.println(RED + "Game has ended\n\n" + RESET);
    }

    /**
     * This method display a player disconnection message.
     * @param name The name.
     */
    public void displayDisconnection(String name) {
        System.out.println(RED + name + " has quit the game.\n" + RESET);
        client.closeSocket();
    }

    //called when the condition for the endgame are met,calculate and send the result to each player

    /**
     * This method shows the results of the game.
     * @param islands The list pf islands.
     * @param players The list of players.
     * @param boards The array of boards.
     */
    public void displayResults(
            List<Island> islands,
            List<Player> players,
            Board[] boards
    ) {
        EndGameCLI.displayResults(client, islands, players, boards);
    }

    /**
     * This method ask if the player wants to use a character card.
     */
    public void UseCharacterCard() {
        int choice;
        System.out.println(
                CYAN + "Do you want to use a character card? no[0] or yes[1]\n" + RESET
        );
        choice = InputParser.getInt();

        while (choice < 0 || choice > 1) {
            System.out.println(
                    RED + "Number not allowed, please choose another number" + RESET
            );
            choice = InputParser.getInt();
        }

        client.sendMessageToServer(new UseCharacterCardReply(choice));
    }

    /**
     * This method displays the request the Character card that the player wants to use.
     * @param player The player.
     * @param cc The array of character cards.
     */
    public void ChooseCharacterCard(Player player, CharacterCard[] cc) {
        int choice;
        Scanner sc = new Scanner(System.in);
        boolean poor = true;
        System.out.println(
                CYAN + "Choose a character card from the ones below: \n" + RESET
        );
        for (int i = 0; i < 3; i++) {
            System.out.println(BOLD + "Character Card [" + i + "]" + RESET);
            System.out.println(BLUE + "   ID: " + RESET + cc[i].getIDCode());
            System.out.println(BLUE + "   Name: " + RESET + cc[i].getName());
            System.out.println(BLUE + "   Cost: " + RESET + cc[i].getCost());
            System.out.println("--------------------------------------------------");
            if (player.getCoin() >= cc[i].getCost()) {
                poor = false;
            }
        }
        System.out.println(BLUE + "   Player Coins: " + RESET + player.getCoin());
        if (!poor) {
            System.out.print(CYAN + "\nEnter the number of the character card you want to choose: " + RESET);
            choice = sc.nextInt();

            while ((choice < 0 || choice > 2) || player.getCoin() < cc[choice].getCost()) {
                System.out.println(
                        RED + "Number not allowed, please choose another number\n" + RESET
                );
                choice = sc.nextInt();
            }
            client.sendMessageToServer(new ChooseCharacterCardReply(choice));
        } else {
            System.out.println(
                    RED + "\nIt seems you have not enough coins..\n" + RESET
            );
            client.sendMessageToServer(new NotenoughCoinReply());
        }
    }

    /**
     * This method shows the students on a character card.
     * @param students The students.
     */
    public void showStudent(Student[] students) {
        AssistantCLI.showStudent(client, students);
    }

    /**
     * This method shows the islands to choose.
     * @param islands The islands to choose.
     */
    public void heraldIsland(List<Island> islands) {
        AssistantCLI.heraldIsland(client, islands);
    }
}