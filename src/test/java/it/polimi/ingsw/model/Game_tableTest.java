package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//THESE TESTS DON'T WORK
//The reason is not known
class Game_tableTest {

    @Test
    public void testCheck_if_playable(){
        Tower_colour tower = Tower_colour.BLACK;
        Game_table game_table = new Game_table(2,new Turn(), new Player[]{new Player("ff", 0, Tower_colour.GREY, 1), new Player("gg", 1, Tower_colour.BLACK,2)});
        Assistance_card[] assistance_card = new Assistance_card[]{Assistance_card.CAT, Assistance_card.BULLDOG};

        Assistance_card played = Assistance_card.CAT;

        assertFalse(game_table.check_if_playable(played));
    }


    @Test
    public void testSet_mother_nature_start(){
        Game_table game_table = new Game_table(2,new Turn(), new Player[]{new Player("ff", 0, Tower_colour.GREY, 1), new Player("gg", 1, Tower_colour.BLACK,2)});

    }

}