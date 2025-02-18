package it.polimi.ingsw.model;

import java.util.List;

/**
 * This class represent the game state.
 * It creates the game table and the class player.
 * It also includes the flags for expert mode, the teams if the game is 4 players and the name of the players.
 */
public class GameState {
    private Player[] players;
    private final GameTable gameTable;
    private int currPlayer;
    private String[] names;
    private int[] wizards;
    private List<Player> playerList;

    /**
     * Constructor of the class.
     * @param playersNum Number of players in the game.
     * @param names Strings containing the names of the players.
     * @param wizards Array of integers containing the numbers of the wizards to be assigned to each player.
     * @param currPlayer The current player.
     */
    public GameState(int playersNum, String[] names, int[] wizards, int currPlayer, List<Player> playerList){
        this.names = names;
        this.wizards = wizards;
        this.currPlayer = currPlayer;
        this.playerList = playerList;

        players = new Player[playersNum];
        for(int i = 0; i < playersNum; i++){
            players[i] = playerList.get(i);
        }

        gameTable = new GameTable(playersNum);
    }

    /**
     * This method returns the game table.
     * @return The game table.
     */
    public GameTable getGameTable() {
        return gameTable;
    }

    /**
     * This method returns the array of players.
     * @return The array of players.
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * This method returns the list of players.
     * @return The list of players.
     */
    public List<Player> getPlayersList() {
        return playerList;
    }

    /**
     * This method sets the current player.
     * @param player The current player.
     */
    public void setCurrPlayer(int player){
        currPlayer = player;
    }


    /**
     * This method returns the current player.
     * @return The current player.
     */
    public int getCurrPlayer() {
        return currPlayer;
    }
}
