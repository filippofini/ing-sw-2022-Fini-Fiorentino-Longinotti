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
        Deck deck = player.getDeck();
        System.out.println("choose the number of a assistant:\n");
        for(int i = 0; i < deck.countElements(); i++){
            System.out.println(deck.getCards().get(i)+"["+i+"]\n");
        }

        choice = InputParser.getInt();
        while(choice < 0 || choice >= deck.countElements() || !GT.checkIfPlayable(deck.getCards().get(choice), deck)){
            System.out.println("Number not valid, please choose a number from the list");
            choice = InputParser.getInt();
        }

        client.sendMessageToServer(new ChooseAssistantCardReply(choice));
        System.out.println("\nMother Nature start position: island["+GT.getMotherNaturePos()+"]\n");
    }

    /**
     * This method shows the students on a character card.
     * @param client The client.
     * @param students The array of students on the card.
     */
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
        client.sendMessageToServer(new ShowStudentsReply(students[choice].getColor()));
    }

    /**
     * This method sets the island where to move a student picked from the herald character card.
     * @param client The client.
     * @param islands The list of islands.
     */
    public static void HeraldIsland(Client client, List<Island> islands) {
        System.out.println("Choose the island for the character card\n");

        for (int i=0;i< islands.size();i++){
            System.out.println("Island["+i+"]:\n");
            for (int k=0;k<5; k++){
                System.out.println(DiskColour.values()[k] +": "+islands.get(i).getArrStudents()[k]+"\n");
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
