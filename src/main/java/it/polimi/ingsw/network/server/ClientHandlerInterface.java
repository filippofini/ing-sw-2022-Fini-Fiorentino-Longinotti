package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
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
    void setIslandToMove(int isl);
    void setposToMove(int pos);
    void setpos(int pos);
    void setcolour(int colour);
    void setNickname(String nickname);
    void setMnmovement(int mnmovement);
    void setClientHandlerPhase(ClientHandlerPhase clientHandlerPhase);
    void setGameMode(GameMode gameMode);
    void setGameStarted(boolean gameStarted);
    void setNumberOfPlayersForNextGame(int numberOfPlayersForNextGame);
    //Controller getController();
    void startTimer();
    void setAssistantCardChosen(int assistantCardChosen);
    void setCloudChosen(int cloudChosen);
    int getIslandToMove();
    int getMnmovement();
    int getAssistantCardChosen();
    int getStudToMove();
    int getPos();
    int getCloudChosen();
    void setUseCharacterCard(int useChCard);
    int getUseCharacterCard();
    void setChCardUsed(int chCardUsed);
    int getChCardUsed();
    boolean getCanBeUsed();
    void setCanBeUsed(boolean canBeUsed);
    GameController getGameController();
}
