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

        Island island = new Island(boards1,1, TowerColour.STARTER);
        assertEquals(1,island.getIsland_ID());

        }

    @Test
    public void testBoards() {
        Island island = new Island(boards1,1, TowerColour.STARTER);
        assertArrayEquals(boards1,island.getBoards());
    }

    @Test
   void testCheck_controller() {
    Island island = new Island(boards1, 1, TowerColour.STARTER);
        island.setPlayerController(1);
        assertEquals(1,island.getPlayerController());
    }

    @Test
    void testCalculate_influence1() {
        boolean[] Arr_prof = {true,true,false,false,false};
        int[] Arr_stud = {7,2,4,5,4};
        int tower = 2;
        Island island1 = new Island(boards1,1, TowerColour.STARTER);
        boards1[1] = new Board(4, TowerColour.STARTER);

        boards1[1].setArrProfessors(Arr_prof);
        island1.setArrStudents(Arr_stud);
        island1.setPlayerController(1);
        island1.setInfluenceController(7);
        island1.setTower(tower);
        assertTrue(island1.calculateInfluence(1,boards1));
    }

    @Test
    void testCalculate_influence2() {

        boolean[] Arr_prof = {true,true,false,false,false};
        int[] Arr_stud = {7,2,4,5,4};
        int tower = 2;
        Island island1 = new Island(boards1,1, TowerColour.STARTER);
        boards1[1] = new Board(3, TowerColour.STARTER);



        boards1[1].setArrProfessors(Arr_prof);
        island1.setArrStudents(Arr_stud);
        island1.setPlayerController(2);
        island1.setInfluenceController(7);
        island1.setTower(tower);
        assertFalse(island1.calculateInfluence(1,boards1));

    }

    @Test
    void testCalculate_influence3() {

        boolean[] Arr_prof = {true,true,false,false,false};
        int[] Arr_stud = {7,2,4,5,4};
        int tower = 2;
        Island island1 = new Island(boards1,1, TowerColour.STARTER);
        boards1[3] = new Board(4, TowerColour.STARTER);

        boards1[3].setArrProfessors(Arr_prof);
        island1.setArrStudents(Arr_stud);
        island1.setPlayerController(2);
        island1.setInfluenceController(7);
        island1.setTower(tower);
        assertFalse(island1.calculateInfluence(3,boards1));


    }

    @Test
    void testCalculate_influence4() {

        boolean[] Arr_prof = {true,true,false,false,false};
        int[] Arr_stud = {7,2,4,5,4};
        int tower = 2;
        Island island2 = new Island(boards2,2, TowerColour.STARTER);
        boards2[1] = new Board(3, TowerColour.STARTER);
        boards2[2] = new Board(3, TowerColour.STARTER);

        boards2[1].setArrProfessors(Arr_prof);
        island2.setArrStudents(Arr_stud);
        island2.setPlayerController(2);
        island2.setInfluenceController(7);
        island2.setTower(tower);
        assertFalse(island2.calculateInfluence(1,boards2));

    }

    @Test
    void testCalculate_influence5() {

        boolean[] Arr_prof = {true,true,false,false,false};
        int[] Arr_stud = {7,2,4,5,4};
        int tower = 2;
        Island island1 = new Island(boards1,1, TowerColour.STARTER);

        island1.setProhibitionCard(true);
        assertTrue(island1.calculateInfluence(1,boards1));
    }

    @Test
    void testMotherNatureTrue() {
        Island island = new Island(boards1,1, TowerColour.STARTER);
        island.setMotherNature(true);
        assertTrue(island.isMotherNatureHere());
    }

    @Test
    void testAddStudents() {
        transfer = new Student(DiskColour.RED);
        Island island = new Island(boards1,1, TowerColour.STARTER);
        int[] start = {1,1,1,1,1};
        island.setArrStudents(start);
        int[] ar = {1,2,1,1,1};
        island.addStudents(transfer);
        assertArrayEquals(ar,island.getArrStudents());
    }

    @Test
    void testCheckController() {
        Island island = new Island(boards1,1, TowerColour.STARTER);
        island.setPlayerController(1);
        assertEquals(1,island.checkController());
    }

    @Test
    void testIsProhibition_card() {
        Island island = new Island(boards1,1, TowerColour.STARTER);
        assertFalse(island.isProhibitionCard());
    }

    @Test
    void testGetInfluenceController() {
        Island island = new Island(boards1,1, TowerColour.STARTER);
        assertEquals(0,island.getInfluenceController());
    }

    @Test
    void testAddTower() {
        Island island = new Island(boards1,1, TowerColour.STARTER);
        boards1[1]= new Board(2, TowerColour.BLACK);
        island.addTower(1);
        assertEquals(2,island.getTower());

    }

    @Test
    void testIncrementPos() {
        Island island = new Island(boards1,1, TowerColour.STARTER);
        int[] Arr_stud = {7,2,4,5,4};
        island.setArrStudents(Arr_stud);
        island.incrementPos(0);
        assertEquals(8,Arr_stud[0]);
    }

    @Test
    void testSetOneStudent() {

    }
}