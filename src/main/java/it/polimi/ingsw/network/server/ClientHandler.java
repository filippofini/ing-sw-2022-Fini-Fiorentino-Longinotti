package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.message.ConnectionMessage;
import it.polimi.ingsw.network.message.toClient.GameModeRequest;
import it.polimi.ingsw.network.message.toClient.MessagesToClient;
import it.polimi.ingsw.network.message.toClient.TextMessage;
import it.polimi.ingsw.network.message.toClient.TimeoutExpiredMessage;
import it.polimi.ingsw.network.message.toServer.MessagesToServer;
import it.polimi.ingsw.enumerations.ClientHandlerPhase;
import it.polimi.ingsw.model.GameMode;
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
    private Server server;
    private Thread timer;
    private GameController gameController;
    private final Thread pinger;
    private boolean active = false;
    private boolean validNickname;
    private int mnmovement;
    private int pos;
    private int islandToMove;
    private int studToMove;
    private int positionChosen;
    private int colour;
    private  int assistantCardChosen;
    private  int cloudChosen;
    private int useChCard=0;
    private int ChCardUsed;
    private boolean canBeUsed;

    /**
     * This method checks if the game has started.
     * @return {@code True} if the game has started, {@code False} if not.
     */
    public boolean isGameStarted() {
        return gameStarted;
    }

    private boolean gameStarted = false;

    private String nickname = null;
    private GameMode gameMode;

    private ClientHandlerPhase clientHandlerPhase;


    /**
     * Constructor of the class.
     * It sends a ping message that check the client's connection until it become inactive.
     * @param socket The socket of the client.
     * @param server Server used for the connection.
     */
    public ClientHandler(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
        this.validNickname = false;
        this.pinger = new Thread(() -> {
            while (active){
                try{
                    Thread.sleep(HEARTBEAT);
                    sendMessageToClient(ConnectionMessage.PING);
                }catch (InterruptedException e){
                    break;
                }
            }
        });
    }

    /**
     * This method is used to start receiving messages in the client handler.
     */
    public void run(){
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            active = true;
            pinger.start();

            clientHandlerPhase = ClientHandlerPhase.WAITING_GAME_MODE;
            //sendMessageToClient(new GameModeRequest());

            while(active){
                try {
                    Object message = inputStream.readObject();
                    if(message != null && !(message == ConnectionMessage.PING)) {
                        stopTimer();
                        Server.SERVER_LOGGER.log(Level.INFO, "[" + (nickname != null ? nickname : socket.getInetAddress().getHostAddress()) + "]: " + message);
                        if(active && !(gameStarted && gameController.getCheck())){
                            ((MessagesToServer) message).handleMessage(server, this);
                        }
                    }

                } catch (ClassNotFoundException ignored) {
                } catch (SocketTimeoutException e){ //when the timer has expired
                    sendMessageToClient(new TimeoutExpiredMessage());
                    handleSocketDisconnection(true);
                } catch (IOException e){//when the client is no longer connected
                    handleSocketDisconnection(false);
                }

            }
        }catch (IOException e){
            boolean timeout = e instanceof SocketTimeoutException;
            handleSocketDisconnection(timeout);
        }
    }

    /**
     * This method is used to start timer.
     */
    @Override
    public void startTimer(){
        timer = new Thread(() -> {
            try{
                Thread.sleep(TIMEOUT_FOR_RESPONSE);
                handleSocketDisconnection(true);
            } catch (InterruptedException e){ }
        });
        timer.start();
    }

    /**
     * This method is used to stop the timer.
     */
    public void stopTimer(){
        if (timer != null && timer.isAlive()){
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
            if (checkMessage(message))
                Server.SERVER_LOGGER.log(Level.INFO, "[" + (nickname != null ? nickname : socket.getInetAddress().getHostAddress()) + "]: " + message.toString());
            outputStream.writeObject(message);
            outputStream.flush();
            outputStream.reset();
            if (message instanceof MessagesToClient &&((MessagesToClient) message).hasTimer())
                startTimer();
        } catch (IOException e) {
            handleSocketDisconnection(e instanceof SocketTimeoutException);
        }
    }

    /**
     * This method is used to check whether a message should be printed.
     * @param message The message to check.
     * @return {@code True} if the message must be printed.
     */
    private boolean checkMessage(Serializable message){
        return  (message != ConnectionMessage.PING && !(message instanceof TextMessage));
    }

    /**
     * This method handles client's disconnection.
     * @param timeout {@code True} if the timeout has been reached, {@code False} if not.
     */
    //If the timer is expired or the ping message cannot be sent due to disconnection of the client (it throws IO Exception) I tell the client that he has been disconnected
    private void handleSocketDisconnection(boolean timeout){
        stopTimer();
        if (!active)
            return;
        //The connection is not active anymore
        this.active = false;
        Server.SERVER_LOGGER.log(Level.SEVERE, "[" + (nickname != null ? nickname : socket.getInetAddress().getHostAddress())+ "]: " + "client disconnected" + (timeout ? " because the timeout has expired" : ""));
        //If the game is started, the controller will handle his disconnection
        if (gameStarted){
            gameController.handleClientDisconnection(nickname);
        } else {
            //If the game is not started yet, I simply remove the player from the list of waiting players
            server.removeConnectionLobby(this);
        }
        try {
            if (timeout)
                outputStream.writeObject(new TimeoutExpiredMessage());
            else
                outputStream.writeObject(ConnectionMessage.CONNECTION_CLOSED);
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) { }
        try {
            inputStream.close();
        } catch (IOException e) {
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        try {
            socket.close();
        } catch (IOException e) {
        }
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
    public String getNickname(){
        return nickname;
    }

    /**
     * This method checks if mother nature can be moved.
     * @return {@code True} if mother nature can be moved, {@code False} if not.
     */
    @Override
    public boolean checkMnMovThisTurn() {
        return false;
    }

    /**
     * This method returns the client handler phase.
     * @return The client handler phase.
     */
    @Override
    public ClientHandlerPhase getClientHandlerPhase(){
        return clientHandlerPhase;
    }

    /**
     * This method sets the position where to move the student.
     * @param pos The position where to move the student.
     */
    @Override
    public void setposToMove(int pos) {
        this.pos = pos;
    }

    /**
     * This method sets the nickname.
     * @param nickname The nickname.
     */
    @Override
    public void setNickname(String nickname){
        this.nickname = nickname;
        server.handleNicknameChoice(this);
    }

    /**
     * This method sets the movement of mother nature.
     * @param mnmovement The movement of mother nature.
     */
    @Override
    public void setMnmovement(int mnmovement) {
        this.mnmovement = mnmovement;
    }

    /**
     * This method sets the client handler phase.
     * @param clientHandlerPhase The client handler phase.
     */
    @Override
    public void setClientHandlerPhase(ClientHandlerPhase clientHandlerPhase){
        this.clientHandlerPhase = clientHandlerPhase;
    }

    /**
     * This method sets the game mode.
     * @param gameMode The game mode.
     */
    @Override
    public void setGameMode(GameMode gameMode){
        this.gameMode = gameMode;
    }

    /**
     * This method sets the start of the game.
     * @param gameStarted {@code true} to start the game.
     */
    @Override
    public void setGameStarted(boolean gameStarted){
        this.gameStarted = gameStarted;
    }

    /**
     * This method sets the number of players for the next game.
     * @param numberOfPlayersForNextGame The number of players for the next game.
     */
    public void setNumberOfPlayersForNextGame(int numberOfPlayersForNextGame){
        server.setNumberOfPlayersForNextGame(this, numberOfPlayersForNextGame);
    }

    /**
     * This method sets the game controller.
     * @param gameController The game controller.
     */
    public void setGameController(GameController gameController){
        this.gameController = gameController;
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
    public Server getServer(){
        return server;
    }

    /**
     * This method checks if it's active.
     * @return {@code True} if is active.
     */
    public boolean isActive(){
        return active;
    }

    /**
     * This method checks if the chosen nickname is valid.
     * @return {@code True} if is valid.
     */
    public boolean isValidNickname() {
        return validNickname;
    }

    /**
     * This method sets a valid nickname.
     * @param validNickname A valid nickname.
     */
    public void setValidNickname(boolean validNickname) {
        this.validNickname = validNickname;
    }

    /**
     * This method sets the island where to move the student.
     * @param islandToMove The island where to move the student.
     */
    @Override
    public void setIslandToMove(int islandToMove) {
        this.islandToMove = islandToMove;
    }

    /**
     * This method sets the student to be moved.
     * @param studToMove The student to be moved.
     */
    @Override
    public void setstudToMove(int studToMove) {
        this.studToMove=studToMove;
    }

    /**
     * This method sets the position.
     * @param positionChosen The position.
     */
    @Override
    public void setpos(int positionChosen) {
        this.positionChosen=positionChosen;
    }

    /**
     * This method sets the colour.
     * @param colour The colour.
     */
    @Override
    public void setcolour(int colour) {
        this.colour=colour;
    }

    /**
     * This method sets the chosen cloud.
     * @param cloudChosen The chosen cloud.
     */
    @Override
    public void setCloudChosen(int cloudChosen) {
        this.cloudChosen = cloudChosen;
    }

    /**
     * This method sets the chosen assistant card to be played.
     * @param assistantCardChosen The chosen assistant card to be played.
     */
    @Override
    public void setAssistantCardChosen(int assistantCardChosen) {
        this.assistantCardChosen = assistantCardChosen;
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
    public int getMnmovement() {
        return mnmovement;
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
    public void setUseCharacterCard(int useChCard) {
        this.useChCard = useChCard;
    }

    /**
     * This method returns value that is used to see if the player wants to play a card.
     * @return A value that is used to see if the player wants to play a card.
     */
    public int getUseCharacterCard() {
        return useChCard;
    }

    /**
     * This method sets the card chosen to be played by the player.
     * @param chCardUsed The card chosen to be played by the player.
     */
    public void setChCardUsed(int chCardUsed) {
        ChCardUsed = chCardUsed;
    }

    /**
     * This method returns the card chosen to be played by the player.
     * @return The card chosen to be played by the player.
     */
    public int getChCardUsed() {
        return ChCardUsed;
    }

    /**
     * This method checks if it can be used.
     * @return {@code True} if it can be used, {@code False} if it can't.
     */
    public boolean getCanBeUsed() {
        return canBeUsed;
    }

    /**
     * This method sets if the character card can be used.
     * @param canBeUsed {@code True} if it can be used, {@code False} if it can't.
     */
    public void setCanBeUsed(boolean canBeUsed) {
        this.canBeUsed = canBeUsed;
    }
}

