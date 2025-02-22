package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.toServer.ResultNotifyReply;
import java.util.List;

/**
 * This class represents the CLI for the endgame.
 */
public class EndGameCLI {

    /**
     * This method displays the results of the game.
     * @param client The client.
     * @param islands The list of islands.
     * @param players The list of players.
     * @param boards The array of boards.
     */
    public static void displayResults(Client client, List<Island> islands, List<Player> players, Board[] boards) {
        if (players == null || islands == null || boards == null || client == null) {
            System.err.println("Invalid input data. Cannot display results.");
            return;
        }

        int winnerID = -1;
        int maxTowers = 0;

        // Determine the winner based on the number of towers
        for (int i = 0; i < players.size(); i++) {
            int playerTowers = calculatePlayerTowers(islands, i);

            if (playerTowers > maxTowers) {
                winnerID = i;
                maxTowers = playerTowers;
            } else if (playerTowers == maxTowers && winnerID != -1) {
                if (countProfessors(boards[i]) > countProfessors(boards[winnerID])) {
                    winnerID = i;
                }
            }
        }

        // Display the winner(s)
        displayWinner(players, winnerID, maxTowers);

        // Display each player's towers and professors
        for (int i = 0; i < players.size(); i++) {
            int playerTowers = calculatePlayerTowers(islands, i);
            int playerProfessors = countProfessors(boards[i]);

            System.out.println(players.get(i).getNickname() + ":\n");
            System.out.println("Towers: " + playerTowers + "\n");
            System.out.println("Professors: " + playerProfessors + "\n\n");
        }

        // Notify the server and close the client socket
        client.sendMessageToServer(new ResultNotifyReply());
        client.closeSocket();
    }

    private static int calculatePlayerTowers(List<Island> islands, int playerID) {
        int towers = 0;
        for (Island island : islands) {
            if (island.getPlayerController() == playerID) {
                towers += island.getTower();
            }
        }
        return towers;
    }

    private static int countProfessors(Board board) {
        int count = 0;
        for (boolean hasProfessor : board.getArrProfessors()) {
            if (hasProfessor) {
                count++;
            }
        }
        return count;
    }

    private static void displayWinner(List<Player> players, int winnerID, int maxTowers) {
        if (players.size() == 2 || players.size() == 3) {
            System.out.println("\nThe winner is: " + players.get(winnerID).getNickname() + " with " + maxTowers + " towers placed!\n");
        } else if (players.size() == 4) {
            if (winnerID == 0 || winnerID == 1) {
                System.out.println("The winners are: " + players.get(0).getNickname() + " and " + players.get(1).getNickname() + " with " + maxTowers + " towers!\n");
            } else if (winnerID == 2 || winnerID == 3) {
                System.out.println("The winners are: " + players.get(2).getNickname() + " and " + players.get(3).getNickname() + " with " + maxTowers + " towers!\n");
            }
        }
    }
}