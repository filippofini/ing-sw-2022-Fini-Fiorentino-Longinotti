package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.ClientHandlerPhase;
import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.message.toClient.MessagesToClient;
import it.polimi.ingsw.network.message.toClient.NotifyDisconnection;
import it.polimi.ingsw.network.message.toClient.ResultsNotify;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.Server;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * This class is used to manage game phases.
 */
public class GameController implements Serializable {

    private static final long serialVersionUID = 4405183481677036856L;
    private GameMode gameMode;
    private List<Player> players;
    private List<ClientHandler> clientHandlers;
    private int n_player;
    public static final String SERVER_NICKNAME = "server";
    private Server server;
    private ReentrantLock lockConnections = new ReentrantLock(true);

    //per gestire le partite multiple creare la classe Game o usare gamestate?

    public GameController(GameMode gameMode){
        this.gameMode = gameMode;
        this.players = new LinkedList<Player>() ;
        this.clientHandlers = new LinkedList<>();
    }

    /**
     * Method to handle a client's disconnection.
     * @param nickname the nickname the disconnected client.
     */
    public synchronized void handleClientDisconnection(String nickname) {
        ClientHandler connection = getConnectionByNickname(nickname);
        clientHandlers.remove(connection);
        forceEndMultiplayerGame(nickname);
        server.removeConnectionGame(connection);

    }


    /**
     * Method to force the end of a game.
     * @param nickname the nickname of the disconnected client.
     */
    private void forceEndMultiplayerGame(String nickname){
        for (Player player : getPlayers_ID()) {
            //For each player that is still active I notify the end of the game and I reinsert him in the lobby room
            if (player.isActive()) {
                getConnectionByNickname(player.getNickname()).sendMessageToClient(new NotifyDisconnection(nickname));
                getConnectionByNickname(player.getNickname()).setGameStarted(false);
                getConnectionByNickname(player.getNickname()).setGameController(null);
                getConnectionByNickname(player.getNickname()).setClientHandlerPhase(ClientHandlerPhase.WAITING_IN_THE_LOBBY);
                server.addClientHandler(getConnectionByNickname(player.getNickname()));
            } else {
                server.removeNickname(player.getNickname());
            }
        }

    }

    public List<Player> getPlayers_ID() {
        return players;
    }


    public void start(){
        startNewGame();
    }
    private void startNewGame() {
        Server.SERVER_LOGGER.log(Level.INFO, "Creating a new " + gameMode.name().replace("_"," ") + ", players: " + clientHandlers.stream().map(ClientHandler::getNickname).collect(Collectors.toList()));
        //this.setGame(new TurnController());  TODO:CAPIRE COME LANCIARE LA PRIMA FASE DEL GIOCO CHE SI TROVA IN TURN CONTROLLER!!
    }

    /**
     * Method to add a connection to the client handlers' list
     * @param connection ClientHandler of the connection to add
     */
    public void addConnection(ClientHandler connection) {
        lockConnections.lock();
        try {
            this.clientHandlers.add(connection);
        }
        finally {
            lockConnections.unlock();
        }
    }

    /**
     * Method to remove a connection from the client handlers' list. Used in case of disconnection of a client.
     * @param connection ClientHandler the connection to remove
     */
    public void removeConnection(ClientHandler connection){
        lockConnections.lock();
        try {
            this.clientHandlers.remove(connection);
        } finally {
            lockConnections.unlock();
        }
    }


    public ClientHandler getConnectionByNickname(String nickname){
        lockConnections.lock();
        try{
            for (ClientHandler clientHandler : clientHandlers){
                if (clientHandler.getNickname().equals(nickname)){
                    return clientHandler;
                }
            }
        } finally {
            lockConnections.unlock();
        }
        return null;
    }


    public void endGame() {
        getServer().gameEnded(this,new ResultsNotify());

    }

    /**
     * Method to send the same message to all the clients connected
     * @param message to be sent
     */
    public void sendMessageToAll(MessagesToClient message){
        lockConnections.lock();
        try{
            for (ClientHandler clientHandler : clientHandlers)
                clientHandler.sendMessageToClient(message);
        } finally {
            lockConnections.unlock();
        }
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }
}
