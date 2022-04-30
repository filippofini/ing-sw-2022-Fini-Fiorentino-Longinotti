package it.polimi.ingsw.model;

/**
 * This class represent the game state.
 * It creates the game table and the class player.
 * It also includes the flags for expert mode, the teams if the game is 4 players and the name of the players.
 * The class has the current turn.
 */
public class Game_State {
    private int n_players;
    private Player[] players;
    private Game_table GT;
    private Turn turn;
    private int[] teams;
    private boolean chat;
    private boolean expert_mode;

    private String[] names;
    private int[] wizard;

    /**
     * Constructor of the class.
     * @param n_players Number of players in the game.
     * @param names Strings containing the names of the players.
     * @param wizard Array of integers containing the numbers of the wizards to be assigned to each player.
     * @param expert_mode {@code True} if expert mode is enabled, {@code False} if not.
     */
    public Game_State(int n_players,String[] names,int wizard[], boolean expert_mode){
        this.n_players = n_players;
        this.names = names;
        this.wizard = wizard;
        this.expert_mode = expert_mode;
        turn = new Turn();

        players = new Player[n_players];
        for(int i=0; i<n_players;i++){
            players[i] = new Player(names[i], wizard[i], Tower_colour.values()[i], i+1);
        }

        GT = new Game_table(n_players, turn);
        if(n_players == 4){
            teams = new int[4];
            chat = true;
        }
    }

    /**
     * This method returns the game table.
     * @return The game table.
     */
    public Game_table getGT() {
        return GT;
    }

    /**
     * This method returns the array of players.
     * @return The array of players.
     */
    public Player[] getPlayers() {
        return players;
    }


}
