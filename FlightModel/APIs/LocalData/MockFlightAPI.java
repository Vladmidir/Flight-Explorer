package FlightModel.APIs.LocalData;

import FlightModel.APIs.WebAPIs.FlightAPIEndPoint;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * This class is used to mock a flight API endpoint.
 * It is used to test the FlightExplorer class.
 */
public class MockFlightAPI implements FlightAPIEndPoint {
    private String data;    // The data to be returned by the search function

    /**
     * Constructor for MockFlightAPI
     * @param filePath - the path to the file containing the data to be returned by the search function
     * @throws IOException - if the file is not found
     */
    public MockFlightAPI(String filePath) throws IOException {
        try {
            this.data = Files.readString(Path.of(filePath));
        } catch (IOException e) {
            throw new IOException(filePath + " not found");
        }
    }

    @Override
    public String search(HashMap<String, String> params) {
        return data;
    }
}
