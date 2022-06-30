package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.ClientHandlerPhase;
import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.model.GameTable;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TowerColour;
import it.polimi.ingsw.network.message.toClient.*;
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
    private int i=0;
    private int j =0;
    private List<Player> players;
    private String[] names;
    private List<ClientHandler> clientHandlers;
    private int player_ID=0;
    public static final String SERVER_NICKNAME = "server";
    private Server server;
    private ReentrantLock lockConnections = new ReentrantLock(true);

   // private String[] wizards = new String[] {"Nature Wizard","Sand Wizard","Air Wizard","Ice Wizard"};

    private int[] wizards = {0,1,2,3};
    private int wizard = 0;

    GameTable GameTable;
    private boolean check;

    public GameController(GameMode gameMode){
        this.gameMode = gameMode;
        this.players = new LinkedList<>() ;
        this.clientHandlers = new LinkedList<>();
        check = false;
    }

    /**
     * Method to handle a client's disconnection.
     * @param nickname the nickname the disconnected client.
     */
    public synchronized void handleClientDisconnection(String nickname) {
        ClientHandler connection = getConnectionByNickname(nickname);
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
        players = addPlayer(clientHandlers);
        TurnController turnController = new TurnController(clientHandlers.size(),getArrayNickname(clientHandlers),wizards,getBooleanGameMode(gameMode),players,clientHandlers);
        turnController.setGameController(this);
        /*for (int i = 0; i < clientHandlers.size(); i++) {
            clientHandlers.get(0).setClientHandlerPhase(ClientHandlerPhase.READY_TO_START);
            clientHandlers.get(0).setGameStarted(true);
            turnController.addConnection(clientHandlers.get(0));
            clientHandlers.remove(0);
        }*/
        while(turnController.getendgame()==false) {

            turnController.planning_phase_general();
            turnController.action_phase();

        }
        endGame();
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

    public List<Player> addPlayer(List<ClientHandler> clientHandlers) {
        TowerColour towerColour = null;
        for (int i=0; i<clientHandlers.size(); i++) {
            String nickname = clientHandlers.get(i).getNickname();
            if(clientHandlers.size() == 4) {
                if(i == 0 || i ==2) {
                    towerColour = TowerColour.BLACK;
                }
                if(i == 1 || i == 3) {
                    towerColour = TowerColour.WHITE;
                }
            }
            else if(clientHandlers.size()==3) {
                if(i == 0)
                    towerColour =TowerColour.BLACK;
                if (i==1)
                    towerColour = TowerColour.WHITE;
                if (i==2)
                    towerColour = TowerColour.GREY;
            }
                else if(clientHandlers.size()==2)
            {
                if (i==0)
                    towerColour = TowerColour.BLACK;
                
                if (i==1)
                    towerColour = TowerColour.WHITE;
                
            }
                int wiz = i;
                int ID = i;


            Player p = new Player(nickname,wiz,towerColour,ID);
            players.add(p);
        }
        return players;
    }

    public String[] getArrayNickname(List<ClientHandler> clientHandlers) {
        names = new String[clientHandlers.size()];
        for (int i=0; i<clientHandlers.size(); i++) {
                names[i] = clientHandlers.get(i).getNickname();
        }

        return names;
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
        getServer().gameEnded(this,new ResultsNotify(getGameTable().getIslands(),players,getGameTable().getBoards()));

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

    public boolean getBooleanGameMode(GameMode gameMode) {
        if(gameMode==GameMode.STANDARD)
            return false;
        else
            return true;

    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }

    public void setGameTable(GameTable gameTable) {
        GameTable = gameTable;
    }
    public GameTable getGameTable() {
        return GameTable;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean getCheck() {
        return check;
    }

}
