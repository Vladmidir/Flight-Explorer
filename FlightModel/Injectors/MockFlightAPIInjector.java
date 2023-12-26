package FlightModel.Injectors;

import FlightModel.APIs.LocalData.AirportAPI;
import FlightModel.APIs.LocalData.MockFlightAPI;
import FlightModel.FlightExplorer;
import java.io.IOException;
import java.nio.file.Path;

/**
 * This class is used to inject a local data, mock flight API into the FlightExplorer class.
 */
public class MockFlightAPIInjector implements FlightAPIInjector{
    private String path;

    /**
     * Constructor for MockFlightAPIInjector
     * @param path - the path to the file containing the data to be returned by the search function
     */
    public MockFlightAPIInjector(Path path) {
        this.path = path.toAbsolutePath().toString();
    }
    @Override
    public FlightExplorer buildFlightExplorer() throws IOException {
        //set up the airport API
        AirportAPI airportAPI = new AirportAPI();
        //set up the real time flight API
        try {
            MockFlightAPI mockFlightAPI = new MockFlightAPI(path);
            return new FlightExplorer(mockFlightAPI, null, airportAPI);
        } catch (IOException e) {
            throw e;
        }
    }
}
