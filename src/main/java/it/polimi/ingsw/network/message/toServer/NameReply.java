package it.polimi.ingsw.network.message.toServer;
import it.polimi.ingsw.network.server.ClientHandlerInterface;
import it.polimi.ingsw.network.server.ServerInterface;



/**
 * Message to notify the chosen name.
 */
public class NameReply implements MessagesToServer{

    private final String nickname;

    private static final String NAME_REGEXP = "^([a-zA-Z0-9._\\-]{1,20})$";
    //private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEXP);

    public NameReply(String nickname){
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void handleMessage(ServerInterface server, ClientHandlerInterface clientHandler) {

        clientHandler.setNickname(nickname);

    }
    @Override
    public String toString() {
        return "Received name: " + nickname;
    }
}
