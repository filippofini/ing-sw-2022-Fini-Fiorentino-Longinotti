package it.polimi.ingsw.network.server;


import it.polimi.ingsw.controller.Game_Controller;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.view.VirtualView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Server class that starts a socket server.
 */
public class Server {
    private final Game_Controller gameController;

    private final Map<String, ClientHandler> clientHandlerMap;

    public static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private final Object lock;

    public Server(Game_Controller gameController) {
        this.gameController = gameController;
        this.clientHandlerMap = Collections.synchronizedMap(new HashMap<>());
        this.lock = new Object();
    }

    /**
     * This method adds a client to be managed by the server instance.
     *
     * @param name      the nickname associated with the client.
     * @param clientHandler the ClientHandler associated with the client.
     */
    public void addClient(String name, ClientHandler clientHandler) {
        VirtualView vv = new VirtualView(clientHandler);

        if (!gameController.isGameStarted()) {
            if (gameController.checkLoginName(name, vv)) {
                clientHandlerMap.put(name, clientHandler);
                gameController.loginHandler(name, vv);
            }
        } else {
            vv.showLoginResult(true, false, null);
            clientHandler.disconnect();
        }

    }

    /**
     * This method removes a client given his nickname.
     *
     * @param name      the VirtualView to be removed.
     * @param notifyEnabled set to {@code true} to enable a lobby disconnection message, {@code false} otherwise.
     */
    public void removeClient(String name, boolean notifyEnabled) {
        clientHandlerMap.remove(name);
        gameController.removeVirtualView(name, notifyEnabled);
        LOGGER.info(() -> "Removed " + name + " from the client list.");
    }

    /**
     * This method forwards a received message from the client to the GameController.
     *
     * @param message the message to be forwarded.
     */
    public void onMessageReceived(Message message) {
        gameController.onMessageReceived(message);
    }

    /**
     * This method manages the disconnection of a client.
     *
     * @param clientHandler the client disconnecting.
     */
    public void onDisconnect(ClientHandler clientHandler) {
        synchronized (lock) {
            String name = getNameFromClientHandler(clientHandler);

            if (name != null) {

                boolean gameStarted = gameController.isGameStarted();
                removeClient(name, !gameStarted); // enable lobby notifications only if the game didn't start yet.

                if(gameController.getTurnController() != null &&
                        !gameController.getTurnController().getNicknameQueue().contains(name)) {
                    return;
                }

                // Resets server status only if the game was already started.
                // Otherwise the server will wait for a new player to connect.
                if (gameStarted) {
                    gameController.broadcastDisconnectionMessage(name, " disconnected from the server. GAME ENDED.");

                    gameController.endGame();
                    clientHandlerMap.clear();
                }
            }
        }
    }


    /**
     * This method returns the corresponding nickname of a ClientHandler.
     *
     * @param clientHandler the client handler.
     * @return the corresponding nickname of a ClientHandler.
     */
    private String getNicknameFromClientHandler(ClientHandler clientHandler) {
        return clientHandlerMap.entrySet()
                .stream()
                .filter(entry -> clientHandler.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }



}
