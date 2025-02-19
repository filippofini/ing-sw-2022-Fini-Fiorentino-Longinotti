package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TowerColour;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the class {@link it.polimi.ingsw.model.character.Herald}.
 */
class HeraldTest {

    List<Player> lPlayers = new ArrayList<>();
    private List<Player> addLPlayers(List lPlayers){
        lPlayers.add(new Player("ff",0, TowerColour.GREY,1));
        lPlayers.add(new Player("gg",1,TowerColour.GREY,2));
        return lPlayers;
    }
    Herald herald = new Herald();
    GameState game_state = new GameState(2, new String[]{"FF", "HH"}, new int[]{0,1},0, addLPlayers(lPlayers));

    @Test
    public void testSetUses1(){
        herald.setUses();

        assertEquals(1, herald.getUses());
    }
    @Test
    public void testSetUses2(){
        herald.setUses();
        herald.setUses();

        assertNotEquals(1, herald.getUses());
    }

    @Test
    public void testID(){
        assertEquals(3,herald.getIDCode());
    }

    @Test
    public void testCost(){
        assertEquals(3, herald.getCost());
    }


    @Test
    public void testEffect(){
        herald.setIndexTo(5);
        herald.effect(game_state);

        assertEquals(false, game_state.getGameTable().getIslands().get(4).isMotherNatureHere());
    }

}