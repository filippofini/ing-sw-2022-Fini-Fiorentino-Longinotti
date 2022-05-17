package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Student;

/**
 * Message used by the client to select the students to move
 */
public class MoveStudent_reply extends Message{

   // private static final long serialVersionUID =;
    private final Student []students;

    /**
     * Constructor of the class.
     * @param name The name of the player.
     * @param students Array of students.
     */
    public MoveStudent_reply(String name, Student[]students) {
        super(name, MessageType.MOVESTUDENT_REPLY);
        this.students = students;
    }

    public Student[] getStudents() {
        return students;
    }

    @Override
    public String toString() {
        return "MoveStudent_reply{" +
                "name=" + getName() +
                ", students=" + students +
                '}';
    }

}
