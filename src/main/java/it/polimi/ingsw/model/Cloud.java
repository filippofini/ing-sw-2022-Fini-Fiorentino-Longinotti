package it.polimi.ingsw.model;

public class Cloud {

    private int[] arr_students;

    //the number of players affects the number of students on each island
    public Cloud(int numofPlayers) {

        if (numofPlayers == 2 || numofPlayers== 4) {
            arr_students = new int[3];
        }

        if (numofPlayers == 3) {
            arr_students = new int[4];
        }
    }

    public void add_to_cloud(){
        //TODO: implement method to add students to the clouds
    }

    public int[] getArr_students() {
        return arr_students;
    }
}

