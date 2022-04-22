package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {

    @Test
    public void testCloud(){
        Cloud cloud = new Cloud(2);
        assertEquals(5,Arrays.stream(cloud.getArr_students()).count());
    }

}