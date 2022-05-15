package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Character_card;
import it.polimi.ingsw.model.Disk_colour;
import it.polimi.ingsw.model.Game_State;
import it.polimi.ingsw.model.Student;

import java.util.Scanner;

/**
 * Minstrel character card
 */
public class Minstrel extends Character_card {
    private final int ID_code=10;
    private int cost=1;
    private int uses=0;
    @Override
    public void effect(Game_State game_state){
        int numOfMoving;
        int studColour;
        int entrchange;


        Scanner sc= new Scanner(System.in);
        //TODO:cornercase
        numOfMoving= sc.nextInt();
        for (int i=0;i<numOfMoving;i++){
            studColour=sc.nextInt();
            for(int j=0;j<game_state.getGT().getBoards()[game_state.getCurr_player()].getArrEntranceStudents().length;j++){
                //print all of the entrance students
            }
            entrchange=sc.nextInt();
            game_state.getGT().getBoards()[game_state.getCurr_player()].getArrPositionStudents()[studColour]--;
            game_state.getGT().getBoards()[game_state.getCurr_player()].getArrPositionStudents()[game_state.getGT().getBoards()[game_state.getCurr_player()].getArrEntranceStudents()[entrchange].getColour()]++;
            game_state.getGT().getBoards()[game_state.getCurr_player()].getArrEntranceStudents()[entrchange]= new Student(inverse_color(studColour));

        }
        setUses();

    }
    /**
     * This method convert a position in the respective color
     * @param color The index of the array that rapresent a color
     */
    public Disk_colour inverse_color(int color){
        if(color==0){
            return Disk_colour.YELLOW;
        }
        else if(color==1){
            return Disk_colour.RED;
        }
        else if(color==2){
            return Disk_colour.PINK;
        }
        else if(color==3){
            return Disk_colour.BLUE;
        }
        else{
            return Disk_colour.GREEN;
        }
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
