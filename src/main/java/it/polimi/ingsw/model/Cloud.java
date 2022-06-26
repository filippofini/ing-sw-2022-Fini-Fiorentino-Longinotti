package it.polimi.ingsw.model;

/**
 * This class represent the clouds.
 * An array of int is used to keep track of the students and their type:
 * position 0 represent the yellow... More info at {@link DiskColour}.
 */
public class Cloud {
    private int[] arr_students;
    private final int cloud_ID;

    /**
     * Constructor of the class.
     * @param cloud_ID The cloud ID.
     */
    public Cloud(int cloud_ID) {
        this.cloud_ID = cloud_ID;
        arr_students = new int[5];
    }

    /**
     * This method returns the array of students in the cloud.
     * @return The array of students in the cloud.
     */
    public int[] getArr_students() {
        return arr_students;
    }

    /**
     * This method returns the cloud ID.
     * @return The cloud ID.
     */
    public int getCloud_ID() {
        return cloud_ID;
    }

    /**
     * This method sets the array of students on the island.
     * @param arr_students The array of students on the island.
     */
    public void setArr_students(int[] arr_students) {
        this.arr_students = arr_students;
    }
}

