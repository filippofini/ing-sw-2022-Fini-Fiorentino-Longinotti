package it.polimi.ingsw.model;

/**
 * Class game state
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
     * Constructor of the class
     * @param n_players number of players
     * @param names strings containing the names of the players
     * @param wizard array of int containing the numbers of the wizards
     * @param expert_mode true if expert mode is enabled, false otherwise
     */
    public Game_State(int n_players,String[] names,int wizard[], boolean expert_mode){
        this.n_players = n_players;
        this.names = names;
        this.wizard = wizard;
        this.expert_mode = expert_mode;
        turn = new Turn();
        for(int i=0; i<n_players;i++){
            players[i] = new Player(names[i], wizard[i], Tower_colour.values()[i], i+1);
        }
        GT = new Game_table(n_players, turn);
        if(n_players == 4){
            teams = new int[4];
            chat = true;
        }
    }
}
