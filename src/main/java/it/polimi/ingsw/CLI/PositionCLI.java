package it.polimi.ingsw.CLI;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.toServer.PositionReply;

public class PositionCLI {
    public static void choosePositionRequest(Client client,int upperlimit){
        int choice;
        System.out.println("choose the Position:\n (allowed from 0 to "+upperlimit+" excluded)\n");
        choice=InputParser.getInt();

        while(choice<0 || choice>=upperlimit){
            System.out.println("Number not allowed,please choose another number");
            choice=InputParser.getInt();
        }
        client.sendMessageToServer(new PositionReply(choice));
    }
}
