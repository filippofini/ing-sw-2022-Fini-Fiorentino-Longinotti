package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.Disk_colour;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Student;

import java.util.List;

public class IslandCLI {

    public static void displayIslandInfo(Island island, int islandID){

        System.out.println("Island["+islandID+":]\n");
        for (int k=0;k<5; k++){
            System.out.println(Disk_colour.values()[k] +": "+island.getArr_students()[k]+"\n");
        }

        System.out.println("Influence: "+island.getInfluence_controller()+"\n");
        System.out.println("number of towers: "+island.getTower()+"\n");
        System.out.println("Owned by: player "+island.getPlayer_controller()+"\n");
    }

    public  static void MoveStudentToIslandRequest(Client client,List<Island> islands,Student stud_to_island){

            System.out.println("Choose the island where to add the student:"+stud_to_island+"\n");
            int choice=InputParser.getInt();
            while (choice<0 ||choice>=islands.size()){
                System.out.println("This Island does not exit, please choose a valid number:\n");
                choice=InputParser.getInt();
            }

            client.sendMessageToServer(new moveStudentResponse(choice));

    }



}
