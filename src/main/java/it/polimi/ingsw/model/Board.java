package it.polimi.ingsw.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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
    //return students to send in islands.

    public List<Student> MoveEntranceStudents(){
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

            if (arrEntranceStudents[choiceStudent].getisChosen() == false) {

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
            else if (arrEntranceStudents[choiceStudent].getisChosen() == true){
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

    public boolean[] getArrProfessors() {
        return arrProfessors;
    }

    public int getMaxEntranceStudents() {
        return maxEntranceStudents;
    }

    public int[] getArrPositionStudents() {
        return arrPositionStudents;
    }
    //TODO: check coins, i want to create an array of bool to keep track the coins that i have obtained or not
}
