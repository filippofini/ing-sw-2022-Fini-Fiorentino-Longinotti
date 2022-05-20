package it.polimi.ingsw.network.server;

import it.polimi.ingsw.enumerations.ClientHandlerPhase;
import it.polimi.ingsw.model.GameMode;

import java.io.Serializable;

public interface ClientHandlerInterface {

    void sendMessageToClient(Serializable message);
    GameMode getGameMode();
    String getNickname();
    ClientHandlerPhase getClientHandlerPhase();
    void setNickname(String nickname);
    void setClientHandlerPhase(ClientHandlerPhase clientHandlerPhase);
    void setGameMode(GameMode gameMode);
    void setGameStarted(boolean gameStarted);
    void setNumberOfPlayersForNextGame(int numberOfPlayersForNextGame);
    //Controller getController();
    void startTimer();
}
