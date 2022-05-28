package it.polimi.ingsw.view;

import java.util.List;

public interface ViewInterface {

    void handleCloseConnection(boolean was_connected);

    /**
     * This method notifies that the time to send a response is over.
     */
    void displayTimeoutExpiredMessage();

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

    /**
     * This method sets the players names.
     * @param player_name The name of the client receiving the message.
     * @param other_names The names of the other players.
     */
    void setNicknames(String player_name, List<String> other_names);

    /**
     * This method notifies all the players that are ready to play.
     * @param names The list of names.
     */
    void displayPlayersReadyToStartMessage(List<String> names);

    /**
     * This method notifies the waiting before starting the game.
     */
    void displayWaitingInTheLobbyMessage();

    /**
     * This method informs a disconnection.
     * @param name The name of the player.
     */
    void displayDisconnection(String name);


    void displayTimeoutFinishedMessage();

    void displayMessage(String message);

    /**
     * This method displays the request of the mother nature movement.
     */
    void displayMoveMnRequest();

    /**
     * This method displays the request of the number of moves of mother nature.
     */
    void displayPositionToMoveRequest();

    /**
     * This method displays the request of the students to move.
     */
    void displayStudentToMoveRequest();

    /**
     * This method displays the number of conquered islands.
     */
    void displayConqueredIslands(int[] conquered);

    /**
     * This method displays the request of the choice for students movement.
     */
    void displayWhereToMoveStudents();

    /**
     * This method displays the request of the choice for the assistant card.
     */
    void displayChooseAssistantCardRequest();

    /**
     * This method displays the request of the choice for a cloud.
     */
    void displayChooseCloudRequest();

    /**
     * This method informs that the game is started.
     */
    void displayStartGameNotify();

    /**
     * This method informs that the game is ended.
     */
    void displayEndGameNotify();

    /**
     * This method shows the results of the game.
     */
    void displayResults();

    /**
     * This method displays the request of the colour of disk's student prompt to use two character cards.
     */
    void displayColourRequest();
}
