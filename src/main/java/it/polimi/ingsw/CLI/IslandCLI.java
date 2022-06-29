package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.DiskColour;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.toClient.DisplayIslandInfoRequest;
import it.polimi.ingsw.network.message.toServer.DisplayIslandInfoReply;
import it.polimi.ingsw.network.message.toServer.MoveStudentReply;

import java.util.List;

/**
 * This class represents the CLI for the island.
 */
public class IslandCLI {

    /**
     * This method displays the island info.
     *
     * @param client
     * @param island   The island.
     * @param islandID The island ID.
     */
    public static void displayIslandInfo(Client client, Island island, int islandID){

        System.out.println("Island["+islandID+":]\n");
        for (int k=0;k<5; k++){
            System.out.println(DiskColour.values()[k] +": "+island.getArr_students()[k]+"\n");
        }

        System.out.println("Influence: "+island.getInfluence_controller()+"\n");
        System.out.println("number of towers: "+island.getTower()+"\n");
        System.out.println("Owned by: player "+island.getPlayer_controller()+"\n");

        client.sendMessageToServer(new DisplayIslandInfoReply());
    }

    /**
     * This method requests the island where to move the student.
     * @param client The client.
     * @param islands The list of islands.
     * @param stud_to_island The chosen student to be moved.
     */
    public  static void MoveStudentToIslandRequest(Client client, List<Island> islands, Student stud_to_island){

            System.out.println("Choose the island where to add the student:"+stud_to_island+"\n");
            int choice=InputParser.getInt();
            while (choice<0 ||choice>=islands.size()){
                System.out.println("This Island does not exit, please choose a valid number:\n");
                choice=InputParser.getInt();
            }

            client.sendMessageToServer(new MoveStudentReply(choice));

    }

}
