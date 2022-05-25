package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.DiskColour;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.server.ColourResponse;

public class ColourCLI {
    public static void chooseColourRequest(Client client){
        int choice;
        System.out.println("choose the number of a colour:\n");
        for(int i=0;i<5;i++){
            System.out.println(DiskColour.values()[i]+"["+i+"]\n");
        }
        choice=InputParser.getInt();

        while(choice<0 || choice>4){
            System.out.println("Number not valid,please choose a number from the list");
            choice=InputParser.getInt();
        }
        client.sendMessageToServer(new ColourResponse(choice));
    }
}
