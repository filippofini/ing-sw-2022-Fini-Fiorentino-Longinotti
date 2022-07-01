package it.polimi.ingsw.CLI;


import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.toServer.*;

import java.util.List;

/**
 * This class represent the choice for the assistant in the CLI.
 */
public class AssistantCLI {

    /**
     * Constructor of the class.
     * @param client The client.
     * @param player The current player.
     * @param GT The game table.
     */
    public static void chooseAssistantCard(Client client, Player player, GameTable GT){
        int choice;
        System.out.println("choose the number of a assistant:\n");
        for(int i=0;i<player.getDeck().count_elements();i++){
            System.out.println(player.getDeck().getCards().get(i)+"["+i+"]\n");
        }
        choice=InputParser.getInt();

        while(choice<0 || choice>=player.getDeck().count_elements() || !check_if_playable(player.getDeck().getCards().get(choice),GT)){
            System.out.println("Number not valid,please choose a number from the list");
            choice=InputParser.getInt();
        }

        client.sendMessageToServer(new ChooseAssistantCardReply(choice));
    }
    /**
     * This method checks if an assistance card is playable.
     * That means it can't be on the discard deck of any other player.
     * @param chosen The assistance card chosen to be played.
     * @param GT The game table.
     * @return {@code False} if card is already played, {@code True} otherwise.
     */
    public static boolean check_if_playable(AssistanceCard chosen, GameTable GT){
        boolean playable_card = true;
        for (int i = 0; i < GT.getNum_players() && playable_card; i++) {
            if(GT.getDiscard_deck()[i].equals(chosen)){
                playable_card = false;
            }
        }
        return playable_card;
    }

    public static void ShowStudent(Client client, Student[] students){
        int choice;
        System.out.println("choose the number of a student for the character card:\n");
        for(int i=0;i<students.length;i++){
            System.out.println(students[i].getEnumColour()+":["+i+"]\n");
        }
        choice=InputParser.getInt();

        while(choice<0 || choice>3){
            System.out.println("Number not valid,please choose a number from the list");
            choice=InputParser.getInt();
        }
        client.sendMessageToServer(new ShowStudentsReply(students[choice].getColour()));
    }
    public static void HeraldIsland(Client client, List<Island> islands) {
        System.out.println("Choose the island for the character card\n");

        for (int i=0;i< islands.size();i++){
            System.out.println("Island["+i+"]:\n");
            for (int k=0;k<5; k++){
                System.out.println(DiskColour.values()[k] +": "+islands.get(i).getArr_students()[k]+"\n");
            }
        }


        int choice=InputParser.getInt();
        while (choice<0 ||choice>=islands.size()){
            System.out.println("This Island does not exit, please choose a valid number:\n");
            choice=InputParser.getInt();
        }

        client.sendMessageToServer(new HeraldIslandReply(choice));
    }

}
