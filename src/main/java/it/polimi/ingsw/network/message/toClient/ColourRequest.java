package it.polimi.ingsw.network.message.toClient;

import it.polimi.ingsw.view.ViewInterface;

/**
 * Message to ask to choose the colour of disk's students.
 */
public class ColourRequest extends MessagesToClient{

    public ColourRequest() { super(true);}

    @Override
    public void handleMessage(ViewInterface view) {
        view.displayColourRequest();
    }

    @Override
    public String toString(){
        return "Asking the colour of disk's student";
    }

}
