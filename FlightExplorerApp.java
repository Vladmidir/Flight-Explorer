import FlightModel.FlightExplorer;
import FlightModel.Injectors.AviationStackInjector;
import FlightModel.Injectors.FlightAPIInjector;
import FlightModel.Injectors.MockFlightAPIInjector;
import javafx.application.Application;
import javafx.stage.Stage;
import views.FlightExplorerView;
import FlightModel.APIs.LocalData.AirportAPI;
import FlightModel.APIs.WebAPIs.RealTimeFlightAPI;
import FlightModel.ConfigReader;

import java.io.IOException;
import java.nio.file.Paths;

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
        FlightAPIInjector injector = new MockFlightAPIInjector(Paths.get("./FlightModel/dataFiles/mockFlightData.json"));
//        FlightAPIInjector injector = new AviationStackInjector();
        try {
            this.model = injector.buildFlightExplorer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.view = new FlightExplorerView(model, primaryStage);
    }

}