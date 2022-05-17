package it.polimi.ingsw.network.message;

/**
 * Message used by the server to ask students that clients wants to move.
 */
public class MoveStudentRequest_message extends Message{

    //private static final long serialVersionUID =;

        /**
         * Constructor of the class.
         * @param name The name of the player.
         */
        public MoveStudentRequest_message(String name) {
            super(name, MessageType.MOVESTUDENT_REQUEST);
        }

        @Override
        public String toString() {
            return "MoveStudentRequest{" +
                    "name=" + getName() +
                    '}';
        }
}
