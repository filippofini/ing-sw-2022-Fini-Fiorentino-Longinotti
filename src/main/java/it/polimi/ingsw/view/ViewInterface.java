package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;

import java.util.List;


/**
 * This interface contains the methods to handle the request sent to/by the client.
 */
public interface ViewInterface {


    /**
     * This method handle the closing of connections.
     * @param was_connected Boolean to check if there was a connection.
     */
    void handleCloseConnection(boolean was_connected);

    /**
     * This method notifies that the time to send a response is over.
     */
    void displayTimeoutCloseConnection();

    /**
     * This method asks the name.
     * @param is_retry {@code True} if it's not the first time that the nickname is asked.
     * @param taken {@code True} if someone else has already this nickname.
     */
    void displayNicknameRequest(boolean is_retry, boolean taken);

    /**
     * This method displays the request of the game mode.
     */
    void displayGameModeRequest();

    /**
     * This method asks the desired number of players for the game.
     */
    void displayNumberOfPlayersRequest();

    /*
     * This method sets the players names.
     * @param player_name The name of the client receiving the message.
     * @param other_names The names of the other players.
     *
    void setNicknames(String player_name, List<String> other_names);

    /**
     * This method notifies all the players that are ready to play.
     * @param names The list of names.
     *
    void displayPlayersReadyToStartMessage(List<String> names);
    */
    /**
     * This method notifies the waiting before starting the game.
     */
    void displayWaitingMessage();

    /*
     * This method informs a disconnection.
     * @param name The name of the player.
     *
    void displayDisconnection(String name);
    *

    void displayTimeoutFinishedMessage();
    */

    void displayMessage(String message);

    /**
     * This method displays the request of the mother nature movement.
     */
    void motherNatureMovementRequest(int Mn_pos, Player Current_Player);

    /*
     * This method displays the request of the number of moves of mother nature.
     *
    void displayPositionToMoveRequest();
    */
    /**
     * This method displays the request of the students to move.
     */
    void displayStudentToMoveRequest(Board board);

    /*
     * This method displays the number of conquered islands.
     *
    void displayConqueredIslands(int[] conquered);
    */

    /**
     * This method displays the request of the choice for students movement.
     */
    void displayWhereToMoveStudents(Board board, int choiceStudent);
    /**
     * This method request in which island the student must be moved.
     */
    void MoveStudentToIslandRequest(List<Island> islands, Student stud_to_island);
    /**
     * This method displays the request of the choice for the assistant card.
     */
    void displayChooseAssistantCardRequest(Player player, GameTable GT);

    /**
     * This method displays the request of the choice for a cloud.
     */
    void displayChooseCloudRequest(List<Cloud> clouds);
    /**
     * This method displays the Island info.
     */
    void displayIslandInfo(Island island, int islandID);
    /**
     * This method displays the :Dining Room table of the colour "x" is full message.
     */
    void displayDiningRoomColourFull( Board board,int choiceStudent);
    /**
     * This method displays the: student chosen previously message.
     */
    void displayStudentChosenPreviously( Board board,int choiceStudent);
    /**
     * This method displays the entrance students.
     */
    void displayEntranceStudents(Board board);
    /**
     * This method request a position.
     */
    void choosePositionRequest(int upperLimit);
    /**
     * This method close the connection.
     */
    void closeConnection();
    /**
     * This method display a player disconnection message.
     */
    void displayDisconnection(String name);
    /**
     * This method ask if the player wants to use a character card.
     */
    void UseCharacterCard();
    /**
     * This method displays the request the Character card the player wants to use.
     */
    void ChooseCharacterCard(Player player,CharacterCard[] cc);

    /*
     * This method informs that the game is started.
     *
    void displayStartGameNotify();
    */

    /*
     * This method informs that the game is ended.
     *
    void displayEndGameNotify();
    */
    /**
     * This method shows the results of the game.
     */
    void displayResults( List<Island> islands, List<Player> players,Board[] boards);

    /**
     * This method displays the request of the colour of disk's student prompt to use two character cards.
     */
    void displayColourRequest();

}
