package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameState;

import java.util.Scanner;

/**
 * Thief character card
 */
public class Thief extends CharacterCard {
    private final int ID_code=12;
    private int cost=3;
    private int uses=0;
    @Override
    public void effect(GameState game_state){
        int chosencolour;
        Scanner sc= new Scanner(System.in);
        //TODO: CLI choose a number that represents colours
        chosencolour=sc.nextInt();
        while(chosencolour<0 || chosencolour>4){
            System.out.println("Number not valid, please select a number from the list above");
            chosencolour=sc.nextInt();
        }
        for(int i=0;i<game_state.getGT().getNum_players();i++){
            for(int j=0; j<3;j++){
                if(game_state.getGT().getBoards()[i].getArrPositionStudents()[chosencolour]>0){
                    game_state.getGT().getBoards()[i].getArrPositionStudents()[chosencolour]--;
                    game_state.getGT().getBag()[chosencolour]++;
                }
            }
        }
    setUses();
    }

    /**
     * This method is used when a card has been used.
     * It increases the uses and the cost by 1.
     */
    public void setUses() {
        this.uses++;
        setCost(cost+1);
    }

    /**
     * This method returns the cost.
     * @return The cost.
     */
    public int getCost() {
        return cost;
    }

    /**
     * This method sets the cost.
     * @param cost The new cost of the card.
     */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * This method returns the uses of the card.
     * @return The uses of the card.
     */
    public int getUses() {
        return uses;
    }

    /**
     * This method return the ID code of the card.
     * @return The ID code of the card.
     */
    public int getID_code() {
        return ID_code;
    }
}
