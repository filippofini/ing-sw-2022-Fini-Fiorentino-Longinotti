package it.polimi.ingsw.network.message.toServer;

import it.polimi.ingsw.enumerations.ClientHandlerPhase;
import it.polimi.ingsw.network.message.toClient.NameRequest;
import it.polimi.ingsw.network.server.ClientHandlerInterface;
import it.polimi.ingsw.network.server.ServerInterface;

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
    public void handleMessage(ServerInterface server, ClientHandlerInterface clientHandler) {

        clientHandler.setNickname(name);
    }

    @Override
    public String toString() {
        return "Received name: " + name;
    }
}
