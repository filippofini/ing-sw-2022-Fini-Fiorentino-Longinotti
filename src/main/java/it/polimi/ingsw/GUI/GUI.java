package it.polimi.ingsw.GUI;

import it.polimi.ingsw.network.client.Client;

import it.polimi.ingsw.view.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;


/**
 * This class manages the graphical user interface.
 */
public class GUI extends Application implements View {

    private Stage stage;
    private Client client;
    private FXMLLoader fxmlLoader;

    @Override
    public void start(Stage stage) throws Exception{
        this.stage = stage;

    }

    private void createMainScene(){

    }

    @Override
    public void handleCloseConnection(boolean was_connected) {

    }
}
