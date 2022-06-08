package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.DiskColour;
import it.polimi.ingsw.model.GameTable;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.toServer.ChooseCloudReply;

import java.util.List;

public class CloudCLI {
    public static void chooseCloud(Client client, List<Cloud> clouds,Player player){
        int choice;
        System.out.println("choose the number of the cloud you want to choose:\n");
        for(int i=0;i<clouds.size();i++){
            System.out.println("Cloud["+i+"]\n");
            for(int j=0;j<5;j++){
                System.out.println(DiskColour.printColour(j) + clouds.get(i).getArr_students()[j]);
            }
        }
        choice=InputParser.getInt();

        while(choice<0 || choice>=player.getDeck().count_elements()){
            System.out.println("Number not valid,please choose a number from the list");
            choice=InputParser.getInt();
        }
        client.sendMessageToServer(new ChooseCloudReply(choice));
    }
}
