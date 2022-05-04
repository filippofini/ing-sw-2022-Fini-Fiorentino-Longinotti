package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the class {@link it.polimi.ingsw.model.Island}.
 */
class IslandTest {


    private final Board[] boards1 = new Board[4];
    private final Board[] boards2 = new Board[3];

    private Student transfer;

    @Test
    public void testIslandID() {

        Island island = new Island(boards1,1, Tower_colour.STARTER);
        assertEquals(1,island.getIsland_ID());

        }

    @Test
    public void testBoards() {
        Island island = new Island(boards1,1, Tower_colour.STARTER);
        assertArrayEquals(boards1,island.getBoards());
    }

    @Test
   void testCheck_controller() {
    Island island = new Island(boards1, 1, Tower_colour.STARTER);
        island.setPlayer_controller(1);
        assertEquals(1,island.getPlayer_controller());
    }


    @Test
    void testCalculate_influence() {
        boolean[] Arr_prof = {true,true,false,false,false};
        int[] Arr_stud = {7,2,4,5,4};
        int tower = 2;
        Island island1 = new Island(boards1,1,Tower_colour.STARTER);
        Island island2 = new Island(boards2,2,Tower_colour.STARTER);
        boards1[1] = new Board(4,1,Tower_colour.STARTER);
        boards1[3] = new Board(4,3,Tower_colour.STARTER);
        boards2[1] = new Board(3,1,Tower_colour.STARTER);
        boards2[2] = new Board(3,2,Tower_colour.STARTER);

        boards1[1].setArrProfessors(Arr_prof);
        island1.setArr_students(Arr_stud);
        island1.setPlayer_controller(1);
        island1.setInfluence_controller(7);
        island1.setTower(tower);
        assertTrue(island1.calculate_influence(1,boards1));

        boards1[1].setArrProfessors(Arr_prof);
        island1.setArr_students(Arr_stud);
        island1.setPlayer_controller(2);
        island1.setInfluence_controller(7);
        island1.setTower(tower);
        assertFalse(island1.calculate_influence(1,boards1));

        boards1[3].setArrProfessors(Arr_prof);
        island1.setArr_students(Arr_stud);
        island1.setPlayer_controller(2);
        island1.setInfluence_controller(7);
        island1.setTower(tower);
        assertFalse(island1.calculate_influence(3,boards1));

        boards2[1].setArrProfessors(Arr_prof);
        island2.setArr_students(Arr_stud);
        island2.setPlayer_controller(2);
        island2.setInfluence_controller(7);
        island2.setTower(tower);
        assertFalse(island2.calculate_influence(1,boards2));

        island1.setProhibition_card(true);
        assertTrue(island1.calculate_influence(1,boards1));
    }


    @Test
    void testMotherNatureTrue() {
        Island island = new Island(boards1,1, Tower_colour.STARTER);
        island.setMother_nature(true);
        assertTrue(island.isMother_nature());
    }

    @Test
    void testAddStudents() {
        transfer = new Student(Disk_colour.RED);
        Island island = new Island(boards1,1,Tower_colour.STARTER);
        int[] start = {1,1,1,1,1};
        island.setArr_students(start);
        int[] ar = {1,2,1,1,1};
        island.add_students(transfer);
        assertArrayEquals(ar,island.getArr_students());
    }

    @Test
    void testCheckController() {
        Island island = new Island(boards1,1,Tower_colour.STARTER);
        island.setPlayer_controller(1);
        assertEquals(1,island.check_controller());
    }

    @Test
    void testIsProhibition_card() {
        Island island = new Island(boards1,1,Tower_colour.STARTER);
        assertFalse(island.isProhibition_card());
    }

    @Test
    void testGetInfluenceController() {
        Island island = new Island(boards1,1,Tower_colour.STARTER);
        assertEquals(0,island.getInfluence_controller());
    }

    @Test
    void testAddTower() {
        Island island = new Island(boards1,1,Tower_colour.STARTER);
        boards1[1]= new Board(2,1,Tower_colour.BLACK);
        island.add_tower(1);
        assertEquals(2,island.getTower());



    }

}