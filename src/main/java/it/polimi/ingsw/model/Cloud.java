package it.polimi.ingsw.model;

/**
 * This class represent the clouds.
 * An array of int is used to keep track of the students and their type:
 * Pos. 0 represent the yellow... More info at {@link it.polimi.ingsw.model.Disk_colour}.
 *
 */
public class Cloud {
    private int[] arr_students;

    /**
     * Constructor of the class.
     */
    public Cloud() {
        arr_students = new int[5];
    }

    /**
     * This method returns the array of students in the cloud.
     * @return The array of students in the cloud.
     */
    public int[] getArr_students() {
        return arr_students;
    }
}

