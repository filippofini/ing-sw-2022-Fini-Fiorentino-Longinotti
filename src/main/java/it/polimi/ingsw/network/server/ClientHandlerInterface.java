package it.polimi.ingsw.network.server;

import it.polimi.ingsw.enumerations.ClientHandlerPhase;
import it.polimi.ingsw.model.GameMode;

import java.io.Serializable;

public interface ClientHandlerInterface {

    void sendMessageToClient(Serializable message);
    GameMode getGameMode();
    String getNickname();
    boolean checkMnMovThisTurn();


    ClientHandlerPhase getClientHandlerPhase();
    void setstudToMove(int stud);
    void setposToMove(int pos);
    void setcolour(int colour);
    void setNickname(String nickname);
    void setMnmovement(int mnmovement);
    void setClientHandlerPhase(ClientHandlerPhase clientHandlerPhase);
    void setGameMode(GameMode gameMode);
    void setGameStarted(boolean gameStarted);
    void setNumberOfPlayersForNextGame(int numberOfPlayersForNextGame);
    //Controller getController();
    void startTimer();
}
