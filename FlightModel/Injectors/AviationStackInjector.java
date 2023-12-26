package FlightModel.Injectors;

import FlightModel.APIs.LocalData.AirportAPI;
import FlightModel.APIs.WebAPIs.HistoricFlightAPI;
import FlightModel.APIs.WebAPIs.RealTimeFlightAPI;
import FlightModel.ConfigReader;
import FlightModel.FlightExplorer;
import views.FlightExplorerView;

/**
 * This class is used to inject a AviationStack Web API into the FlightExplorer class.
 */
public class AviationStackInjector implements FlightAPIInjector{
    @Override
    public FlightExplorer buildFlightExplorer() {
        //set up the config
        ConfigReader configReader = new ConfigReader();
        try {
            configReader.getPropValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //set up the airport API
        AirportAPI airportAPI = new AirportAPI();
        //set up the real time flight API
        RealTimeFlightAPI realTimeFlightAPI = new RealTimeFlightAPI(System.getProperty("AVIATIONSTACK_KEY"));
        //return the flight explorer
        return new FlightExplorer(realTimeFlightAPI, null, airportAPI);
    }
}
