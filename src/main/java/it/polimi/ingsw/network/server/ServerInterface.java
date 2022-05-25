package it.polimi.ingsw.network.server;

public interface ServerInterface {
    void newGameManager();
    void setNumberOfPlayersForNextGame(ClientHandlerInterface clientHandler, int numberOfPlayersForNextGame);
}

