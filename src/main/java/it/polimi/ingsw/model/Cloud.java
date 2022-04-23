package it.polimi.ingsw.model;

/**
 * Class cloud contains an array of int to keep track of the students and their type
 */
public class Cloud {
    private int[] arr_students;

    public Cloud() {
        arr_students = new int[5];
    }

    public int[] getArr_students() {
        return arr_students;
    }
}

