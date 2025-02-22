package it.polimi.ingsw.model;


import java.io.Serializable;

/**
 * This class represent the islands.
 * Each island has an array of int used to keep track of the students and their type.
 * Pos. 0 represent the yellow... More info at {@link DiskColor}.
 * One island has mother nature set at the beginning of the game and then moved with a method on {@link GameTable}.
 * mother nature is represented by a boolean: {@code True} if is on the island, {@code False} if not on the island.
 * Each island can have at max 1 tower (more than 1 if merged). The tower can be placed by the player that control the island.
 */
public class Island implements Serializable {

    private final Board[] boards;
    private boolean motherNatureHere;
    private int tower;
    private int influenceController;
    private int playerController;
    private String controllerName;
    private final int island_ID;
    private int[] arrStudents;
    private boolean prohibition_card;
    private boolean includeTowers;
    private boolean ColorProhibitionActive;
    private int colorProhibitionIndex;
    private int extraInfluence;
    private final String[] names;

    /**
     * Constructor of the class.
     *
     * @param boards     Reference to the boards of the players.
     * @param island_ID  the starting ID of the island, it's not very useful.
     * @param towerColor Puts tower starter to the island, that means no tower (see: {@link TowerColour}).
     */
    public Island(Board[] boards, int island_ID, TowerColour towerColor, String[] names) {
        this.island_ID = island_ID;
        this.boards = boards;
        this.tower = towerColor.getTowerTranslate();
        influenceController = 0;
        arrStudents = new int[5];
        motherNatureHere = false;
        playerController = -1;
        controllerName = "None";
        prohibition_card = false;
        ColorProhibitionActive = false;
        colorProhibitionIndex = 0;
        includeTowers = true;
        extraInfluence = 0;
        this.names = names;
    }

    /**
     * This method calculate the influence of the player on the island.
     * The player with the highest influence becomes the controller and can place a tower if mother nature is on that island.
     *
     * @param currentPlayer The current player of the turn.
     * @param Boards        The array of the boards of the players.
     * @return {@code False} if the control of the island is changed, {@code True} otherwise.
     */
    public boolean calculateInfluence(int currentPlayer, Board[] Boards) {
        // If a prohibition card is on the island, remove it and return immediately.
        if (prohibition_card) {
            setProhibitionCard(false);
            if (ColorProhibitionActive) {
                setProhibitionColour(false);
            }
            return true; // Island control does NOT change because no influence is calculated.
        }

        // 1) Compute base influence (count students of the colors the current player has professors for).
        int newInfluence = 0;
        for (int i = 0; i < 5; i++) {
            if (boards[currentPlayer].getArrProfessors()[i]) {
                // If there is a color prohibition, skip that color.
                if (!ColorProhibitionActive || i != colorProhibitionIndex) {
                    newInfluence += arrStudents[i];
                }
            }
        }

        // 2) If towers should be included and the current player is already the controller, add them.
        if (includeTowers && currentPlayer == playerController) {
            newInfluence += tower;
        }

        // 3) Determine the “threshold” above which control changes.
        //    • If towers are included, we compare with influence_controller (full current influence).
        //    • If towers are not included, we subtract tower from influence_controller
        //      because the existing tower on this island was previously counted.
        int threshold = includeTowers ? influenceController : (influenceController - tower);

        boolean samePlayer = true;  // true if control does NOT change
        // 4) If the new influence beats the threshold, see if we need to change the island's control.
        if (newInfluence > threshold) {
            // If a different player outscored the current controller:
            if (currentPlayer != playerController) {
                // 4-Player logic: place the tower on the correct board (team board) instead of the old controller.
                if (Boards.length == 4) {
                    if (currentPlayer == 1 || currentPlayer == 2) {
                        Boards[1].setNumTowers(Boards[1].getNumTowers() + tower);
                    } else {
                        Boards[3].setNumTowers(Boards[3].getNumTowers() + tower);
                    }
                    // Update control to the new player
                    playerController = currentPlayer;
                    translateIDName();
                    tower = 1;
                    // In the original code for 4-players, influence_controller is set to the immediate new influence
                    // (plus extra_influence only if towers were NOT included).
                    influenceController = newInfluence + (includeTowers ? 0 : extraInfluence);
                } else {
                    // 2-Player (or 3-Player) logic: return the tower to the old controller (if any), then assign control.
                    if (playerController != -1) {
                        Boards[playerController].setNumTowers(
                                Boards[playerController].getNumTowers() + tower
                        );
                    }
                    playerController = currentPlayer;
                    translateIDName();
                    tower = 1;
                    // In the original code, if towers are included, we add +1 as well as extra_influence.
                    // Otherwise, just extra_influence.
                    influenceController = newInfluence + extraInfluence + (includeTowers ? 1 : 0);
                }
                samePlayer = false; // Control changes hands
            } else {
                // Same player retains control; just update influence.
                influenceController = newInfluence + extraInfluence;
            }
        }

        // 5) If a color prohibition was in effect, clear it after calculation.
        if (ColorProhibitionActive) {
            setProhibitionColour(false);
        }

        // Return false if control changed; true otherwise (same player).
        return samePlayer;
    }


    /**
     * This method can be used to add students to the island.
     *
     * @param transfer The list of students to be added to the island.
     */
    public void addStudents(Student transfer) {
        arrStudents[transfer.getColor()]++;
    }


    /**
     * This method returns the player that controls the island.
     *
     * @return The player that controls the island.
     */
    public int checkController() {
        return playerController;
    }

    /**
     * This method can be used to add a tower to the island.
     *
     * @param current_player The current player of the turn.
     */
    public void addTower(int current_player) {
        this.tower = boards[current_player].getTower();
    }

    /**
     * This method returns the boards of the players
     *
     * @return The boards of the players.
     */
    public Board[] getBoards() {
        return boards;
    }

    /**
     * This method checks if mother nature is on the island.
     *
     * @return {@code True} if mother nature is on the island, {@code False} if not.
     */
    public boolean isMotherNatureHere() {
        return motherNatureHere;
    }

    /**
     * This method returns the int that represent the colour of the tower on the island.
     *
     * @return The int that represent the colour of the tower on the island.
     */
    public int getTower() {
        return tower;
    }

    /**
     * This method returns the influence of the player that controls the island.
     *
     * @return The influence of the player that controls the island.
     */
    public int getInfluenceController() {
        return influenceController;
    }

    /**
     * This method returns the int that represent the player that controls the island.
     *
     * @return The int that represent the player that controls the island.
     */
    public int getPlayerController() {
        return playerController;
    }

    /**
     * This method returns the island ID.
     *
     * @return The island ID.
     */
    public int getIsland_ID() {
        return island_ID;
    }

    /**
     * This method returns the students on the island
     *
     * @return The array of students on the island
     */
    public int[] getArrStudents() {
        return arrStudents;
    }

    /**
     * This method sets mother nature on the island, {@code True} if is on the island, {@code False} if not on the island.
     *
     * @param mother_nature {@code True} if is on the island, {@code False} if not on the island.
     */
    public void setMotherNature(boolean mother_nature) {
        this.motherNatureHere = mother_nature;
    }

    /**
     * This method sets the tower on the island.
     *
     * @param tower The int that represent the tower to be placed on the island.
     */
    public void setTower(int tower) {
        this.tower = tower;
    }

    /**
     * This method sets the highest influence on the island.
     *
     * @param influenceController The number representing the highest influence on the island.
     */
    public void setInfluenceController(int influenceController) {
        this.influenceController = influenceController;
    }

    /**
     * This method sets the player that has the highest influence.
     *
     * @param player_controller The player ID that has the highest influence.
     */
    public void setPlayerController(int player_controller) {
        this.playerController = player_controller;
        translateIDName();
    }

    /**
     * This method sets the students on the island.
     *
     * @param arr_students The array students to be set on the island.
     */
    public void setArrStudents(int[] arr_students) {
        this.arrStudents = arr_students;
    }

    /**
     * This method increments the array of student given the index.
     * It is used during the start of the game.
     *
     * @param index The index to increment in array of students.
     */
    public void incrementPos(int index) {
        this.arrStudents[index]++;
    }

    /**
     * This method checks if a prohibition card has been put on the island.
     *
     * @return {@code True} if a prohibition card has been put on the island, {@code False} if not.
     */
    public boolean isProhibitionCard() {
        return prohibition_card;
    }

    /**
     * This method sets a prohibition card on the island.
     *
     * @param prohibition_card {@code True} if wanted to put a prohibition card on the island, {@code False} if not.
     */
    public void setProhibitionCard(boolean prohibition_card) {
        this.prohibition_card = prohibition_card;
    }

    /**
     * This method increments the element of the array of students by 1 given the index.
     *
     * @param index The index to know which student will be added.
     */
    public void setOneStudent(int index) {
        arrStudents[index]++;
    }

    /**
     * This method sets the include_towers variable
     *
     * @param include_towers indicate if the towers must be counted in the influence
     */
    public void setIncludeTowers(boolean include_towers) {
        this.includeTowers = include_towers;
    }

    /**
     * This method sets the prohibition.
     *
     * @param prohibition_colour {@code True} if the prohibition is enabled, {@code False} if not.
     */
    public void setProhibitionColour(boolean prohibition_colour) {
        this.ColorProhibitionActive = prohibition_colour;
    }

    /**
     * This method sets the extra influence activated by a character card.
     *
     * @param extra_influence The extra influence to be added to the calculus of the influence.
     */
    public void setExtraInfluence(int extra_influence) {
        this.extraInfluence = extra_influence;
    }

    /**
     * This method returns the extra influence.
     *
     * @return The extra influence.
     */
    public int getExtraInfluence() {
        return extraInfluence;
    }

    /**
     * This method returns the name of the player that controls the island.
     *
     * @return The name of the player that controls the island.
     */
    public String getControllerName() {
        return controllerName;
    }

    /**
     * This method sets the name of the player that controls the island.
     *
     * @param controllerName The name of the player that controls the island.
     */
    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    /**
     * This method sets the name of the player that controls the island.
     */
    private void translateIDName() {
        if (playerController == -1)
            setControllerName("None");
        else
            setControllerName(names[playerController]);
    }

    /**
     * This method returns the include_towers variable.
     *
     * @return {@code True} if the towers must be counted in the influence, {@code False} if not.
     */
    public boolean isIncludeTowers() {
        return includeTowers;
    }
}