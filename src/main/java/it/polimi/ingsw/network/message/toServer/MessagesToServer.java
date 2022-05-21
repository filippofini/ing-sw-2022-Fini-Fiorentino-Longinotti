package it.polimi.ingsw.network.message.toServer;

import it.polimi.ingsw.network.server.ClientHandlerInterface;
import it.polimi.ingsw.network.server.Server_interface;

import java.io.Serializable;

public interface MessagesToServer extends Serializable {
    void handleMessage(Server_interface server, ClientHandlerInterface clientHandler);
}
