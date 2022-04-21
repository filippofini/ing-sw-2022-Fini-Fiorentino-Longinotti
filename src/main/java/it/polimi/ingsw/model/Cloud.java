package it.polimi.ingsw.model;

public class Cloud {

    private int[] arr_students;

    //the number of players affects the number of students on each island
    public Cloud(int numofPlayers) {

            arr_students = new int[5];

    }

    public void add_to_cloud(){
        //TODO: implement method to add students to the clouds
    }

    public int[] getArr_students() {
        return arr_students;
    }
}

