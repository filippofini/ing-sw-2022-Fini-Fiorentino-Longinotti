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
    private int useChCard=false;


    public boolean isGameStarted() {
        return gameStarted;
    }

    private boolean gameStarted = false;

    private String nickname = null;
    private GameMode gameMode;

    private ClientHandlerPhase clientHandlerPhase;


    /**
     * Constructor. It sends a ping message that check the client's connection until it become inactive
     * @param socket the socket of the client.
     * @param server server used for the connection.
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
     * Method used to start receiving messages in the client handler
     */
    //TODO: capire come gestire la game phase tramite il controller.
    public void run(){
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            active = true;
            pinger.start();

            clientHandlerPhase = ClientHandlerPhase.WAITING_GAME_MODE;
            sendMessageToClient(new GameModeRequest());

            while(active){
                try {
                    Object message = inputStream.readObject();
                    if(message != null && !(message == ConnectionMessage.PING)) {
                        stopTimer();
                        Server.SERVER_LOGGER.log(Level.INFO, "[" + (nickname != null ? nickname : socket.getInetAddress().getHostAddress()) + "]: " + message);
                        if(active && !(gameStarted && gameController.getGamePhase() instanceof PlayPhase && !(((PlayPhase) gameController.getGamePhase()).getTurnController().getCurrentPlayer().getNickname().equals(nickname))))
                            ((MessagesToServer) message).handleMessage(server, this);
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
     * Method used to start timer.
     */
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
     * Method used to stop the timer
     */
    public void stopTimer(){
        if (timer != null && timer.isAlive()){
            timer.interrupt();
            }
    }

    /**
     * Method used to send a message to the client
     * @param message the message to be sent
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
     * Method to check whether a message should be printed.
     * @param message the message to check
     * @return true only if the message must be printed
     */
    private boolean checkMessage(Serializable message){
        return  (message != ConnectionMessage.PING && !(message instanceof TextMessage));
    }

    /**
     * Method to handle client's disconnection
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

    @Override
    public GameMode getGameMode() {
        return gameMode;
    }

    @Override
    public String getNickname(){
        return nickname;
    }

    @Override
    public boolean checkMnMovThisTurn() {
        return false;
    }

    @Override
    public ClientHandlerPhase getClientHandlerPhase(){
        return clientHandlerPhase;
    }

    @Override
    public void setposToMove(int pos) {
        this.pos = pos;
    }


    @Override
    public void setNickname(String nickname){
        this.nickname = nickname;
        server.handleNicknameChoice(this);
    }

    @Override
    public void setMnmovement(int mnmovement) {
        this.mnmovement = mnmovement;
    }

    @Override
    public void setClientHandlerPhase(ClientHandlerPhase clientHandlerPhase){
        this.clientHandlerPhase = clientHandlerPhase;
    }

    @Override
    public void setGameMode(GameMode gameMode){
        this.gameMode = gameMode;
    }

    @Override
    public void setGameStarted(boolean gameStarted){
        this.gameStarted = gameStarted;
    }

    public void setNumberOfPlayersForNextGame(int numberOfPlayersForNextGame){
        server.setNumberOfPlayersForNextGame(this, numberOfPlayersForNextGame);
    }

  public void setGameController(GameController gameController){
      this.gameController = gameController;
    }

    public GameController getGameController() {
        return gameController;
    }
    public Server getServer(){
        return server;
    }

    public boolean isActive(){
        return active;
    }

    public boolean isValidNickname() {
        return validNickname;
    }

    public void setValidNickname(boolean validNickname) {
        this.validNickname = validNickname;
    }

    @Override
    public void setIslandToMove(int islandToMove) {
        this.islandToMove = islandToMove;
    }

    @Override
    public void setstudToMove(int studToMove) {
        this.studToMove=studToMove;
    }

    @Override
    public void setpos(int positionChosen) {
        this.positionChosen=positionChosen;
    }

    @Override
    public void setcolour(int colour) {
        this.colour=colour;
    }

    @Override
    public void setCloudChosen(int cloudChosen) {
        this.cloudChosen = cloudChosen;
    }

    @Override
    public void setAssistantCardChosen(int assistantCardChosen) {
        this.assistantCardChosen = assistantCardChosen;
    }

    public int getIslandToMove() {
        return islandToMove;
    }

    public int getMnmovement() {
        return mnmovement;
    }

    public int getAssistantCardChosen() {
        return assistantCardChosen;
    }

    public int getStudToMove() {
        return studToMove;
    }

    public int getPos() {
        return pos;
    }

    public int getCloudChosen() {
        return cloudChosen;
    }

    public void setUseCharacterCard(int useChCard) {
        this.useChCard = useChCard;
    }

    public int getUseCharacterCard() {
        return useChCard;
    }
}

