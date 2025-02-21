package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.DiskColour;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.toServer.DisplayIslandInfoReply;
import it.polimi.ingsw.network.message.toServer.MoveStudentReply;
import java.util.List;

public class IslandCLI {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PINK = "\u001B[35m";

    private static String getColor(DiskColour color) {
        return switch (color) {
            case RED -> RED;
            case GREEN -> GREEN;
            case YELLOW -> YELLOW;
            case BLUE -> BLUE;
            case PINK -> PINK;
        };
    }

    private static void printIslandAsciiArt(Island island, int islandID) {
        System.out.println("\n");
        System.out.println("        ~~~~~~~~~~~~~~~~        ");
        System.out.println("      ~~~~~~~~~~~~~~~~~~~      ");
        System.out.println("    ~~~~~~~~~~  ~~~~~~~~~~    ");
        System.out.println("   ~~~~~~~~      ~~~~~~~~   ");
        System.out.println("  ~~~~~~~        ~~~~~~~  ");
        System.out.printf("    ||        ID:%-2d        ||  \n", islandID);
        System.out.println("    ||                    ||  ");
        System.out.println("    ||      STUDENTS      ||  ");

        for (DiskColour color : DiskColour.values()) {
            System.out.printf("    ||  %s%-7s%s : %-3d  ||\n", getColor(color), color, RESET, island.getArrStudents()[color.ordinal()]);
        }

        System.out.println("    ||                    ||  ");
        System.out.println("    ||--------------------||  ");
        System.out.printf("    || Influence  : %s  ||\n", island.getInfluenceController());
        System.out.printf("    || Towers    : %d  ||\n", island.getTower());
        System.out.printf("    || Player   : %s  ||\n", island.getControllerName());
        System.out.println("  ~~~~~~~        ~~~~~~~  ");
        System.out.println("   ~~~~~~~~      ~~~~~~~~   ");
        System.out.println("    ~~~~~~~~~~  ~~~~~~~~~~    ");
        System.out.println("      ~~~~~~~~~~~~~~~~~~~      ");
        System.out.println("        ~~~~~~~~~~~~~~~~        ");
    }

    public static void displayIslandInfo(Client client, Island island, int islandID) {
        printIslandAsciiArt(island, islandID);
        client.sendMessageToServer(new DisplayIslandInfoReply());
    }

    public static void MoveStudentToIslandRequest(Client client, List<Island> islands, Student studToIsland) {
        System.out.println("\n==================================================");
        System.out.println(" Choose an island to move the student: " + getColor(studToIsland.getEnumColour()) + studToIsland.getEnumColour() + RESET);
        System.out.println("==================================================\n");

        for (int i = 0; i < islands.size(); i++) {
            printIslandAsciiArt(islands.get(i), i);
        }

        System.out.print("Select island (0-" + (islands.size() - 1) + "): ");
        int choice = InputParser.getInt();

        while (choice < 0 || choice >= islands.size()) {
            System.out.println("\nInvalid choice. Please enter a valid island number:");
            choice = InputParser.getInt();
        }

        client.sendMessageToServer(new MoveStudentReply(choice));
    }
}