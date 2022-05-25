package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.server.Server;
import java.io.Serializable;


public class GameController implements Serializable {

    private static final long serialVersionUID = 4405183481677036856L;
    private String[] players;
    private int n_player;
    public static final String SERVER_NICKNAME = "server";
    private Server server;


    public GameController(){
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

    public void setServer(Server server) {
        this.server = server;
    }
}
