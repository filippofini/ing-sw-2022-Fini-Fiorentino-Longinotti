package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.message.toClient.*;
import it.polimi.ingsw.network.server.ClientHandler;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class represents the controller of the turn. It manages the planning phase and the action phase of each player.
 * It controls and calculates the player order for the next round.
 */
public class TurnController {

    private final int[] playerOrder;
    private final GameState gameState;
    private final GameTable gameTable;
    private final int numPlayers;
    List<Player> playersList;
    CharacterCard playedCharacterCard;
    List<ClientHandler> clientHandlerList;
    private boolean endgame;
    private final boolean expertMode;

    /**
     * Constructor of the class.
     * @param numPlayers The number of players in the game.
     * @param names The strings containing the names of the players.
     * @param wizard The array of int containing the numbers representing the wizards.
     * @param expert_mode {@code True} if expert mode is enabled, {@code False} if not.
     * @param playersList The list of player for turn order.
     */
    public TurnController(
            int numPlayers,
            String[] names,
            int[] wizard,
            boolean expert_mode,
            List<Player> playersList,
            List<ClientHandler> clientHandlers
    ) {
        endgame = false;
        this.expertMode = expert_mode;
        this.numPlayers = numPlayers;
        this.playersList = playersList;
        playerOrder = new int[numPlayers];
        this.clientHandlerList = clientHandlers;
        for (int i = 0; i < numPlayers; i++) {
            playerOrder[i] = i;
        }
        gameState = new GameState(
                numPlayers,
                names,
                wizard,
                0,
                this.playersList
        );
        gameTable = gameState.getGameTable();
    }

    /**
     * This method defines the planning phase by calculating the order of player
     * and replenishing the clouds at the beginning of the round
     */
    public void planningPhaseGeneral() {
        gameTable.replenishClouds(this);
        for (int i = 0; i < numPlayers; i++) {
            planningPhasePersonal(playerOrder[i]);
        }
        calculatePlayerOrder();
    }

    /**
     * This method represent the planning phase of the current player. Here the player decides which assistance card
     *  he wants to play.
     * @param i The ID of the player.
     */
    public void planningPhasePersonal(int i) {
        if (!playersList.get(i).getDeck().getCards().isEmpty()) {
            clientHandlerList
                    .get(i)
                    .sendMessageToClient(
                            new ChooseAssistantCardRequest(
                                    playersList.get(i),
                                    gameTable
                            )
                    );
            gameTable.chooseAssistant(
                    playersList.get(i),
                    clientHandlerList.get(i).getAssistantCardChosen()
            );
        } else {
            endgame = true;
        }
    }

    /**
     * This method represents the action phase of the round. It moves the students to the islands, moves mother nature
     * and checks if island can be conquered and merged.
     * It's also used to play character cards.
     */
    public void actionPhase() {
        for (int roundIndex = 0; roundIndex < numPlayers; roundIndex++) {
            if (endgame) {
                break;
            }

            gameState.setCurrPlayer(playerOrder[roundIndex]);

            // Current handler and player references
            ClientHandler currentHandler = clientHandlerList.get(playerOrder[roundIndex]);
            Player currentPlayer = playersList.get(playerOrder[roundIndex]);

            // Expert mode: allow using a Character Card
            if (expertMode) {
                handleCharacterCardUsage(currentHandler, currentPlayer, roundIndex);
            }

            // Move students from entrance to islands
            moveStudentsToIslands(currentHandler, roundIndex);

            // Display island info after students have moved
            displayIslandsInfo(currentHandler);

            // Move mother nature
            moveMotherNature(currentHandler, currentPlayer);

            // Check if any tower was conquered or if the game ends
            checkTowerConquest(playerOrder[roundIndex]);

            if (endgame) {
                break;
            }

            // Merge islands if needed
            mergeIslands(roundIndex);

            if (endgame) {
                break;
            }

            // Choose a cloud to refill the entrance
            chooseCloud(currentHandler, roundIndex);

            // Reset any temporary effects from certain character cards
            resetTemporaryCharacterEffects(currentHandler);
        }
    }

    /**
     * Handles the optional use of a Character Card in expert mode.
     */
    private void handleCharacterCardUsage(ClientHandler currentHandler, Player currentPlayer, int playerIndex) {
        currentHandler.sendMessageToClient(new UseCharacterCardRequest());
        if (!currentHandler.isCharacterCardUsed()) {
            return;
        }

        // Ask player which character card to use
        currentHandler.sendMessageToClient(
                new ChooseCharacterCardRequest(currentPlayer, gameTable.getCharacterCards())
        );
        CharacterCard chosenCharacterCard = gameTable.getCharacterCards()[currentHandler.getChCardUsed()];

        if (!currentHandler.isCharacterCardUsable()) {
            return;
        }

        // Apply effect based on the Character Card’s ID
        switch (chosenCharacterCard.getIDCode()) {
            case 1:
                handleCardId1(chosenCharacterCard, currentHandler, playerIndex);
                break;
            case 2:
            case 4:
            case 6:
            case 8:
                chosenCharacterCard.effect(getGameState());
                break;
            case 3:
            case 5:
                handleCardId3or5(chosenCharacterCard, currentHandler);
                break;
            case 11:
                handleCardId11(chosenCharacterCard, currentHandler, playerIndex);
                break;
            default:
                // No default action unless needed
                break;
        }
        playedCharacterCard = chosenCharacterCard;
    }

    /**
     * Moves a set of students from the entrance to the islands.
     */
    private void moveStudentsToIslands(ClientHandler currentHandler, int playerIndex) {
        List<Student> studentsToIsland = gameTable
                .getBoards()[playerOrder[playerIndex]]
                .moveEntranceStudents(gameState, currentHandler);

        for (Student student : studentsToIsland) {
            currentHandler.sendMessageToClient(
                    new ChooseIslandRequest(gameTable.getIslands(), student)
            );
            int chosenIsland = currentHandler.getIslandToMove();
            gameTable.getIslands().get(chosenIsland).addStudents(student);
        }
    }

    /**
     * Displays the relevant island information to the current client.
     */
    private void displayIslandsInfo(ClientHandler currentHandler) {
        for (int j = 0; j < gameTable.getHowManyLeft(); j++) {
            currentHandler.sendMessageToClient(
                    new DisplayIslandInfoRequest(gameTable.getIslands().get(j), j)
            );
        }
    }

    /**
     * Moves Mother Nature by the amount specified by the current handler.
     */
    private void moveMotherNature(ClientHandler currentHandler, Player currentPlayer) {
        currentHandler.sendMessageToClient(
                new MoveMnRequest(gameTable.getMotherNaturePos(), currentPlayer)
        );
        gameTable.moveMotherNature(currentHandler.getMotherNatureMovement());
    }

    /**
     * Checks if a tower was placed on the island Mother Nature landed on
     * and handles a potential endgame state.
     */
    private void checkTowerConquest(int playerIndex) {
        boolean didIslandHaveTower = gameTable.getIslands()
                .get(gameTable.getMotherNaturePos())
                .calculateInfluence(playerIndex, gameTable.getBoards());

        // didIslandHaveTower == false means the island was just conquered
        if (!didIslandHaveTower) {
            updateTowerCount(playerIndex);
        }
    }

    /**
     * Decrements the tower count of the current player's board (or team in 4-player mode).
     */
    private void updateTowerCount(int playerIndex) {
        if (numPlayers == 4) {
            // Team 1: players 1 & 2, Team 2: players 3 & 4
            if ((playerOrder[playerIndex] == 1 || playerOrder[playerIndex] == 2)
                    && gameTable.getBoards()[1].getNumTowers() > 0) {

                gameTable.getBoards()[1].setNumTowers(
                        gameTable.getBoards()[1].getNumTowers() - 1
                );
                if (gameTable.getBoards()[1].getNumTowers() == 0) {
                    endgame = true;
                }
            } else if ((playerOrder[playerIndex] == 3 || playerOrder[playerIndex] == 4)
                    && gameTable.getBoards()[3].getNumTowers() > 0) {

                gameTable.getBoards()[3].setNumTowers(
                        gameTable.getBoards()[3].getNumTowers() - 1
                );
                if (gameTable.getBoards()[3].getNumTowers() == 0) {
                    endgame = true;
                }
            }
        } else {
            Board currentBoard = gameTable.getBoards()[playerOrder[playerIndex]];
            if (currentBoard.getNumTowers() > 0) {
                currentBoard.setNumTowers(currentBoard.getNumTowers() - 1);
                if (currentBoard.getNumTowers() == 0) {
                    endgame = true;
                }
            }
        }
    }

    /**
     * Merges islands if they belong to the same team/player and checks if the game ends
     * due to a reduced island count (3 or fewer).
     */
    private void mergeIslands(int playerIndex) {
        gameTable.merge(gameTable.getMotherNaturePos(), playerOrder[playerIndex], gameTable.getBoards());
        if (gameTable.getIslands().size() == 3) {
            endgame = true;
        }
    }

    /**
     * Chooses a cloud and refills the player's entrance with the students from that cloud.
     */
    private void chooseCloud(ClientHandler currentHandler, int playerIndex) {
        Cloud chosenCloud = gameTable.chooseCloud(currentHandler);
        int[] cloudStudents = chosenCloud.getArrStudents();
        gameTable.getBoards()[playerOrder[playerIndex]].setArrEntranceStudents(cloudStudents);
    }

    /**
     * Resets any temporary influence modifications from certain Character Cards.
     * (Specifically ID code 8 or 6 from the original logic.)
     */
    private void resetTemporaryCharacterEffects(ClientHandler currentHandler) {
        if (currentHandler.isCharacterCardUsed() && playedCharacterCard != null) {
            switch (playedCharacterCard.getIDCode()) {
                case 8:
                    // Remove extra influence from all islands
                    for (Island island : gameTable.getIslands()) {
                        island.setExtraInfluence(0);
                    }
                    break;
                case 6:
                    // Re-include towers in influence calculation
                    for (Island island : gameTable.getIslands()) {
                        island.setIncludeTowers(true);
                    }
                    break;
                default:
                    // No reset needed for other IDs or no effect
                    break;
            }
        }
    }

/* -------------------------------------------------------------------------
   Below are helper methods to handle specific Character Card IDs. These are
   extracted from the large switch block in handleCharacterCardUsage().
   Adjust them as needed if your class structure or data differ.
   ------------------------------------------------------------------------- */

    /**
     * Handles the effect logic for Character Card with ID 1.
     */
    private void handleCardId1(CharacterCard card, ClientHandler handler, int playerIndex) {
        Student[] tempCCStud = new Student[4];
        for (int l = 0; l < 4; l++) {
            tempCCStud[l] = new Student(
                    gameTable.getBoards()[playerIndex].inverseColor(card.getStudents()[l])
            );
        }
        handler.sendMessageToClient(new ShowStudentRequest(tempCCStud));
        card.setChosenStudent(handler.getMonkStudent());
        handler.sendMessageToClient(new HeraldIslandRequest(gameTable.getIslands()));
        card.setIndexTo(handler.getHeraldIsland());
        card.effect(getGameState());
    }

    /**
     * Handles the effect logic for Character Cards with ID 3 or 5.
     */
    private void handleCardId3or5(CharacterCard card, ClientHandler handler) {
        handler.sendMessageToClient(new HeraldIslandRequest(gameTable.getIslands()));
        card.setIndexTo(handler.getHeraldIsland());
        card.effect(getGameState());
    }

    /**
     * Handles the effect logic for Character Card with ID 11.
     */
    private void handleCardId11(CharacterCard card, ClientHandler handler, int playerIndex) {
        Student[] tempCCStud = new Student[4];
        for (int l = 0; l < 4; l++) {
            tempCCStud[l] = new Student(
                    gameTable.getBoards()[playerIndex].inverseColor(card.getStudents()[l])
            );
        }
        handler.sendMessageToClient(new ShowStudentRequest(tempCCStud));
        card.setChosenStudent(handler.getMonkStudent());
        card.setCurrentPlayer(playerOrder[playerIndex]);
        card.effect(getGameState());
    }

    /**
     * This method calculates the order of the players for the turn.
     * The player with the lowest value of the played assistance card starts first.
     */
    public void calculatePlayerOrder() {
        // Create a list copy of players.
        List<Player> sortedPlayers = new ArrayList<>(playersList);
        // Sort players based on the value of the chosen card.
        sortedPlayers.sort(
                Comparator.comparingInt(p -> p.getChosenCard().getValue())
        );

        // Update the playerOrder array with each player's ID in sorted order.
        for (int i = 0; i < numPlayers; i++) {
            playerOrder[i] = sortedPlayers.get(i).getPlayer_ID();
        }
    }

    /**
     * This method returns the game state.
     * @return The game state.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * This method returns the end game.
     * @return The end game.
     */
    public boolean getEndgame() {
        return endgame;
    }

    /**
     * This method sets the end game.
     * @param endgame The end game.
     */
    public void setEndgame(boolean endgame) {
        this.endgame = endgame;
    }
}