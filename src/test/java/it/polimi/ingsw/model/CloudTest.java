package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the class {@link it.polimi.ingsw.model.Cloud}.
 */
class CloudTest {

    @Test
    public void testCloud(){
        Cloud cloud = new Cloud();
        assertEquals(5,Arrays.stream(cloud.getArr_students()).count());
    }

}