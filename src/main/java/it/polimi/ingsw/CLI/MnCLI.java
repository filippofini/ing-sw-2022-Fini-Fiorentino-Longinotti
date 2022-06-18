package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.toServer.MoveMnReply;

public class MnCLI {


    public static void motherNatureMovementRequest(Client client, int Mn_pos, Player Current_Player){
        System.out.println("Choose how many steps Mother Nature have to do:\n");
        System.out.println("Mother Nature position: Island["+Mn_pos+"]\n");
        System.out.println("Number of steps possible:"+Current_Player.getMoves()+"\n");
        int choice=InputParser.getInt();
        while (choice<0 ||choice>=Current_Player.getMoves()){
            System.out.println("It's not possible to do this number of steps, please choose a valid number:\n");
            choice=InputParser.getInt();
        }
        client.sendMessageToServer(new MoveMnReply(choice));
    }

}
