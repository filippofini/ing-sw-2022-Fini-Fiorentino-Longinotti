package it.polimi.ingsw.network.message.toClient;


import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.view.ViewInterface;

public class displayIslandinfoRequest extends MessagesToClient{
    Island island;
    int islandID;
    public displayIslandinfoRequest(Island island, int islandID){
        super(true);
        this.island=island;
        this.islandID=islandID;
    }
    @Override
    public void handleMessage(ViewInterface view) {
        view.displayIslandInfo(island,islandID);}

}
