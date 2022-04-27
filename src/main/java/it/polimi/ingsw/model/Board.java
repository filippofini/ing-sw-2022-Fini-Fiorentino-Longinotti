package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents the board.
 * Each student has his personal board containing an array of int representing the students (see more at: {@link it.polimi.ingsw.model.Disk_colour}.
 * The class includes the methods to move the students into and out of the board.
 * Each board has towers with different colours (see more at: {@link it.polimi.ingsw.model.Tower_colour}).
 * The towers in the board are 8 if the game has 2 or 4 players, 7 if the game has 3 players.
 */
public class Board {
    private final int board_player;
    private int n_towers;
    private int[] arrPositionStudents;
    private Student[] arrEntranceStudents;
    private boolean[] arrProfessors;
    private int tower;
    private int maxEntranceStudents;
    private boolean[][] trackCoins = new boolean[5][3];

    /**
     * Constructor of the class.
     * @param numOfPlayers The number of players in the game.
     * @param playerID The player ID assigned to the board. It's an indirect reference to the player.
     * @param tower The tower colour assigned to the player and to the board.
     */
    public Board(int numOfPlayers, int playerID, Tower_colour tower){
        this.board_player = playerID;
        this.tower = tower.getTower_translate();
        arrPositionStudents = new int[5];
        arrProfessors = new boolean[5];
        for(int i=0; i<5;i++){
            for(int j=0; j<3;j++){
                trackCoins[i][j]=false;
            }
        }

        //for now with if, maybe later will become switch case
        // need re-check values
        // remember that now there is the space for the students, not the instances of the students!
        if(numOfPlayers == 2 ){
            arrEntranceStudents = new Student[7];
            maxEntranceStudents=7;
            n_towers = 8;
        }
        else if(numOfPlayers == 3){
            arrEntranceStudents = new Student[9];
            maxEntranceStudents=9;
            n_towers = 6;
        }
        //I had supposed that the squads where players(1-2) && players(3-4), if not it will need changes!
        else if(numOfPlayers == 4){
            arrEntranceStudents = new Student[7];
            maxEntranceStudents=7;
            n_towers = 0;
            if((playerID == 1 || playerID == 3))
            n_towers = 8;
        }
    }

    public void add_prof(Disk_colour profColour){
        arrProfessors[profColour.getTranslateColour()]=true;
    }
    public void add_student(Disk_colour studentColour){
        arrPositionStudents[studentColour.getTranslateColour()]++;

    }

    /**
     * This method moves students to the islands.
     * @return The list of students sent to an island.
     */
    public List<Student> moveEntranceStudents(){
        int choiceStudent;
        int choicePosition;
        int studentsChosen=0;
        boolean validChoice = true;
        List<Student> studentToIslands = new ArrayList<Student>();
        Scanner sc= new Scanner(System.in);
        System.out.println("choose the number of one of the students to move:\n");

        for (int i=0;i< maxEntranceStudents-1;i++) {
            System.out.println(arrEntranceStudents[i].getEnumColour() + "["+(i)+"] ");
        }
        System.out.println(arrEntranceStudents[maxEntranceStudents-1].getEnumColour() + "["+(maxEntranceStudents-1)+"]\n");
        do{
            choiceStudent = sc.nextInt();
            if(choiceStudent<maxEntranceStudents){
                validChoice=false;
            }
            else {
                System.out.println("Number not valid,please choose a number from the list");
            }
        } while(validChoice);

        validChoice=true;

        while(studentsChosen <3) {
            if (arrEntranceStudents[choiceStudent].getIsChosen() == false) {
                System.out.println("where do you want to move the student?:\n");
                System.out.println("Dining Room[0] Island[1]");
                do{
                    choicePosition = sc.nextInt();
                    if(choiceStudent<2){
                        validChoice=false;
                    }
                    else {
                        System.out.println("Number not valid,please choose a number from the list");
                    }
                } while(validChoice);
                validChoice=true;

                if(choicePosition == 0){
                    if(arrPositionStudents[arrEntranceStudents[choiceStudent].getColour()]<10){
                        arrPositionStudents[arrEntranceStudents[choiceStudent].getColour()]++;
                        arrEntranceStudents[choiceStudent].Chosen();
                        studentsChosen++;
                    }
                    else{
                        System.out.println("table of colour:"+arrEntranceStudents[choiceStudent].getEnumColour() +" is full, please choose another student");
                    }
                }
                else if(choicePosition == 1){
                    studentToIslands.add(new Student(arrEntranceStudents[choiceStudent].getEnumColour()));
                    arrEntranceStudents[choiceStudent].Chosen();
                    studentsChosen++;
                }
                System.out.println("choose the number of one of the students to move:\n");
                do{
                    choiceStudent = sc.nextInt();
                    if(choiceStudent<maxEntranceStudents){
                        validChoice=false;
                    }
                    else {
                        System.out.println("Number not valid,please choose a number from the list");
                    }
                } while(validChoice);
                validChoice=true;

            }
            else if (arrEntranceStudents[choiceStudent].getIsChosen() == true){
                System.out.println("Student chosen previously,please choose another student\n");
                do{
                    choiceStudent = sc.nextInt();
                    if(choiceStudent<maxEntranceStudents){
                        validChoice=false;
                    }
                    else {
                        System.out.println("Number not valid,please choose a number from the list");
                    }
                } while(validChoice);
                validChoice=true;
            }
        }
        return studentToIslands;
    }

    /**
     * This method checks if coins can be earned.
     * @return The coins earned this turn, to sum with current coins each turn.
     */
    public int coinsEarned(){
        int coins=0;
        for(int i=0; i<5;i++){
            if(arrPositionStudents[i]/3==1){
                if(trackCoins[i][0]==false){
                    coins++;
                    trackCoins[i][0]=true;
                }
            }
            if(arrPositionStudents[i]/3==2){
                if(trackCoins[i][1]==false){
                    coins++;
                    trackCoins[i][1]=true;
                }
            }
            if(arrPositionStudents[i]/3==3){
                if(trackCoins[i][2]==false){
                    coins++;
                    trackCoins[i][2]=true;
                }
            }
        }
        return coins;
    }

    /**
     * This method returns the array of professors that are on the board.
     * @return The array of professors that are on the board.
     */
    public boolean[] getArrProfessors() {
        return arrProfessors;
    }

    /**
     * This method returns the array of students on the board.
     * @return The array of students on the board.
     */
    public int[] getArrPositionStudents() {
        return arrPositionStudents;
    }

    /**
     * This method returns the number of towers remaining on the board.
     * @return The number of towers remaining on the board.
     */
    public int getN_towers() {
        return n_towers;
    }

    /**
     * This method sets the number of towers on the board.
     * @param n_towers The number of towers on the board.
     */
    public void setN_towers(int n_towers) {
        this.n_towers = n_towers;
    }

    /**
     * This method sets the array of students on the board
     * @param arrPositionStudents
     */
    public void setArrPositionStudents(int[] arrPositionStudents) {
        this.arrPositionStudents = arrPositionStudents;
    }

    /**
     * This method sets the array of entrance of students.
     * @param arrEntranceStudents The array of entrance of students.
     */
    public void setArrEntranceStudents(Student[] arrEntranceStudents) {
        this.arrEntranceStudents = arrEntranceStudents;
    }

    /**
     * This method sets the array of professors on the board.
     * @param arrProfessors The array of professors on the board.
     */
    public void setArrProfessors(boolean[] arrProfessors) {
        this.arrProfessors = arrProfessors;
    }

    /**
     * This method sets the towers on the board.
     * @param tower The number of towers on the board.
     */
    public void setTower(int tower) {
        this.tower = tower;
    }

    /**
     * This method sets the max dimension of the entrance of students.
     * @param maxEntranceStudents The number that represents the max dimension of the entrance of students.
     */
    public void setMaxEntranceStudents(int maxEntranceStudents) {
        this.maxEntranceStudents = maxEntranceStudents;
    }

    /**
     * This method returns the arrays used to keep track of the coins.
     * @return The arrays used to keep track of the coins.
     */
    public boolean[][] getTrackCoins() {
        return trackCoins;
    }

    /**
     * This method sets the arrays used to keep track of the coins.
     * @param trackCoins The arrays used to keep track of the coins.
     */
    public void setTrackCoins(boolean[][] trackCoins) {
        this.trackCoins = trackCoins;
    }

    /**
     * This method returns the tower colour on the board.
     * @return The tower colour on the board.
     */
    public int getTower() {
        return tower;
    }
}
