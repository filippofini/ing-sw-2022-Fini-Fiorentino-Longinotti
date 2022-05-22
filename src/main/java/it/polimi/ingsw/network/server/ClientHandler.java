package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Game_Controller;
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

import it.polimi.ingsw.network.server.ClientHandlerInterface;

public class ClientHandler implements ClientHandlerInterface, Runnable {
    public static final int HEARTBEAT = 5000;
    public static final int TIMEOUT_FOR_RESPONSE = 240000;
    private final Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Server server;
    private Thread timer;
    private Game_Controller controller;
    private final Thread pinger;
    private boolean active = false;
    private boolean validNickname;

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
    //TODO: capire come gestire la game phase
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
                    Object messageFromClient = inputStream.readObject();
                    if(messageFromClient != null && !(messageFromClient == ConnectionMessage.PING)) {
                        stopTimer();
                        Server.SERVER_LOGGER.log(Level.INFO, "[" + (nickname != null ? nickname : socket.getInetAddress().getHostAddress()) + "]: " + messageFromClient);
                        if(active && !(gameStarted && controller.getGamePhase() instanceof PlayPhase && !(((PlayPhase) controller.getGamePhase()).getTurnController().getCurrentPlayer().getNickname().equals(nickname))))
                            ((MessagesToServer) messageFromClient).handleMessage(server, this);
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

    //TODO: implementare i messaggi mancanti
    private boolean checkMessage(Serializable message){
        return !(message instanceof MatchDataMessage) && message != ConnectionMessage.PING && !(message instanceof LoadLeaderCardsMessage) && !(message instanceof LoadDevelopmentCardsMessage) && !(message instanceof TextMessage);
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
            controller.handleClientDisconnection(nickname);
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
    public ClientHandlerPhase getClientHandlerPhase(){
        return clientHandlerPhase;
    }


    @Override
    public void setNickname(String nickname){
        this.nickname = nickname;
        server.handleNicknameChoice(this);
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

  public void setController(Game_Controller controller){
      this.controller = controller;
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
}

