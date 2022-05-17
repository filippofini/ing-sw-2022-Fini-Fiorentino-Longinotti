package it.polimi.ingsw.network.message;

/**
 * Message used to send to the server the game mode picked by the client.
 */
    public class GameMode_reply extends Message{

        private boolean game_mode;
    //    private static final long serialVersionUID;


    /**
     * Constructor of the class.
     * @param name The name of player.
     * @param game_mode the game mode picked.
     */
    public GameMode_reply(String name, boolean game_mode) {
        super(name, MessageType.GAMEMODE_REPLY);
        this.game_mode = game_mode;
    }

    public boolean getGameMode() {
        return game_mode;
    }

    @Override
    public String toString() {
        return "GameMode_reply{" +
                "name=" + getName() +
                ", game mode=" + game_mode +
                '}';
    }

}
