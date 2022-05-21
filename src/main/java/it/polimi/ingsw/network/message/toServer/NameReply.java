package it.polimi.ingsw.network.message.toServer;

import it.polimi.ingsw.enumerations.ClientHandlerPhase;
import it.polimi.ingsw.network.message.toClient.NameRequest;
import it.polimi.ingsw.network.server.ClientHandlerInterface;
import it.polimi.ingsw.network.server.Server_interface;

import java.util.regex.Pattern;

/**
 * Message to notify the chosen name.
 */
public class NameReply implements MessagesToServer{

    private final String name;
    private static final String NAME_REGEXP = "^([a-zA-Z0-9._\\-]{1,20})$";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEXP);

    public NameReply(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void handleMessage(Server_interface server, ClientHandlerInterface clientHandler) {
        if (name == null || clientHandler.getClientHandlerPhase() != ClientHandlerPhase.WAITING_NICKNAME){
            return;
        } else if (!NAME_PATTERN.matcher(name).matches()) {
            clientHandler.sendMessageToClient(new NameRequest(true, false));
            return;
        }
        clientHandler.setClientHandlerPhase(ClientHandlerPhase.WAITING_IN_THE_LOBBY);
        clientHandler.setNickname(name);
    }

    @Override
    public String toString() {
        return "Received name: " + name;
    }
}
