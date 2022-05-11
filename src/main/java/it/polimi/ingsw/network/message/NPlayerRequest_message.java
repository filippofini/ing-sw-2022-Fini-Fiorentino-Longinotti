package it.polimi.ingsw.network.message;

import it.polimi.ingsw.controller.Game_Controller;

/**
 * Message used to ask the client the maximum number of players of the game.
 */
public class NPlayerRequest_message extends Message{

    private static final long serialVersionUID = -2155556142315548857L;

    /**
     * Constructor of the class.
     */
    public NPlayerRequest_message(){
        super(Game_Controller.SERVER_NICKNAME, MessageType.NPLAYER_REQUEST);
    }

    @Override
    public String toString() {
        return "NPlayerRequest_message{" +
                "name=" + getName() +
                '}';
    }
}
