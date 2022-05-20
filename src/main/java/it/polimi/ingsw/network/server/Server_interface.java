package it.polimi.ingsw.network.server;

public interface Server_interface {
    void newGameManager();
    void setNumberOfPlayersForNextGame(ClientHandlerInterface clientHandler, int numberOfPlayersForNextGame);
}

