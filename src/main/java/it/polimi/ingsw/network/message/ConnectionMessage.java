package it.polimi.ingsw.network.message;

/**
 * This enumeration contains the basic messages for connection to server.
 */
public enum ConnectionMessage {
    PING("Ping"),
    CONNECTION_CLOSED("Connection closed");
    String message;

    ConnectionMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
