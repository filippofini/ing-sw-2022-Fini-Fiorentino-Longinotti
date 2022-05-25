package it.polimi.ingsw.network.message.toServer;

import it.polimi.ingsw.network.server.ClientHandlerInterface;
import it.polimi.ingsw.network.server.ServerInterface;

import java.io.Serializable;

public interface MessagesToServer extends Serializable {
    void handleMessage(ServerInterface server, ClientHandlerInterface clientHandler);
}
