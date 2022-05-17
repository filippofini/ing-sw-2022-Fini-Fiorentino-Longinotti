package it.polimi.ingsw.CLI;

import java.io.IOException;

public class StartGame {
    private static final String DEFAULT_ADDRESS = "127.0.0.1";
    private static final int DEFAULT_PORT = 1234;

    public static Client InitialConnection(CLI cli, boolean firstConnection){
        int port;
        String IPAddress;
        boolean firstTry = true;

        do{
            if (!firstConnection && firstTry){
                System.out.println(" IP address and port provided are not valid please try again.");
            }
            if(firstTry)
                System.out.println("Enter the server's IP address or d for the default configuration:");
            else if (!firstTry)
                System.out.println("Invalid IP address, insert another address or enter d for default configuration: ");
            firstTry = false;
            IPAddress = InputParser.getLine();
            if (IPAddress.equalsIgnoreCase("d")){
                IPAddress = DEFAULT_ADDRESS;
                port = DEFAULT_PORT;
                return new Client(IPAddress, port, cli);
            }
        }while(!Utils.IPAddressIsValid(IPAddress));
        firstTry = true;
        //Insert port number
        do{
            if(firstTry)
                System.out.println("Enter the port you want to connect to: ");
            else
                System.out.println("Invalid port number please enter an integer between 1024 and 65535");
            firstTry = false;
            port = InputParser.getInt("Invalid port number please enter an integer between 1024 and 65535", CLI.conditionOnIntegerRange(1024, 65535));
        }while (!Utils.portIsValid(port));
        return new Client(IPAddress, port, cli);
    }

    public static void displayNicknameRequest(Client client, boolean retry, boolean alreadyTaken) {
        if (client.isNicknameValid() && client.getNickname().isPresent()){
            client.sendMessageToServer(new NicknameResponse(client.getNickname().get()));
            return;
        }

        if (!retry)
            System.out.println("Insert your nickname");
        else if (!alreadyTaken)
            System.out.println("Your nickname was invalid, be sure to insert only valid characters (A-Z, a-z, 0-9)");
        else {
            System.out.println("Your nickname has already been taken, insert another one");
        }
        String selection = InputParser.getLine();
        if (selection == null)
            return;
        client.setNickname(selection);
        client.sendMessageToServer(new NicknameResponse(selection));
    }

    public static void displayGameModeRequest(Client client) {
        if (client.getGameMode().isPresent()){
            client.sendMessageToServer(new GameModeResponse(client.getGameMode().get()));
            return;
        }
        System.out.println("\nConnection established!");
        System.out.println("Please choose the game mode, standard[s] or expert[e]:");
        GameMode gameMode = null;
        try {
            gameMode = getGameMode(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (gameMode == null)
            return;
        client.setGameMode(gameMode);
        client.sendMessageToServer(new GameModeResponse(gameMode));
    }


}
