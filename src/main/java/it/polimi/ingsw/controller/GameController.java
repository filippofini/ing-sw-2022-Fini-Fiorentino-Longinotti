package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TowerColour;
import it.polimi.ingsw.network.message.toClient.*;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.Server;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

/**
 * This class is used to manage game phases.
 */
public class GameController implements Serializable {

    private final GameMode gameMode;
    private List<Player> players;
    private String[] names;
    private final List<ClientHandler> clientHandlers;
    private Server server;
    private ReentrantLock lockConnections = new ReentrantLock(true);
    private final int[] wizards = { 0, 1, 2, 3 };

    /**
     * Constructor of the class.
     * @param gameMode The game mode.
     */
    public GameController(GameMode gameMode) {
        this.gameMode = gameMode;
        this.players = new LinkedList<>();
        this.clientHandlers = new LinkedList<>();
    }

    /**
     * This method handles a client's disconnection.
     * @param nickname The nickname of the disconnected client.
     */
    public synchronized void handleClientDisconnection(String nickname) {
        ClientHandler connection = getConnectionByNickname(nickname);
        forceEndMultiplayerGame(nickname);
        server.removeConnectionGame(connection);
    }

    /**
     * This method is used force the end of the game.
     * @param nickname The nickname of the disconnected client.
     */
    private void forceEndMultiplayerGame(String nickname) {
        for (Player player : players) {
            //For each player that is still active I notify the end of the game
            getConnectionByNickname(player.getNickname()).sendMessageToClient(
                    new NotifyDisconnection(nickname)
            );
            getConnectionByNickname(player.getNickname()).setGameStarted(false);
            getConnectionByNickname(player.getNickname()).setGameController(
                    null
            );
        }
    }

    /**
     * This method returns the list of players.
     * @return The list of players.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * This method is used to start a game.
     */
    public void start() throws IOException {
        startNewGame();
    }

    /**
     * This method starts a new game.
     */
    private void startNewGame() throws IOException {
        Server.SERVER_LOGGER.log(
                Level.INFO,
                "Creating a new " +
                        gameMode.name().replace("_", " ") +
                        ", players: " +
                        clientHandlers.stream().map(ClientHandler::getNickname).toList()
        );
        players = addPlayer(clientHandlers);
        TurnController turnController = new TurnController(
                clientHandlers.size(),
                getArrayNickname(clientHandlers),
                wizards,
                isExpertMode(gameMode),
                players,
                clientHandlers
        );

        while (!turnController.getEndgame()) {
            turnController.planningPhaseGeneral();
            turnController.actionPhase();
        }
        endGame(turnController);
    }

    /**
     * This method adds a connection to the client handlers' list.
     * @param connection ClientHandler of the connection to add.
     */
    public void addConnection(ClientHandler connection) {
        lockConnections.lock();
        try {
            this.clientHandlers.add(connection);
        } finally {
            lockConnections.unlock();
        }
    }

    /**
     * This method removes a connection from the client handlers' list.
     * Used in case of disconnection of a client.
     * @param connection ClientHandler of the connection to remove.
     */
    public void removeConnection(ClientHandler connection) {
        lockConnections.lock();
        try {
            this.clientHandlers.remove(connection);
        } finally {
            lockConnections.unlock();
        }
    }

    /**
     * This method adds a player to the list, given the list of client handler.
     * @param clientHandlers The list of client handlers.
     * @return The updated list of players.
     */
    public List<Player> addPlayer(List<ClientHandler> clientHandlers) {
        // Define the valid tower colours based on the number of players
        TowerColour[] towerColours;
        int numPlayers = clientHandlers.size();

        towerColours = switch (numPlayers) {
            case 2 -> new TowerColour[]{TowerColour.BLACK, TowerColour.WHITE};
            case 3 -> new TowerColour[]{TowerColour.BLACK, TowerColour.WHITE, TowerColour.GREY};
            default -> throw new IllegalArgumentException("Unsupported number of players: " + numPlayers);
        };

        // Create the players list from the client handlers and the tower colours
        List<Player> updatedPlayers = new LinkedList<>();
        for (int i = 0; i < numPlayers; i++) {
            String nickname = clientHandlers.get(i).getNickname();
            // Create a new player with the given nickname, index, tower colour, and id (using index again)
            Player p = new Player(nickname, i, towerColours[i], i);
            updatedPlayers.add(p);
        }

        this.players = updatedPlayers;
        return updatedPlayers;
    }

    /**
     * This method returns the array of string containing the nicknames.
     * @param clientHandlers The list of client handlers.
     * @return The array of string containing the nicknames.
     */
    public String[] getArrayNickname(List<ClientHandler> clientHandlers) {
        names = new String[clientHandlers.size()];
        for (int i = 0; i < clientHandlers.size(); i++) {
            names[i] = clientHandlers.get(i).getNickname();
        }
        return names;
    }

    /**
     * This method returns a client handler, given a nickname.
     * @param nickname The nickname of the client handler wanted.
     * @return The corresponding client handler.
     */
    public ClientHandler getConnectionByNickname(String nickname) {
        lockConnections.lock();
        try {
            for (ClientHandler clientHandler : clientHandlers) {
                if (clientHandler.getNickname().equals(nickname)) {
                    return clientHandler;
                }
            }
        } finally {
            lockConnections.unlock();
        }
        return null;
    }

    /**
     * This method is used to start the end game. It gives the results to the players.
     */
    public void endGame(TurnController turnController) throws IOException {
        getServer()
                .gameEnded(
                        this,
                        new ResultsNotify(
                                turnController.getGameState().getGameTable().getIslands(),
                                players,
                                turnController.getGameState().getGameTable().getBoards()
                        )
                );
    }

    /**
     * This method is used to send the same message to all the clients connected.
     * @param message The message to be sent.
     */
    public void sendMessageToAll(MessagesToClient message) {
        lockConnections.lock();
        try {
            for (ClientHandler clientHandler : clientHandlers) clientHandler.sendMessageToClient(
                    message
            );
        } finally {
            lockConnections.unlock();
        }
    }

    /**
     * This method returns the game mode.
     * @param gameMode The game mode.
     * @return {@code True} if the game mode is expert, {@code False} if it's standard.
     */
    public boolean isExpertMode(GameMode gameMode) {
        return gameMode != GameMode.STANDARD;
    }

    /**
     * This method sets the server.
     * @param server The server.
     */
    public void setServer(Server server) {
        this.server = server;
    }

    /**
     * This method returns the server.
     * @return The server.
     */
    public Server getServer() {
        return server;
    }
}