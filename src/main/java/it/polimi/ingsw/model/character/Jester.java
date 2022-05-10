package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;
import it.polimi.ingsw.model.Game_State;
import it.polimi.ingsw.model.Student;

/**
 * This class represents the jester character card.
 */
public class Jester extends Character_card {
    private final int ID_code=7;
    private int cost=1;
    private int uses=0;
    private int[] students;
    private int[] chosen_students;
    private int current_player;
    private int n_moved;

    public Jester(int[] students){
        this.students = students;
    }

    @Override
    public void effect(Game_State game_state){
        n_moved = 0;
        for (int i = 0; i < 5; i++) {
            n_moved = n_moved + students[i];
        }
        Student moved;
        //TODO: finish
        for (int i = 0; i < n_moved; i++) {
            for (int j = 0; j < 5; j++) {
                moved = game_state.getGT().getBoards()[current_player].getArrEntranceStudents()[j];
            }
        }
        setUses();
    }

    /**
     * This method sets the student to be moved from the card to the island.
     * @param chosen_students The student to be moved from the card to the island.
     */
    public void setChosen_students(int[] chosen_students) {
        this.chosen_students = chosen_students;
    }

    /**
     * This method sets the player that plays the card.
     * @param player The number representing the player that played the card.
     */
    public void setCurrent_player(int player) {
        this.current_player = player-1;
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

    /**
     * This method returns the array of students on the card.
     * @return The array of students on the card.
     */
    public int[] getStudents() {
        return students;
    }
}
