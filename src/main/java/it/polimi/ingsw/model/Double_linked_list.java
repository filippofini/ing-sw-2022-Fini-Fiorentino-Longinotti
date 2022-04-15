package it.polimi.ingsw.model;

//This class creates a double linked list of Island objects + append at the end + delete

public class Double_linked_list {
    Node head;

    class Node{
        Island data;
        Node prev;
        Node next;


        public Node(Island data) {
            this.data = data;
        }
    }


    public void append(Island new_data) {

        Node new_node = new Node(new_data);

        Node last = head;

        //This new node is going to be the last node, so make next of it as NULL
        new_node.next = null;

        //If the Linked List is empty, then make the new node as head
        if (head == null) {
            new_node.prev = null;
            head = new_node;
            return;
        }

        //Else traverse until the last node
        while (last.next != null)
            last = last.next;

        //Change the next of last node
        last.next = new_node;

        //Make last node as previous of new node
        new_node.prev = last;
    }


    public void delete_node(Node del){

        //Base case
        if (head == null || del == null) {
            return;
        }

        //If node to be deleted is head node
        if (head == del) {
            head = del.next;
        }

        //Change next only if node to be deleted is NOT the last node
        if (del.next != null) {
            del.next.prev = del.prev;
        }

        //Change prev only if node to be deleted is NOT the first node
        if (del.prev != null) {
            del.prev.next = del.next;
        }
    }

    //TODO: answer the following questions in merge island
    public void merge_islands(Node final_island, Node deleted_island){
        //Sum of students on both islands
        int[] students1 = final_island.data.getArr_students();
        int[] students2 = deleted_island.data.getArr_students();
        for (int i = 0; i < 5; i++) {
            students1[i] += students2[i];
        }
        final_island.data.setArr_students(students1);

        //Do towers merge??? If no delete
        final_island.data.setTower(final_island.data.getTower()+deleted_island.data.getTower());

        //If mother nature is in the deleted island move it to the final island
        if(deleted_island.data.check_mn()){
            final_island.data.setMother_nature(true);
        }





        final_island.data.calculate_influence();

        delete_node(deleted_island);

    }

}
