import FlightModel.FlightExplorer;
import javafx.application.Application;
import javafx.stage.Stage;
import views.FlightExplorerView;

import java.io.IOException;

/**
 * Class FlightExplorerApp.
 */
public class FlightExplorerApp extends Application {

    FlightExplorer model;
    FlightExplorerView view;

    public static void main(String[] args) {

        launch(args);
    }

    /*
     * JavaFX is a Framework, and to use it we will have to
     * respect its control flow!  To start the app, we need
     * to call "launch" which will in turn call "start" ...
     */
    @Override
    public void start(Stage primaryStage) {
        //TODO: Configure the view and display it.
        this.model = new FlightExplorer(null, null, null); //FlightExplorer NOT IMPLEMENTED #TODO
        this.view = new FlightExplorerView(model,primaryStage);
    }

}