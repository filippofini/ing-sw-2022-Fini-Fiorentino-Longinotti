package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameState;

import java.util.Scanner;

/**
 * Mushroom collector character card
 */
public class MushroomCollector extends CharacterCard {
    private final int ID_code=9;
    private int cost=3;
    private int uses=0;
    @Override
    public void effect(GameState GS){
        Scanner sc= new Scanner(System.in);
        GS.getGT().getIslands().get(GS.getGT().getMother_nature_pos()).setProhibition_colour(true);

        int choicecolor = sc.nextInt();//colourCLI
        GS.getGT().getIslands().get(GS.getGT().getMother_nature_pos()).setProh_col(choicecolor);
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
