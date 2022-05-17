package it.polimi.ingsw.network.message;

import it.polimi.ingsw.controller.Game_Controller;

/**
 * Message used to ask the client the game mode.
 */
public class GameModeRequest_message extends Message{

   // private static final long serialVersionUID;

    /**
     * Constructor of the class.
     */
    public GameModeRequest_message(){
        super(Game_Controller.SERVER_NICKNAME,MessageType.GAMEMODE_REQUEST);
    }

    @Override
    public String toString() {
        return "GameModeRequest_message{" +
                "name=" + getName() +
                '}';
    }


}
