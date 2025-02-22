package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.toServer.GameModeReply;
import it.polimi.ingsw.network.message.toServer.NameReply;
import it.polimi.ingsw.network.message.toServer.NumberOfPlayersReply;
import it.polimi.ingsw.network.message.toServer.WaitingInTheLobbyReply;
import java.io.IOException;

/**
 * This class represents the start of the game.
 */
public class StartGame {

    private static final String DEFAULT_ADDRESS = "127.0.0.1";
    private static final int DEFAULT_PORT = 1250;

    private static final String RESET = "\033[0m";
    private static final String RED = "\033[0;31m";
    private static final String GREEN = "\033[0;32m";
    private static final String YELLOW = "\033[0;33m";
    private static final String CYAN = "\033[0;36m";

    /**
     * This method handles the starting connection.
     * @param cli The CLI main.
     * @param firstConnection {@code False} if it's the first connection.
     * @return The client.
     */
    public static Client InitialConnection(CLI cli, boolean firstConnection) {
        int port;
        String IPAddress;
        boolean firstTry = true;

        do {
            if (!firstConnection && firstTry) {
                System.out.println(RED + "IP address and port provided are not valid, please try again." + RESET);
            }
            if (firstTry) {
                System.out.println(CYAN + "Enter the server's IP address or 'd' for the default configuration:" + RESET);
            } else {
                System.out.println(RED + "Invalid IP address, insert another address or enter 'd' for default configuration: " + RESET);
            }
            firstTry = false;
            IPAddress = InputParser.getLine();
            if (IPAddress.equalsIgnoreCase("d")) {
                IPAddress = DEFAULT_ADDRESS;
                port = DEFAULT_PORT;
                return new Client(IPAddress, port, cli);
            }
        } while (!Utils.IPAddressIsValid(IPAddress));

        firstTry = true;
        // Insert port number
        do {
            if (firstTry) {
                System.out.println(CYAN + "Enter the port you want to connect to: " + RESET);
            } else {
                System.out.println(RED + "Invalid port number, please enter an integer between 1024 and 65535" + RESET);
            }
            firstTry = false;
            port = InputParser.getInt();
        } while (!Utils.portIsValid(port));

        return new Client(IPAddress, port, cli);
    }

    /**
     * This method displays the nickname request.
     * @param client The client.
     * @param retry {@code False} if it's the first request of nickname, {@code True} if not.
     */
    public static void displayNicknameRequest(Client client, boolean retry) {
        boolean check = false;
        if (!retry) {
            System.out.println(CYAN + "Insert your nickname:" + RESET);
        } else {
            System.out.println(RED + "Your nickname has already been taken, insert another one:" + RESET);
        }
        String selection = InputParser.getLine();
        while (!check) {
            if (selection == null) {
                System.out.println(RED + "Your nickname is invalid, please insert another one:" + RESET);
                selection = InputParser.getLine();
            } else {
                check = true;
                client.setName(selection);
                client.sendMessageToServer(new NameReply(selection));
            }
        }
    }

    /**
     * This method displays the game mode request.
     * @param client The client.
     */
    public static void displayGameModeRequest(Client client) {
        if (client.getGameMode().isPresent()) {
            client.sendMessageToServer(new GameModeReply(client.getGameMode().get()));
            return;
        }
        System.out.println(GREEN + "\nConnection established!\n" + RESET);
        System.out.println(CYAN + "Please choose the game mode, standard[s] or expert[e]:" + RESET);
        GameMode gameMode = null;
        try {
            gameMode = getGameMode();
        } catch (IOException e) {
            System.out.println(RED + "An error occurred" + RESET);
        }
        if (gameMode == null) return;
        client.setGameMode(gameMode);
        client.sendMessageToServer(new GameModeReply(gameMode));
    }

    /**
     * This method gets the game mode.
     * @return The game mode.
     */
    private static GameMode getGameMode() throws IOException {
        while (true) {
            String gameModeString = InputParser.getLine();
            if (gameModeString == null) throw new IOException();
            if (gameModeString.equals("s")) return GameMode.STANDARD;
            else if (gameModeString.equals("e")) return GameMode.EXPERT;
            else {
                System.out.println(RED + "Invalid game mode, type [s] for standard mode or [e] for expert mode" + RESET);
            }
        }
    }

    /**
     * This method requests the number of players in the game.
     * @param client The client.
     */
    public static void NumberOfPlayersRequest(Client client) {
        System.out.println(CYAN + "Insert the number of players [2] , [3] " + RESET);
        Integer choice = InputParser.getInt(
                RED + "Invalid number of players: please insert an integer number between 2 and 3" + RESET,
                CLI.conditionOnIntegerRange(2, 3)
        );
        if (choice != null) client.sendMessageToServer(new NumberOfPlayersReply(choice));
    }

    /**
     * This method displays the waiting message.
     */
    public static void displayWaitingMessage(Client client) {
        System.out.println(YELLOW + "\nWaiting for other players...\n" + RESET);
        client.sendMessageToServer(new WaitingInTheLobbyReply());
    }
}