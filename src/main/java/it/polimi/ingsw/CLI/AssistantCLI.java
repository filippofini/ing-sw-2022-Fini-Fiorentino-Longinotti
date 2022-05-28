package it.polimi.ingsw.CLI;


import it.polimi.ingsw.model.AssistanceCard;
import it.polimi.ingsw.model.GameTable;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.toServer.ChooseAssistantCardReply;


public class AssistantCLI {
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
}
