package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.GameMode;

public interface ServerInterface {
    void newGameManager(GameMode mode);
    void setNumberOfPlayersForNextGame(ClientHandlerInterface clientHandler, int numberOfPlayersForNextGame);
}

