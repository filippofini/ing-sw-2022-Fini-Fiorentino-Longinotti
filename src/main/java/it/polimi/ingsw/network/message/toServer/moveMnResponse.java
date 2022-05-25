package it.polimi.ingsw.network.message.toServer;

import it.polimi.ingsw.enumerations.ClientHandlerPhase;
import it.polimi.ingsw.network.server.ClientHandlerInterface;
import it.polimi.ingsw.network.server.ServerInterface;

public class moveMnResponse implements MessagesToServer{
    private final int Mnmovement;

    public moveMnResponse(int Mnmovement){
        this.Mnmovement = Mnmovement;
    }

    @Override
    public void handleMessage(ServerInterface server, ClientHandlerInterface clientHandler) {
        /*
        if(!clientHandler.checkMnMovThisTurn()){
            clientHandler.sendMessageToClient(new MnmovementRequest());
        }*/
        clientHandler.setClientHandlerPhase(ClientHandlerPhase.SET_UP_FINISHED);
        clientHandler.setMnmovement(Mnmovement);
    }

    @Override
    public String toString() {
        return "Received Movement: " + Mnmovement;
    }

}
