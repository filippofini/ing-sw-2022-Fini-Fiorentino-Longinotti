package it.polimi.ingsw.model;

import java.awt.*;
import java.util.Scanner;

public class Board {
    private int[] arrPositionStudents;
    private Student[] arrEntranceStudents;
    private boolean[] arrProfessors;
    private int tower;
    private int maxEntranceStudents;
    //need to know how many players for number of towers && PlayerID
    public Board(int numOfPlayers, int playerID){
        arrPositionStudents = new int[5];
        arrProfessors = new boolean[5];
        //for now with if, maybe later will become switch case
        // need re-check values
        // remember that now there is the space for the students, not the istances of the students!

        if(numOfPlayers == 2 ){
            arrEntranceStudents = new Student[7];
            maxEntranceStudents=7;
            tower = 8;
        }
        else if(numOfPlayers == 3){
            arrEntranceStudents = new Student[9];
            maxEntranceStudents=9;
            tower = 6;
        }
        //I had supposed that the squads where players(1-2) && players(3-4), if not it will need changes!
        else if(numOfPlayers == 4){
            arrEntranceStudents = new Student[7];
            maxEntranceStudents=7;
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
    //return students to send in islands...it will become a list i think.
    //TODO: while cycles for wrong number enter

    public  Student[] MoveEntranceStudents(){
        int choiceStudent;
        int choicePosition;
        int studentsChosen=0;
        boolean flag = true;
        Student[] studentToIslands = new Student[3];
        int num_studentsToIslands =0;
        Scanner sc= new Scanner(System.in);
        System.out.println("choose the number of one of the students to move:\n");

        for (int i=0;i< maxEntranceStudents-1;i++) {
            System.out.println(arrEntranceStudents[i].getEnumColour() + "["+(i)+"] ");
        }
        System.out.println(arrEntranceStudents[maxEntranceStudents-1].getEnumColour() + "["+(maxEntranceStudents-1)+"]\n");
        choiceStudent = sc.nextInt();
        while(studentsChosen <3) {

            //TODO: cornercase dining room row full
            if (arrEntranceStudents[choiceStudent].getisChosen() == false) {
                arrEntranceStudents[choiceStudent].Chosen();
                studentsChosen++;
                System.out.println("where do you want to move the student?:\n");
                System.out.println("Dining Room[0] Island[1]");
                choicePosition = sc.nextInt();
                if(choicePosition == 0){
                    arrPositionStudents[arrEntranceStudents[choiceStudent].getColour()]++;
                }
                else if(choicePosition == 1){
                    studentToIslands[num_studentsToIslands] = arrEntranceStudents[choiceStudent];
                    num_studentsToIslands++;
                }
                System.out.println("choose the numer of one of the students to move:\n");
                choiceStudent = sc.nextInt();

            }
            else if (arrEntranceStudents[choiceStudent].getisChosen() == true){
                System.out.println("Student chosen previously, choose another student\n");
                choiceStudent = sc.nextInt();
            }
        }
        return studentToIslands;
    }

    public boolean[] getArrProfessors() {
        return arrProfessors;
    }

    public int getMaxEntranceStudents() {
        return maxEntranceStudents;
    }


    }
