package it.polimi.ingsw.model;

public class Cloud {

    private Student[] arr_students;

    //the number of players affects the number of students on each island
    public Cloud(int numofPlayers) {

        if (numofPlayers == 2 || numofPlayers== 4) {
            arr_students = new Student[3];
        }

        if (numofPlayers == 3) {
            arr_students = new Student[4];
        }
    }
}

