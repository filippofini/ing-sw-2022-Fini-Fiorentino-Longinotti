package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.model.character.*;
import it.polimi.ingsw.network.message.toClient.ChooseCloudRequest;
import it.polimi.ingsw.network.server.ClientHandler;

import java.io.Serializable;
import java.util.*;

/**
 * This class represent the game table. Here the list of islands, the list of clouds and the array of boards is created.
 * The class includes methods to move mother nature in the islands and the merge of island if possible.
 * The discard deck of assistance cards is store here and the three character cards are drawn in the beginning.
 */
public class GameTable implements Serializable {
    private final int numPlayers;
    private int islandsCounter;
    private final Board[] boards;
    private final LinkedList<Island> islands;
    private final List<Cloud> clouds;
    private List<Cloud> tempClouds;
    private int motherNaturePos;
    private int[] bag;
    private final CharacterCard[] characterCards;
    private final AssistanceCard[] discardDeck;

    /**
     * Constructor of the class.
     *
     * @param numPlayers The number of players in the game. There can be 2,3 or 4 players.
     */
    public GameTable(int numPlayers) {
        islandsCounter = 12;
        this.numPlayers = numPlayers;

        bag = new int[5];
        Arrays.fill(bag, 2);

        boards = new Board[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            boards[i] = new Board(numPlayers, TowerColour.values()[i]);
        }

        islands = new LinkedList<>();
        for (int i = 0; i < islandsCounter; i++) {
            islands.add(new Island(boards, i + 1, TowerColour.STARTER));
        }

        setMotherNatureStart();
        bagIslandStart();

        Random random = new Random();

        int studentCount = 0;
        if (numPlayers == 2) {
            studentCount = 7;
        } else if (numPlayers == 3) {
            studentCount = 9;
        }

        for (int i = 0; i < numPlayers; i++) {
            for (int j = 0; j < studentCount; j++) {
                int tempRand = random.nextInt(5);
                boards[i].setArrEntranceStudents(new Student(boards[i].inverseColor(tempRand)), j);
                bag[tempRand]--;
            }
        }

        clouds = new ArrayList<>();
        tempClouds = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            clouds.add(new Cloud(i + 1));
        }

        resetTempClouds();
        discardDeck = new AssistanceCard[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            discardDeck[i] = AssistanceCard.STARTER;
        }

        characterCards = new CharacterCard[3];

        drawThreeCharCards();
    }

    /**
     * This method checks if an assistance card is playable.
     * That means it can't be on the discard deck of any other player.
     *
     * @param chosen The assistance card chosen to be played.
     * @param deck   The deck of the player
     * @return {@code False} if card is already played, {@code True} otherwise.
     */
    public boolean checkIfPlayable(AssistanceCard chosen, Deck deck) {
        for (int i = 0; i < numPlayers; i++) {
            if (discardDeck[i].equals(chosen) && !checkOnlyPlayableCard(deck, chosen)) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method checks if an assistance card is the only card playable.
     *
     * @param chosen The assistance card chosen to be played.
     * @param deck   The deck of the player
     * @return {@code False} if card isn't the only card playable
     */
    public boolean checkOnlyPlayableCard(Deck deck, AssistanceCard chosen) {
        for (AssistanceCard card : deck.getCards()) {
            if (!card.equals(chosen)) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method lets the player choose their assistance card
     *
     * @param player          The current player that is going to choose the assistance card.
     * @param assistantChosen The chosen assistant.
     */
    public void chooseAssistant(Player player, int assistantChosen) {
        AssistanceCard choice;
        choice = player.getDeck().getCards().get(assistantChosen);
        player.setMoves(choice.getMotherNatureMovement());
        discardDeck[player.getPlayer_ID()] = choice;
        player.setChosenCard(choice);
        player.getDeck().removeUsedCard(choice);
    }

    /**
     * Returns the index of the previous island and the next island (in a circular way)
     * that can be merged with the given islandIndex. If a slot in the returned array
     * is -1, it means that island cannot be merged.
     *
     * @param islandIndex The index of the island to check
     * @return An array of two integers: [previousIslandIndex, nextIslandIndex]
     */
    private int[] checkMerge(int islandIndex) {
        int[] mergeable = {-1, -1};

        // Guard out-of-range and neutral case
        if (islandIndex < 0 || islandIndex >= islandsCounter) return mergeable;
        int currentController = islands.get(islandIndex).getPlayerController();
        if (currentController == -1) return mergeable;

        // Use modulo arithmetic to simulate a circular arrangement
        int prevIndex = (islandIndex - 1 + islandsCounter) % islandsCounter;
        int nextIndex = (islandIndex + 1) % islandsCounter;

        // Check if the same player controls the previous island
        if (islands.get(prevIndex).getPlayerController() == currentController) {
            mergeable[0] = prevIndex;
        }
        // Check if the same player controls the next island
        if (islands.get(nextIndex).getPlayerController() == currentController) {
            mergeable[1] = nextIndex;
        }

        return mergeable;
    }

    /**
     * Merges one island's students and towers into another island, if valid.
     *
     * @param destIndex The island that absorbs students/towers
     * @param srcIndex  The island that will be merged (srcIndex is removed later)
     */
    private void mergeIslandInto(int destIndex, int srcIndex) {
        if (srcIndex < 0) return;  // nothing to merge

        // Merge students
        int[] destStudents = islands.get(destIndex).getArrStudents();
        int[] srcStudents = islands.get(srcIndex).getArrStudents();
        for (int i = 0; i < destStudents.length; i++) {
            destStudents[i] += srcStudents[i];
        }
        islands.get(destIndex).setArrStudents(destStudents);

        // Merge towers
        int mergedTowers = islands.get(destIndex).getTower()
                + islands.get(srcIndex).getTower();
        islands.get(destIndex).setTower(mergedTowers);
    }

    /**
     * Attempts to merge adjacent islands (in circular fashion) with the island
     * at index islandIndex. After merging, updates the island’s influence
     * and removes any merged islands.
     *
     * @param islandIndex    The index of the island that remains after merge.
     * @param current_player The current turn’s player.
     * @param Boards         The array of boards to calculate influence.
     */
    public void merge(int islandIndex, int current_player, Board[] Boards) {
        // Which islands should we merge?
        int[] toMergeIndexes = checkMerge(islandIndex);

        // Merge island at toMergeIndexes[0] into islandIndex
        mergeIslandInto(islandIndex, toMergeIndexes[0]);
        // Merge island at toMergeIndexes[1] into islandIndex
        mergeIslandInto(islandIndex, toMergeIndexes[1]);

        // Recalculate influence
        islands.get(islandIndex).calculateInfluence(current_player, Boards);

        // Remove merged islands in a way that preserves correct indexing:
        boolean removedOne = false;

        // If the "previous" island was merged, remove it and decrement islandsCounter
        if (toMergeIndexes[0] >= 0) {
            islands.remove(toMergeIndexes[0]);
            islandsCounter--;
            removedOne = true;
        }

        // If the "next" island was merged, adjust index if we already removed one
        if (toMergeIndexes[1] >= 0) {
            // If we already removed the previous one and that previous had an index
            // smaller than the next one, the next one has shifted by -1
            int indexToRemove = toMergeIndexes[1];
            if (removedOne && toMergeIndexes[0] < toMergeIndexes[1]) {
                indexToRemove -= 1;
            }
            islands.remove(indexToRemove);
            islandsCounter--;
        }
    }

    /**
     * Moves Mother Nature by a number of moves chosen by the current player.
     *
     * @param moves Number of moves the player decides, not the max possible moves.
     */
    public void moveMotherNature(int moves) {
        islands.get(motherNaturePos).setMotherNature(false);

        // Compute the new position using modulo for wrap-around behavior
        motherNaturePos = (motherNaturePos + moves) % islandsCounter;
        islands.get(motherNaturePos).setMotherNature(true);
    }


    //Sets mother nature on a random island
    private void setMotherNatureStart() {
        Random rand = new Random();
        this.motherNaturePos = rand.nextInt(12);
        islands.get(this.motherNaturePos).setMotherNature(true);
    }

    //Puts the first students on the islands
    private void bagIslandStart() {
        Random rand = new Random();
        int excludedPos = (this.motherNaturePos + 6) % 12;
        int tempRand;

        for (int i = 0; i < 12; i++) {
            if (i != excludedPos && i != this.motherNaturePos) {
                do {
                    tempRand = rand.nextInt(5);
                } while (bag[tempRand] == 0);

                bag[tempRand]--;
                islands.get(i).incrementPos(tempRand);
            }
        }

        Arrays.fill(bag, 24);
    }

    /**
     * Refills each cloud with students.
     *
     * @param TC The turn controller.
     */
    public void replenishClouds(TurnController TC) {
        Random rand = new Random();

        // Reset cloud students
        for (Cloud cloud : clouds) {
            Arrays.fill(cloud.getArrStudents(), 0);
        }

        int studentsPerCloud = (numPlayers == 2) ? 3 : 4; // Number of students per cloud

        for (int i = 0; i < numPlayers; i++) {
            int count = 0;
            while (count < studentsPerCloud) {
                if (isBagEmpty()) {
                    TC.setEndgame(true);
                    return;
                }

                int tempRand;
                do {
                    tempRand = rand.nextInt(5);
                } while (bag[tempRand] == 0);

                clouds.get(i).setArrStudents(tempRand);
                bag[tempRand]--;
                count++;
            }
        }
    }

    /**
     * Checks if the bag is empty.
     *
     * @return true if the bag is empty, false otherwise.
     */
    private boolean isBagEmpty() {
        for (int count : bag) {
            if (count > 0) return false;
        }
        return true;
    }

    /**
     * This method lets the player choose the cloud that he wants.
     *
     * @param clientHandler The client handler.
     */
    public Cloud chooseCloud(ClientHandler clientHandler) {
        Cloud chosenCloud;

        clientHandler.sendMessageToClient(new ChooseCloudRequest(tempClouds));
        chosenCloud = tempClouds.get(clientHandler.getCloudChosen());
        tempClouds = deleteTempCloud(clientHandler.getCloudChosen());
        if (tempClouds.isEmpty()) {
            resetTempClouds();
        }
        return chosenCloud;

    }

    /**
     * Randomly draws three character cards and assigns students to special cards.
     */
    private void drawThreeCharCards() {

        // Draw students for Monk and Spoilt Princess
        int[] monkDrawn = drawStudents(4);
        int[] princessDrawn = drawStudents(4);

        // Create a deck of character cards
        List<CharacterCard> charDeck = new ArrayList<>(Arrays.asList(
                new Monk(monkDrawn),
                new Farmer(),
                new Herald(),
                new MagicMailman(),
                new HerbsGrandma(),
                new Centaur(),
                new Knight(),
                new SpoiltPrincess(princessDrawn)
        ));

        // Shuffle and pick the first 3 unique cards
        Collections.shuffle(charDeck);
        for (int i = 0; i < 3; i++) {
            characterCards[i] = charDeck.get(i);
        }

        // Return students if Monk is not drawn
        if (!containsCharacter(1)) {
            returnStudentsToBag(monkDrawn);
        }

        // Return students if Spoilt Princess is not drawn
        if (!containsCharacter(11)) {
            returnStudentsToBag(princessDrawn);
        }
    }

    /**
     * Draws a specified number of students from the bag.
     *
     * @param count Number of students to draw.
     * @return An array representing drawn students.
     */
    private int[] drawStudents(int count) {
        Random rand = new Random();
        int[] drawnStudents = new int[5];

        for (int i = 0; i < count; i++) {
            int drawStud;
            do {
                drawStud = rand.nextInt(5);
            } while (bag[drawStud] == 0);

            bag[drawStud]--;
            drawnStudents[drawStud]++;
        }

        return drawnStudents;
    }

    /**
     * Checks if a drawn character card has a specific ID code.
     *
     * @param id The character ID to check.
     * @return True if the character is drawn, false otherwise.
     */
    private boolean containsCharacter(int id) {
        for (CharacterCard character : characterCards) {
            if (character.getIDCode() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns unused students back to the bag.
     *
     * @param students Array of students to return.
     */
    private void returnStudentsToBag(int[] students) {
        for (int i = 0; i < 5; i++) {
            bag[i] += students[i];
        }
    }


    /**
     * This method resets the temporary clouds.
     */
    public void resetTempClouds() {
        tempClouds.addAll(clouds);
    }

    /**
     * This method returns mother nature position.
     *
     * @return Integer of mother nature position.
     */
    public int getMotherNaturePos() {
        return motherNaturePos;
    }

    /**
     * This method removes a temporary cloud given his index.
     *
     * @param cloud_index The index of the cloud to be removed.
     * @return The updated list of clouds.
     */
    public List<Cloud> deleteTempCloud(int cloud_index) {
        tempClouds.remove(cloud_index);
        return tempClouds;
    }

    /**
     * This method returns the list of clouds.
     *
     * @return The list of clouds.
     */
    public List<Cloud> getClouds() {
        return clouds;
    }

    /**
     * This method returns the array of boards.
     *
     * @return The array of boards.
     */
    public Board[] getBoards() {
        return boards;
    }

    /**
     * This method returns the number of islands.
     *
     * @return The number of islands.
     */
    public int getIslandsCounter() {
        return islandsCounter;
    }

    /**
     * This method returns the number of remaining islands.
     *
     * @return The number of remaining islands.
     */
    public int getHowManyLeft() {
        return islandsCounter;
    }

    /**
     * This method returns the list of islands.
     *
     * @return The list of islands.
     */
    public LinkedList<Island> getIslands() {
        return islands;
    }

    /**
     * This method returns the number of players in the game.
     *
     * @return The number of players in teh game.
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * This method returns the bag.
     *
     * @return The bag.
     */
    public int[] getBag() {
        return bag;
    }

    /**
     * This method sets the bag
     *
     * @param bag The bag.
     */
    public void setBag(int[] bag) {
        this.bag = bag;
    }

    /**
     * This method returns the array of characters.
     *
     * @return The array of characters.
     */
    public CharacterCard[] getCharacterCards() {
        return characterCards;
    }

    /**
     * This method returns the discard deck.
     *
     * @return The discard deck.
     */
    public AssistanceCard[] getDiscardDeck() {
        return discardDeck;
    }

    /**
     * This method draws 1 student from the bag.
     *
     * @return The index of the bag representing the colour of the drawn student.
     */
    public int drawOne() {
        if (isBagEmpty()) return -1; // Return -1 if the bag is empty

        Random rand = new Random();
        int chosen;

        do {
            chosen = rand.nextInt(5);
        } while (bag[chosen] == 0);

        bag[chosen]--;
        return chosen;
    }
}