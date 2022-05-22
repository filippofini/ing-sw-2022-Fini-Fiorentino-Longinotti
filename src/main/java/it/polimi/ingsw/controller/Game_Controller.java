package it.polimi.ingsw.controller;

import java.io.Serializable;
import java.util.Observable;

public class Game_Controller extends Observable implements Serializable {

    private static final long serialVersionUID = 4405183481677036856L;

    private String[] players;
    private int n_player;
    public static final String SERVER_NICKNAME = "server";


    private Game_Controller(){
        this.players = new String[n_player];
    }


    /*
    public boolean isGameStarted() {
    }

    public boolean checkLoginName(String name, VirtualView virtualView) {

    }

    public void loginHandler(String name, VirtualView virtualView) {

    }
     */

    public void removeVirtualView(String name, boolean notifyEnabled) {

    }
    
    /*
    public Turn_Controller getTurnController() {

    }
    */

    public void broadcastDisconnectionMessage(String name, String s){

    }

   public void endGame() {

   }


    public void handleClientDisconnection(String nickname) {
    }
}
