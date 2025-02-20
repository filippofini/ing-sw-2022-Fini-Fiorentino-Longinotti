package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.network.message.ConnectionMessage;
import it.polimi.ingsw.network.message.toClient.*;
import it.polimi.ingsw.network.message.toServer.MessagesToServer;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;

/**
 * This class is used by the server to handle the connection with the client.
 * Each client has a corresponding client handler.
 */
public class ClientHandler implements ClientHandlerInterface, Runnable {

    public static final int HEARTBEAT = 5000;
    public static final int TIMEOUT_FOR_RESPONSE = 240000;
    private final Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final Server server;
    private Thread timer;
    private GameController gameController;
    private final Thread pinger;
    private boolean active = false;
    private int motherNatureMovement;
    private int pos;
    private int islandToMove;
    private int studToMove;
    private int assistantCardChosen;
    private int cloudChosen;
    private int useCharacterCard;
    private int ChCardUsed;
    private boolean canBeUsed;
    private int monkStudent;
    private int heraldIsland;
    private String nickname;
    private GameMode gameMode;

    /**
     * Constructor of the class.
     * It sends a ping message that check the client's connection until it become inactive.
     * @param socket The socket of the client.
     * @param server Server used for the connection.
     */
    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.pinger = new Thread(() -> {
            while (active) {
                try {
                    Thread.sleep(HEARTBEAT);
                    sendMessageToClient(ConnectionMessage.PING);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
    }

    /**
     * This method is used to start receiving messages in the client handler.
     */
    public void run() {
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            active = true;
            pinger.start();
            while (active) {
                try {
                    Object message = inputStream.readObject();
                    if (message != null && !(message == ConnectionMessage.PING)) {
                        stopTimer();
                        Server.SERVER_LOGGER.log(
                                Level.INFO,
                                "[" +
                                        (nickname != null
                                                ? nickname
                                                : socket
                                                .getInetAddress()
                                                .getHostAddress()) +
                                        "]: " +
                                        message
                        );
                        ((MessagesToServer) message).handleMessage(
                                server,
                                this
                        );
                    }
                } catch (ClassNotFoundException ignored) {} catch (
                        SocketTimeoutException e
                ) { //when the timer has expired
                    sendMessageToClient(new TimeoutExpiredMessage());
                    handleSocketDisconnection(true);
                } catch (IOException e) { //when the client is no longer connected
                    handleSocketDisconnection(false);
                }
            }
        } catch (IOException e) {
            boolean timeout = e instanceof SocketTimeoutException;
            handleSocketDisconnection(timeout);
        }
    }

    /**
     * This method is used to start timer.
     */
    @Override
    public void startTimer() {
        timer = new Thread(() -> {
            try {
                Thread.sleep(TIMEOUT_FOR_RESPONSE);
                handleSocketDisconnection(true);
            } catch (InterruptedException ignored) {}
        });
        timer.start();
    }

    /**
     * This method is used to stop the timer.
     */
    public void stopTimer() {
        if (timer != null && timer.isAlive()) {
            timer.interrupt();
        }
    }

    /**
     * This method sends a message to the client.
     * @param message The message to be sent.
     */
    @Override
    public synchronized void sendMessageToClient(Serializable message) {
        try {
            if (checkMessage(message)) Server.SERVER_LOGGER.log(
                    Level.INFO,
                    "[" +
                            (nickname != null
                                    ? nickname
                                    : socket.getInetAddress().getHostAddress()) +
                            "]: " +
                            message.toString()
            );

            outputStream.writeObject(message);
            outputStream.flush();
            outputStream.reset();
            System.out.println();
            if (!message.equals(ConnectionMessage.PING)) {
                wait();
            }
            if (
                    message instanceof MessagesToClient &&
                            ((MessagesToClient) message).hasTimer()
            ) startTimer();
        } catch (IOException e) {
            handleSocketDisconnection(e instanceof SocketTimeoutException);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to check whether a message should be printed.
     * @param message The message to check.
     * @return {@code True} if the message must be printed.
     */
    private boolean checkMessage(Serializable message) {
        return (
                message != ConnectionMessage.PING &&
                        !(message instanceof TextMessage)
        );
    }

    /**
     * This method handles client's disconnection.
     * @param timeout {@code True} if the timeout has been reached, {@code False} if not.
     */
    //If the timer is expired or the ping message cannot be sent due to disconnection of the client (it throws IO Exception) I tell the client that he has been disconnected
    private void handleSocketDisconnection(boolean timeout) {
        stopTimer();
        if (!active) return;
        //The connection is not active anymore
        this.active = false;
        Server.SERVER_LOGGER.log(
                Level.SEVERE,
                "[" +
                        (nickname != null
                                ? nickname
                                : socket.getInetAddress().getHostAddress()) +
                        "]: " +
                        "client disconnected" +
                        (timeout ? " because the timeout has expired" : "")
        );

        gameController.handleClientDisconnection(nickname);

        try {
            if (timeout) outputStream.writeObject(new TimeoutExpiredMessage());
            else outputStream.writeObject(ConnectionMessage.CONNECTION_CLOSED);
            outputStream.flush();
            outputStream.reset();
        } catch (IOException ignored) {}
        try {
            inputStream.close();
        } catch (IOException ignored) {}
        try {
            outputStream.close();
        } catch (IOException ignored) {}
        try {
            socket.close();
        } catch (IOException ignored) {}
    }

    /**
     * This method returns the game mode selected.
     * @return The game mode selected.
     */
    @Override
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * This method returns the nickname.
     * @return The nickname.
     */
    @Override
    public String getNickname() {
        return nickname;
    }

    /**
     * This method sets the position where to move the student.
     * @param pos The position where to move the student.
     */
    @Override
    public synchronized void setPosToMove(int pos) {
        this.pos = pos;
        notify();
    }

    /**
     * This method sets the nickname.
     * @param nickname The nickname.
     */
    @Override
    public synchronized void setNickname(String nickname) {
        this.nickname = nickname;
        notify();
    }

    /**
     * This method sets the movement of mother nature.
     * @param motherNatureMovement The movement of mother nature.
     */
    @Override
    public synchronized void setMotherNatureMovement(int motherNatureMovement) {
        this.motherNatureMovement = motherNatureMovement;
        notify();
    }

    /**
     * This method sets the game mode.
     * @param gameMode The game mode.
     */
    @Override
    public synchronized void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        notify();
    }

    /**
     * This method sets the start of the game.
     * @param gameStarted {@code true} to start the game.
     */
    @Override
    public synchronized void setGameStarted(boolean gameStarted) {
        notify();
    }

    /**
     * This method sets the number of players for the next game.
     * @param numberOfPlayersForNextGame The number of players for the next game.
     */
    public synchronized void setNumberOfPlayersForNextGame(
            int numberOfPlayersForNextGame
    ) {
        server.setNumberOfPlayersForNextGame(this, numberOfPlayersForNextGame);
        notify();
    }

    /**
     * This method sets the game controller.
     * @param gameController The game controller.
     */
    public synchronized void setGameController(GameController gameController) {
        this.gameController = gameController;
        notify();
    }

    /**
     * This method returns the game controller.
     * @return The game controller.
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * This method returns the server.
     * @return The server.
     */
    public Server getServer() {
        return server;
    }

    /**
     * This method sets the island where to move the student.
     * @param islandToMove The island where to move the student.
     */
    @Override
    public synchronized void setIslandToMove(int islandToMove) {
        this.islandToMove = islandToMove;
        notify();
    }

    /**
     * This method sets the student to be moved.
     * @param studToMove The student to be moved.
     */
    @Override
    public synchronized void setStudToMove(int studToMove) {
        this.studToMove = studToMove;
        notify();
    }

    /**
     * This method sets the position.
     * @param positionChosen The position.
     */
    @Override
    public synchronized void setPos(int positionChosen) {
        notify();
    }

    /**
     * This method sets the color.
     * @param color The color.
     */
    @Override
    public synchronized void setColor(int color) {
        notify();
    }

    /**
     * This method sets the chosen cloud.
     * @param cloudChosen The chosen cloud.
     */
    @Override
    public synchronized void setCloudChosen(int cloudChosen) {
        this.cloudChosen = cloudChosen;
        notify();
    }

    /**
     * This method sets the chosen assistant card to be played.
     * @param assistantCardChosen The chosen assistant card to be played.
     */
    @Override
    public synchronized void setAssistantCardChosen(int assistantCardChosen) {
        this.assistantCardChosen = assistantCardChosen;
        notify();
    }

    /**
     * This method returns the island where to move the chosen student.
     * @return  The island where to move the chosen student.
     */
    public int getIslandToMove() {
        return islandToMove;
    }

    /**
     * This method returns the movement of mother nature.
     * @return The movement of mother nature.
     */
    public int getMotherNatureMovement() {
        return motherNatureMovement;
    }

    /**
     * This method returns the chosen assistant card to be played.
     * @return The chosen assistant card to be played.
     */
    public int getAssistantCardChosen() {
        return assistantCardChosen;
    }

    /**
     * This method returns the chosen student to be moved.
     * @return The chosen student to be moved.
     */
    public int getStudToMove() {
        return studToMove;
    }

    /**
     * This method returns the position where to move the chosen student.
     * @return The position where to move the chosen student.
     */
    public int getPos() {
        return pos;
    }

    /**
     * This method returns the chosen cloud.
     * @return The chosen cloud.
     */
    public int getCloudChosen() {
        return cloudChosen;
    }

    /**
     * This method sets if the player wants to play a card.
     * @param useChCard The parameter used to see if the player wants to play a card.
     */
    public synchronized void setUseCharacterCard(int useChCard) {
        this.useCharacterCard = useChCard;
        notify();
    }

    /**
     * This method returns value that is used to see if the player wants to play a card.
     *
     * @return A value that is used to see if the player wants to play a card.
     */
    public boolean isCharacterCardUsed() {
        return useCharacterCard == 1;
    }

    /**
     * This method sets the card chosen to be played by the player.
     * @param chCardUsed The card chosen to be played by the player.
     */
    public synchronized void setChCardUsed(int chCardUsed) {
        ChCardUsed = chCardUsed;
        notify();
    }

    /**
     * This method returns the card chosen to be played by the player.
     * @return The card chosen to be played by the player.
     */
    public int getChCardUsed() {
        return ChCardUsed;
    }

    /**
     * This method checks if character card can be used.
     * @return {@code True} if it can be used, {@code False} if it can't.
     */
    public boolean isCharacterCardUsable() {
        return canBeUsed;
    }

    /**
     * This method sets if the character card can be used.
     * @param canBeUsed {@code True} if it can be used, {@code False} if it can't.
     */
    public synchronized void setCanBeUsed(boolean canBeUsed) {
        this.canBeUsed = canBeUsed;
        notify();
    }

    /**
     * This method sets the waiting in the lobby.
     * @param waitingInTheLobby The waiting in the lobby.
     */
    public synchronized void setWaitingInTheLobby(boolean waitingInTheLobby) {
        notify();
    }

    /**
     * This method sets the display of the dining room.
     * @param displayDiningRoom The display of the dining room.
     */
    public synchronized void setDisplayDiningRoom(boolean displayDiningRoom) {
        notify();
    }

    /**
     * This method sets the display of island info.
     * @param displayIslandInfo The display of island info.
     */
    public synchronized void setDisplayIslandInfo(boolean displayIslandInfo) {
        notify();
    }

    /**
     * This method sets the display of the previously chosen student.
     * @param displayStudentChosenPreviously The previously chosen student.
     */
    public synchronized void setDisplayStudentChosenPreviously(
            boolean displayStudentChosenPreviously
    ) {
        notify();
    }

    /**
     * This method notifies the results.
     * @param resultNotify The results.
     */
    public synchronized void setResultNotify(boolean resultNotify) {
        notify();
    }

    /**
     * This method sets if the massage has expired.
     * @param timeoutExpired the expired message.
     */
    public synchronized void setTimeoutExpired(boolean timeoutExpired) {
        notify();
    }

    /**
     * This method sets if there are not enough coins.
     * @param notEnoughCoin The coins checker.
     */
    public synchronized void setNotEnoughCoin(boolean notEnoughCoin) {
        notify();
    }

    /**
     * This method sets the student on the monk card.
     * @param monkStudent The student on the monk card.
     */
    public synchronized void setMonkStudent(int monkStudent) {
        this.monkStudent = monkStudent;
        notify();
    }

    /**
     * This method returns the student chosen from the monk card.
     * @return The student chosen from the monk card.
     */
    public int getMonkStudent() {
        return monkStudent;
    }

    /**
     * This method sets the island chosen after the herald character card.
     * @param heraldIsland The island chosen after the herald character card.
     */
    public synchronized void setHeraldIsland(int heraldIsland) {
        this.heraldIsland = heraldIsland;
        notify();
    }

    /**
     * This method returns the chosen island after the herald character card.
     * @return The chosen island after the herald character card.
     */
    public int getHeraldIsland() {
        return heraldIsland;
    }

    /**
     * This method returns if the client is active.
     * @return True if client is active, False otherwise.
     */
    public boolean isConnected() {
        return active;
    }
}