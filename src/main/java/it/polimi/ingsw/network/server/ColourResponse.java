package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.message.toServer.MessagesToServer;

public class ColourResponse implements MessagesToServer {
    private final int colour;

    public ColourResponse(int colour){this.colour=colour;}

    public void handleMessage(Server_interface server, ClientHandlerInterface clientHandler) {

        clientHandler.setcolour(colour);
    }

    @Override
    public String toString() {
        return "Colour Received";
    }
}
