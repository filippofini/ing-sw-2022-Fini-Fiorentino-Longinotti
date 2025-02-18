package it.polimi.ingsw.model;

import it.polimi.ingsw.network.message.toClient.ChooseIslandOrBoardRequest;
import it.polimi.ingsw.network.message.toClient.StudentToMoveRequest;
import it.polimi.ingsw.network.message.toClient.DisplayDiningRoomColourFullRequest;
import it.polimi.ingsw.network.message.toClient.DisplayStudentChosenPreviouslyRequest;
import it.polimi.ingsw.network.server.ClientHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents the board.
 * Each student has his personal board containing an array of int representing the students (see more at: {@link DiskColour}.
 * The class includes the methods to move the students into and out of the board.
 * Each board has towers with different colours (see more at: {@link TowerColour}).
 * The towers in the board are 8 if the game has 2 or 4 players, 7 if the game has 3 players.
 */
public class Board implements Serializable {
    private final int numPlayers;
    private int numTowers;
    private int[] arrPositionStudents;
    private Student[] arrEntranceStudents;
    private boolean[] arrProfessors;
    private int tower;
    private int maxEntranceStudents;
    private boolean[][] trackCoins;
    private boolean farmerState;

    /**
     * Constructor of the class.
     * @param numOfPlayers The number of players in the game.
     * @param tower The tower colour assigned to the player and to the board.
     */
    public Board(int numOfPlayers, TowerColour tower){
        this.tower = tower.getTower_translate();
        arrPositionStudents = new int[5];
        arrProfessors = new boolean[5];
        trackCoins = new boolean[5][3];

        Arrays.fill(arrPositionStudents, 0);
        for(boolean[] row : trackCoins){
            Arrays.fill(row, false);
        }

        farmerState = false;

        numPlayers = numOfPlayers;

        if(numPlayers == 2 ){
            arrEntranceStudents = new Student[7];
            maxEntranceStudents = 7;
            numTowers = 8;
        } else if(numPlayers == 3){
            arrEntranceStudents = new Student[9];
            maxEntranceStudents = 9;
            numTowers = 6;
        }
    }

    /**
     * This method adds a professor to the board.
     * @param profColour The colour of the professor to be added.
     */
    public void addProf(DiskColour profColour){
        arrProfessors[profColour.getTranslateColour()] = true;
    }

    /**
     * This method adds a student to the board.
     * @param studentColour The colour of the student to be added.
     */
    public void addStudent(DiskColour studentColour){
        arrPositionStudents[studentColour.getTranslateColour()]++;
    }

    /**
     * This method moves students to the islands.
     * @param GS The game state.
     * @param clientHandler The client handler.
     * @return The list of students sent to an island.
     */
    public List<Student> moveEntranceStudents(GameState GS, ClientHandler clientHandler) {
        List<Student> studentsToIslands = new ArrayList<>();
        int studentsChosen = 0;

        // We repeat the "choose and place student" flow until we've placed (numPlayers + 1) students.
        while (studentsChosen < (numPlayers + 1)) {
            clientHandler.sendMessageToClient(new StudentToMoveRequest(this));

            int chosenIndex = clientHandler.getStudToMove();
            Student chosenStudent = arrEntranceStudents[chosenIndex];

            // If the student hasn’t already been moved
            if (!chosenStudent.getIsChosen()) {
                clientHandler.sendMessageToClient(new ChooseIslandOrBoardRequest(this, chosenIndex));
                int placementChoice = clientHandler.getPos();

                switch (placementChoice) {
                    case 0: // Place student in dining
                        if (canPlaceStudentInDining(chosenStudent.getColor())) {
                            placeStudentInDining(chosenStudent);
                            studentsChosen++;
                            updateProfessors(GS);
                        } else {
                            // Dining color is full
                            clientHandler.sendMessageToClient(
                                    new DisplayDiningRoomColourFullRequest(this, chosenIndex)
                            );
                        }
                        break;

                    case 1: // Send student to island
                        studentsToIslands.add(new Student(chosenStudent.getEnumColour()));
                        chosenStudent.chosen();
                        studentsChosen++;
                        break;

                    default:
                        // You may show some error handling/logging here if needed.
                        break;
                }
            } else {
                // The chosen student had already been moved in a previous iteration
                clientHandler.sendMessageToClient(
                        new DisplayStudentChosenPreviouslyRequest(this, chosenIndex)
                );
            }
        }

        // If the "farmer" effect was active, disable it after these placements
        if (farmerState) {
            farmerState = false;
        }

        return studentsToIslands;
    }

    /**
     * Checks if the student’s color can still be placed in the dining area.
     * For example, there's a limit of 10 students of the same color in dining.
     */
    private boolean canPlaceStudentInDining(int colorIndex) {
        return arrPositionStudents[colorIndex] < 10;
    }

    /**
     * Increments the board’s count of students for the chosen color, marks the
     * student as “Chosen,” and triggers coin earning.
     */
    private void placeStudentInDining(Student chosenStudent) {
        arrPositionStudents[chosenStudent.getColor()]++;
        coinsEarned();
        chosenStudent.chosen();
    }

    /**
     * After a student is placed in dining, we update the professor ownership
     * depending on whether the Farmer card is active or not.
     */
    private void updateProfessors(GameState GS) {
        Board[] boards = GS.getGameTable().getBoards();
        int currentPlayer = GS.getCurrPlayer();

        for (int colorIndex = 0; colorIndex < arrProfessors.length; colorIndex++) {
            // Check if any board already owns the professor of this color
            boolean isProfessorOwnedBySomeone = false;
            for (Board b : boards) {
                if (b.getArrProfessors()[colorIndex]) {
                    isProfessorOwnedBySomeone = true;
                    break;
                }
            }

            // Attempt to assign (or reassign) the professor to the current player
            assignProfessorIfEligible(GS, colorIndex, isProfessorOwnedBySomeone, currentPlayer);
        }

        // If the Farmer effect was active, it only applies for this single update
        // (already turned off at the end of moveEntranceStudents).
    }

    /**
     * Assigns the professor of a particular color to the current player if they
     * meet the conditions. Uses Farmer-state logic to decide the comparison rule.
     */
    private void assignProfessorIfEligible(
            GameState GS,
            int colorIndex,
            boolean isProfessorOwnedBySomeone,
            int currentPlayer
    ) {
        Board[] boards = GS.getGameTable().getBoards();
        int currentColorCount = boards[currentPlayer].getArrPositionStudents()[colorIndex];

        for (int i = 0; i < boards.length; i++) {
            if (i == currentPlayer) {
                continue;
            }

            int otherColorCount = boards[i].getArrPositionStudents()[colorIndex];
            boolean otherHasProfessor = boards[i].getArrProfessors()[colorIndex];

            if (farmerState) {
                // Farmer effect: >= to steal or gain if no one has it
                if ((currentColorCount >= otherColorCount && otherHasProfessor)
                        || (!isProfessorOwnedBySomeone && currentColorCount >= otherColorCount)) {
                    boards[currentPlayer].setProfessor(colorIndex, true);
                    boards[i].setProfessor(colorIndex, false);
                }
            } else {
                // Normal effect: > to steal or gain if no one has it
                if ((currentColorCount > otherColorCount && otherHasProfessor)
                        || (!isProfessorOwnedBySomeone && currentColorCount > otherColorCount)) {
                    boards[currentPlayer].setProfessor(colorIndex, true);
                    boards[i].setProfessor(colorIndex, false);
                }
            }
        }
    }

    /**
     * This method checks if coins can be earned.
     * @return The coins earned this turn, to sum with current coins each turn.
     */
    public int coinsEarned() {
        int coins = 0;
        for (int i = 0; i < 5; i++) {
            // j goes from 0 to 2, corresponding to thresholds of 1, 2, and 3 respectively
            for (int j = 0; j < 3; j++) {
                if (arrPositionStudents[i] / 3 == (j + 1) && !trackCoins[i][j]) {
                    coins++;
                    trackCoins[i][j] = true;
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
    public int getNumTowers() {
        return numTowers;
    }

    /**
     * This method sets the number of towers on the board.
     * @param n_towers The number of towers on the board.
     */
    public void setNumTowers(int n_towers) {
        this.numTowers = n_towers;
    }

    /**
     * This method sets the array of students on the board
     * @param arrPositionStudents The array of students on the board.
     */
    public void setArrPositionStudents(int[] arrPositionStudents) {
        this.arrPositionStudents = arrPositionStudents;
    }

    /**
     * This method sets the array of entrance of students.
     * @param arrEntranceStudents The array of entrance of students.
     */
    public void setArrEntranceStudents(Student arrEntranceStudents, int pos) {
        this.arrEntranceStudents[pos] = arrEntranceStudents;
    }

    /**
     * This method sets the array of professors on the board.
     * @param arrProfessors The array of professors on the board.
     */
    public void setArrProfessors(boolean[] arrProfessors) {
        this.arrProfessors = arrProfessors;
    }

    /**
     * This method sets the professor at arrProfessors[index] value.
     * @param index The Index of the professor.
     * @param state If the professor is on the board or not.
     */
    public void setProfessor(int index, boolean state){
        getArrProfessors()[index]=state;
    }

    /**
     * This method sets the towers on the board.
     * @param tower The number of towers on the board.
     */
    public void setTower(int tower) {
        this.tower = tower;
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

    /**
     * This method returns the array of students to be moved to the entrance.
     * @return The array of students to be moved to the entrance.
     */
    public Student[] getArrEntranceStudents() {
        return arrEntranceStudents;
    }

    /**
     * This method adds a student to the board
     * @param index The index of the array of student to be added.
     */
    public void setOneStudent(int index){
        arrPositionStudents[index]++;
    }

    /**
     * This method adds a student to the entrance
     * @param Cloud_Students The array that contains the students on the clouds
     */
    public void setArrEntranceStudents(int[] Cloud_Students ){
        int pos=0;
        while(pos<5){
            if(Cloud_Students[pos]>0){
                for(int i=0;i<arrEntranceStudents.length;i++){
                    if(arrEntranceStudents[i].getIsChosen()){
                        arrEntranceStudents[i]=new Student(inverseColor(pos));
                        Cloud_Students[pos]--;
                        pos=0;
                        break;
                    }
                }
            }else{pos++;}

        }
        

    }

    /**
     * This method convert a position in the respective color
     * @param color The index of the array that represent a color
     */
    public DiskColour inverseColor(int color){
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
     * This method is used to check if the farmer character card is used, and its effect it's active.
     * @param farmerState {@code True} if the effect is active, {@code False} if not.
     */
    public void setFarmerState(boolean farmerState) {
        this.farmerState = farmerState;
    }

    /**
     * This method returns the max entrance of students.
     * @return The max entrance of students.
     */
    public int getMaxEntranceStudents() {
        return maxEntranceStudents;
    }


    /**
     * This method returns if the farmer character card has been played.
     * @return {@code True} if the farmer character card has been played.
     */
    public boolean isFarmerState() {
        return farmerState;
    }
}


