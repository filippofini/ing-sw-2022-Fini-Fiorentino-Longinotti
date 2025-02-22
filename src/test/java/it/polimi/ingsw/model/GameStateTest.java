package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class GameStateTest {

    // Helper method to create a list of players for tests
    private List<Player> createPlayers(int numPlayers) {
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            players.add(new Player("Player" + i, i, TowerColour.GREY, i));
        }
        return players;
    }

    @Test
    public void testInitialization() {
        List<Player> playersList = createPlayers(2);
        GameState gameState = new GameState(2, new String[]{"Player1", "Player2"}, 0, playersList);

        // Verify that game table is created
        assertNotNull(gameState.getGameTable(), "Game table should be initialized.");
        // Verify that players array length equals the expected number
        assertEquals(2, gameState.getPlayers().length, "Players array size should be 2.");
        // Verify that players list is preserved
        assertEquals(playersList, gameState.getPlayersList(), "Players list should match the provided list.");
    }

    @Test
    public void testCurrPlayerGetterSetter() {
        List<Player> playersList = createPlayers(2);
        GameState gameState = new GameState(2, new String[]{"Player1", "Player2"}, 0, playersList);

        // Verify initial current player index
        assertEquals(0, gameState.getCurrPlayer(), "Initial current player index should be 0.");
        // Change the current player and check the update
        gameState.setCurrPlayer(1);
        assertEquals(1, gameState.getCurrPlayer(), "Current player index should be updated to 1.");
    }

    @Test
    public void testPlayersConsistency() {
        List<Player> playersList = createPlayers(2);
        GameState gameState = new GameState(2, new String[]{"Player1", "Player2"}, 0, playersList);

        // Verify each player in the array equals the corresponding element in the list
        Player[] playersArray = gameState.getPlayers();
        assertEquals(playersList.get(0), playersArray[0], "First player should be consistent.");
        assertEquals(playersList.get(1), playersArray[1], "Second player should be consistent.");
    }

    @Test
    public void testGameTableUniquenessAcrossInstances() {
        List<Player> playersList1 = createPlayers(2);
        GameState gameState1 = new GameState(2, new String[]{"Player1", "Player2"}, 0, playersList1);

        List<Player> playersList2 = createPlayers(2);
        GameState gameState2 = new GameState(2, new String[]{"Player1", "Player2"}, 0, playersList2);

        // Even if constructed with the same parameters, each game state's game table should be a unique instance
        assertNotSame(gameState1.getGameTable(), gameState2.getGameTable(),
            "Different GameState instances should have unique game tables.");
    }
}