package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class IslandTest {


    private Board[] boards = new Board[4];


    @Test
    public void testIslandID() {

        Island island = new Island(boards,1, Tower_colour.STARTER);
        assertEquals(1,island.getIsland_ID());

        }

    @Test
    public void testBoards() {
        Island island = new Island(boards,1, Tower_colour.STARTER);
        assertArrayEquals(boards,island.getBoards());
    }

    @Test
   void testCheck_controller() {
    Island island = new Island(boards, 1, Tower_colour.STARTER);
        island.setPlayer_controller(1);
        assertEquals(1,island.getPlayer_controller());
    }


    @Test
    void testCalculate_influence() {
        boolean[] Arr_prof = {true,true,false,false,false};
        int[] Arr_stud = {7,2,4,5,4};
        int tower = 2;
        Island island = new Island(boards,1,Tower_colour.STARTER);
        boards[1] = new Board(2,1,Tower_colour.STARTER);
        boards[1].setArrProfessors(Arr_prof);
        island.setArr_students(Arr_stud);
        island.setPlayer_controller(1);
        island.setInfluence_controller(7);
        island.setTower(tower);


        assertTrue(island.calculate_influence(1,boards));


    }



    @Test
    void testMotherNatureTrue() {
        Island island = new Island(boards,1, Tower_colour.STARTER);
        island.setMother_nature(true);
        assertTrue(island.isMother_nature());
    }

}