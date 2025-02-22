package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.DiskColor;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.toServer.ChooseCloudReply;

import java.util.List;

/**
 * This class represents the CLI for the clouds with an ASCII-art "cloud"
 * containing the actual student color counts inside, with ANSI color codes.
 */
public class CloudCLI {

    // ANSI color codes (these often work in Unix terminals; on Windows, ANSI support
    // may need to be enabled or configured in some shells/settings)
    private static final String ANSI_RED    = "\u001B[31m";
    private static final String ANSI_GREEN  = "\u001B[32m";
    private static final String ANSI_BLUE   = "\u001B[34m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_PINK   = "\u001B[35m";
    private static final String ANSI_RESET  = "\u001B[0m";

    /**
     * This method lets the player choose a cloud from which to take the students,
     * displaying each as a stylized ASCII cloud with color-coded student counts inside.
     *
     * @param client The client.
     * @param clouds The list of clouds.
     */
    public static void chooseCloud(Client client, List<Cloud> clouds) {
        System.out.println("\n=========================================");
        System.out.println("         Cloud Selection Phase");
        System.out.println("=========================================\n");

        for (int i = 0; i < clouds.size(); i++) {
            // Retrieve the student counts for each color
            int redCount    = clouds.get(i).getArrStudents()[DiskColor.RED.ordinal()];
            int greenCount  = clouds.get(i).getArrStudents()[DiskColor.GREEN.ordinal()];
            int blueCount   = clouds.get(i).getArrStudents()[DiskColor.BLUE.ordinal()];
            int yellowCount = clouds.get(i).getArrStudents()[DiskColor.YELLOW.ordinal()];
            int pinkCount   = clouds.get(i).getArrStudents()[DiskColor.PINK.ordinal()];

            // Print an ASCII-art cloud that literally "encloses" the color-coded counts
            System.out.println("          .--~~~~~~~--.            ");
            System.out.println("      .-~               ~-.        ");
            System.out.println("    ,~                     ~.      ");
            System.out.println("   (      ☁  Cloud [" + i + "]  ☁      )     ");
            System.out.printf ("  (   "
                            + ANSI_RED    + "RED"    + ANSI_RESET + ":%-2d  "
                            + ANSI_GREEN  + "GRN"    + ANSI_RESET + ":%-2d  "
                            + ANSI_BLUE   + "BLU"    + ANSI_RESET + ":%-2d   )\n",
                    redCount, greenCount, blueCount);
            System.out.printf("   (      "
                            + ANSI_YELLOW + "YEL"    + ANSI_RESET + ":%-2d  "
                            + ANSI_PINK   + "PNK"    + ANSI_RESET + ":%-2d      )\n",
                    yellowCount, pinkCount);
            System.out.println("    (                        )    ");
            System.out.println("     `~.                  .~'       ");
            System.out.println("        ~-.            .-~          ");
            System.out.println("          `--~~~~~~~--'           ");
            System.out.println();
        }

        // Prompt the user to choose a cloud index
        System.out.println("Please enter the index of the cloud you want to choose:");

        int choice = InputParser.getInt();
        while (choice < 0 || choice >= clouds.size()) {
            System.out.println("Invalid number. Please select a valid cloud index from above:");
            choice = InputParser.getInt();
        }

        System.out.println("\nYou chose cloud " + choice + ". Sending choice to the server...");
        client.sendMessageToServer(new ChooseCloudReply(choice));
        System.out.println("Choice sent! Please wait for the next phase.\n");
    }
}