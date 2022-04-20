package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class IslandTest {


    private Board[] boards;


    @Test
    public void testIslandID() {

        Island island = new Island(1,boards,1);
        assertEquals(1,island.getIsland_ID());

        }

    @Test
    public void testCurrentPlayer() {
        Island island = new Island(1,boards,1);
        assertEquals(1,island.getCurrent_player());

    }

    @Test
    public void testBoards() {
        Island island = new Island(1,boards,1);
        assertArrayEquals(boards,island.getBoards());
    }

    @Test
   void testCheck_controller() {
        Island island = new Island(2,boards, 1);
        island.setPlayer_controller(1);
        assertEquals(1,island.getPlayer_controller());
    }


    //@Test
    //void calculate_influence() {
    //}


    @Test
    void testMotherNatureTrue() {
        Island island = new Island(1,boards,1);
        island.setMother_nature(true);
        assertTrue(island.isMother_nature());
    }

}