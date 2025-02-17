package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * This class represent the clouds.
 * An array of int is used to keep track of the students and their type:
 * position 0 represent the yellow... More info at {@link DiskColour}.
 */
public class Cloud implements Serializable {
    private int[] arrStudents;
    private final int cloudID;

    /**
     * Constructor of the class.
     * @param cloud_ID The cloud ID.
     */
    public Cloud(int cloud_ID) {
        this.cloudID = cloud_ID;
        arrStudents = new int[5];
    }

    /**
     * This method returns the array of students in the cloud.
     * @return The array of students in the cloud.
     */
    public int[] getArrStudents() {
        return arrStudents;
    }

    /**
     * This method returns the cloud ID.
     * @return The cloud ID.
     */
    public int getCloudID() {
        return cloudID;
    }

    /**
     * This method sets the array of students on the island.
     * @param pos The position where to put the student.
     */
    public void setArrStudents(int pos) {
        this.arrStudents[pos]++;
    }
}

