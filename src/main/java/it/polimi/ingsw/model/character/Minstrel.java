package it.polimi.ingsw.model.character;

import it.polimi.ingsw.CLI.InputParser;
import it.polimi.ingsw.CLI.PositionCLI;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.DiskColour;
import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.Student;

import java.util.Scanner;

/**
 * Minstrel character card
 */
public class Minstrel extends CharacterCard {
    private final int ID_code=10;
    private int cost=1;
    private int uses=0;
    @Override
    public void effect(GameState game_state){
        int numOfMoving;
        int studColour;
        int entrchange;

        PositionCLI.choosePositionRequest(Client);

        Scanner sc= new Scanner(System.in);

        numOfMoving= sc.nextInt();//positionCLI
        while(numOfMoving>0 && numOfMoving<3 ){
            numOfMoving= sc.nextInt();//positionCLI
        }
        for (int i=0;i<numOfMoving;i++){
            studColour=sc.nextInt();//pos cli
            while(game_state.getGT().getBoards()[game_state.getCurr_player()].getArrPositionStudents()[studColour]==0){
                studColour=sc.nextInt();//pos cli
            }
            for(int j=0;j<game_state.getGT().getBoards()[game_state.getCurr_player()].getArrEntranceStudents().length;j++){
                //Boardcli,print entrance students
            }
            entrchange= InputParser.getInt();//positionCLI

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
    public DiskColour inverse_color(int color){
        if(color==0){
            return DiskColour.YELLOW;
        }
        else if(color==1){
            return DiskColour.RED;
        }
        else if(color==2){
            return DiskColour.PINK;
        }
        else if(color==3){
            return DiskColour.BLUE;
        }
        else{
            return DiskColour.GREEN;
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
