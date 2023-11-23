package tests;
//import java test libraries
import FlightModel.APIs.LocalData.AirportAPI;
import FlightModel.APIs.WebAPIs.*;
import FlightModel.FlightExplorer;
import FlightModel.Flights.Flight;
import FlightModel.ConfigReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

public class FlightExplorerTests {

    static FlightExplorer flightExplorer;

    @BeforeAll
    public static void setUp() {
        //load the config file
        ConfigReader configReader = new ConfigReader();
        try {
            configReader.getPropValues();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FlightAPIEndPoint realTimeFlights = new RealTimeFlightAPI(System.getProperty("AVIATIONSTACK_KEY"));
        FlightAPIEndPoint historicFlights = null;
        AirportAPI airports = new AirportAPI();

        flightExplorer = new FlightExplorer(realTimeFlights, historicFlights, airports);

    }

    @Test
    public void testGetRealTimeFlights() {
        HashMap<String, String> params = new HashMap<>();
        params.put("flight_status", "active");
        List<Flight> result = flightExplorer.getRealTimeFlights(params);
        //assert result is not null
        assert result != null;
        //assert that all flights are active
        for (Flight flight : result) {
            HashMap<String, String> details = flight.getShortDetails();
            assert details.get("status").equals("active");
        }
        System.out.println(result);
    }
}
