package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.toServer.positionToMoveResponse;

public class BoardCLI {
    public static void studentToMoveRequest(Client client, Board board) {
        boolean validChoice = true;
        int choiceStudent;
        System.out.println("choose the number of one of the students to move:\n");
        displayEntranceStudents(board);
        do {
            choiceStudent = InputParser.getInt();
            if (choiceStudent < board.getMaxEntranceStudents()) {
                validChoice = false;
            } else {
                System.out.println("Number not valid,please choose a number from the list");
            }
        } while (validChoice);

        client.sendMessageToServer(new studentToMoveResponse(choiceStudent));

    }


    public static void positionToMoveRequest(Client client, Board board, int choiceStudent) {
        boolean validChoice = true;
        int choicePosition;


        if (board.getArrEntranceStudents()[choiceStudent].getIsChosen() == false) {
            System.out.println("where do you want to move the student?:\n");
            System.out.println("Dining Room[0] Island[1]");
            do {
                choicePosition = InputParser.getInt();
                if (choiceStudent < 2) {
                    validChoice = false;
                } else {
                    System.out.println("Number not valid,please choose a number from the list");
                }

            } while (validChoice);
            client.sendMessageToServer(new positionToMoveResponse(choicePosition));

        }
    }

    public static void displayDiningRoomColourFull( Board board,int choiceStudent) {
        System.out.println("table of colour:"+board.getArrEntranceStudents()[choiceStudent].getEnumColour() +" is full, please choose another student");

    }
    public static void displayStudentChosenPreviously( Board board,int choiceStudent) {
        System.out.println("Student chosen previously,please choose another student\n");
    }
    public static void displayEntranceStudents( Board board) {
        for (int i = 0; i < board.getMaxEntranceStudents() - 1; i++) {
            System.out.println(board.getArrEntranceStudents()[i].getEnumColour() + "[" + (i) + "] ");
        }
        System.out.println(board.getArrEntranceStudents()[board.getMaxEntranceStudents() - 1].getEnumColour() + "[" + (board.getMaxEntranceStudents() - 1) + "]\n");
    }
}