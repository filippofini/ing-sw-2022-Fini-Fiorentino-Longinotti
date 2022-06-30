package it.polimi.ingsw.network.message.toServer;

import it.polimi.ingsw.network.server.ClientHandlerInterface;
import it.polimi.ingsw.network.server.ServerInterface;

public class HeraldIslandReply implements MessagesToServer{
    private final int heraldIsland;

    public HeraldIslandReply(int heraldIsland){
        this.heraldIsland= heraldIsland;
    }


    public int getHeraldIsland() {
        return heraldIsland;
    }

    @Override
    public void handleMessage(ServerInterface server, ClientHandlerInterface clientHandler){
        clientHandler.setHeraldIsland(heraldIsland);
    }

    @Override
    public String toString() {
        return "Received Island to move";
    }
}
