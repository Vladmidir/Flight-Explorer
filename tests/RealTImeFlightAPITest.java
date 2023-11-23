package tests;
//import java test libraries
import FlightModel.APIs.WebAPIs.FlightAPIEndPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
//import config reader
import FlightModel.ConfigReader;

import java.util.HashMap;

public class RealTImeFlightAPITest {
    @BeforeAll
    public static void setUp() {
        //load the config file
        ConfigReader configReader = new ConfigReader();
        try {
            configReader.getPropValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //write tests for RealTimeFlightAPI
    @Test
    public void testSearch() {
        HashMap<String, String> params = new HashMap<>();
        params.put("flight_status", "active");
        FlightAPIEndPoint realTimeFlights = new FlightModel.APIs.WebAPIs.RealTimeFlightAPI(System.getProperty("AVIATIONSTACK_KEY"));
        String result = realTimeFlights.search(params);
        System.out.println(result);
    }


}
