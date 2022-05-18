package it.polimi.ingsw.network.client;

import java.io.Serializable;

public interface Client_interface {
    void sendMessageToServer(Serializable message);
}
