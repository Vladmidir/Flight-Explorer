import FlightModel.FlightExplorer;
import javafx.application.Application;
import javafx.stage.Stage;
import views.FlightExplorerView;
import FlightModel.APIs.LocalData.AirportAPI;
import FlightModel.APIs.WebAPIs.RealTimeFlightAPI;
import FlightModel.ConfigReader;

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
        //config reader
        ConfigReader configReader = new ConfigReader();
        try {
            configReader.getPropValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
        AirportAPI airportAPI = new AirportAPI();
        this.model = new FlightExplorer(new RealTimeFlightAPI(System.getProperty("AVIATIONSTACK_KEY")), null, airportAPI); //FlightExplorer NOT IMPLEMENTED #TODO
        this.view = new FlightExplorerView(model, primaryStage);
    }

}