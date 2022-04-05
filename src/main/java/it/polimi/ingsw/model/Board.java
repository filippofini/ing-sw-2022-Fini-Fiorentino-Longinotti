package it.polimi.ingsw.model;

public class Board {
    private int[] arrPositionStudents;
    private Student[] arrEntranceStudents;
    private boolean[] arrProfessors;
    private int tower;
    //need to know how many players for number of towers && PlayerID
    public Board(int numOfPlayers, int playerID){
        arrPositionStudents = new int[5];
        arrProfessors = new boolean[5];
        //for now with if, maybe later will become switch case
        // need re-check values
        // remember that now there is the space for the students, not the istances of the students!

        if(numOfPlayers == 2 ){
            arrEntranceStudents = new Student[7];
            tower = 8;
        }
        else if(numOfPlayers == 3){
            arrEntranceStudents = new Student[9];
            tower = 6;
        }
        //I had supposed that the squads where players(1-2) && players(3-4), if not it will need changes!
        else if(numOfPlayers == 4){
            arrEntranceStudents = new Student[7];
            tower = 0;
            if((playerID == 1 || playerID == 3))
            tower = 8;
        }
    }
    public void add_prof(Disk_colour profColour){
        arrProfessors[profColour.getTranslateColour()]=true;
    }
    public void add_student(Disk_colour studentColour){
        arrPositionStudents[studentColour.getTranslateColour()]++;

    }

    public boolean[] getArrProfessors() {
        return arrProfessors;
    }
}
