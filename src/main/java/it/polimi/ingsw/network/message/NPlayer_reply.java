package it.polimi.ingsw.network.message;

/**
 * Message used to send to the server the number of players picked by the client.
 */
public class NPlayer_reply extends Message{

    private static final long serialVersionUID = -4419241297635925047L;
    private final int n_players;

    /**
     * Constructor of the class.
     * @param name The name of the player.
     * @param n_players The number of players.
     */
    public NPlayer_reply(String name, int n_players) {
        super(name, MessageType.NPLAYER_REPLY);
        this.n_players = n_players;
    }

    public int getN_players() {
        return n_players;
    }

    @Override
    public String toString() {
        return "NPlayer_reply{" +
                "name=" + getName() +
                ", n_players=" + n_players +
                '}';
    }
}
