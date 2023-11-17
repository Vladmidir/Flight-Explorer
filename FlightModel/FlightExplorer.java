package FlightModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import FlightModel.APIs.LocalData.AirportAPI;
import FlightModel.APIs.LocalData.LocalDataFile;
import FlightModel.APIs.WebAPIs.FlightAPIEndPoint;
import FlightModel.Airports.Airport;
import FlightModel.Airports.Location;
import FlightModel.Airports.iataAirport;
import FlightModel.Flights.Flight;
import FlightModel.Flights.RealTimeFlight;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 * This class is responsible for handling the Flight Explorer logic.
 * Additionally, this class is a Factory for the Flight objects.
 */
public class FlightExplorer {
    private ArrayList<Flight> displayedFlights;

    private FlightAPIEndPoint realTimeEndpoint;
    private FlightAPIEndPoint historicEndpoint;
    private LocalDataFile airportsEndpoint;

    /*
    * Constructor
     */
    public FlightExplorer(FlightAPIEndPoint realTimeEndpoint, FlightAPIEndPoint historicEndpoint, LocalDataFile airportsEndpoint) {
        this.realTimeEndpoint = realTimeEndpoint;
        this.historicEndpoint = historicEndpoint;
        this.airportsEndpoint = airportsEndpoint;
    }

    /*
    * get the airport details from the local data file
    * build an airport object
    * return the airport object
    */
    private Airport buildAirport(String iata) {
        return null;
    }

    /*
    * get the airport details from the local data file
    * @param iata1: the departure airport iata
    * @param iata2: the arrival airport iata
    * build two airport objects
    * @return (Airport, Airport): the departure and arrival airports
     */
    private Airport[] getFlightAirports(String iata1, String iata2) {
        return null;
    }

    /*
    * Build a flight object
    * @param flightType: the type of flight (historic or real time)
    * @param params: a hashmap containing the parameters for the API call
     */
    public Flight buildFlight(String flightType, HashMap<String, String> params) {
        return null;
    }

    /*
    * Update the flight information about a specific flight
    * Use flight IATA and Flight number to identify the flight
     */
    public void updateFlightInfo(Flight flight) {
        return;
    }

    /*
    * Get the list of displayed flights
    * @return ArrayList<Flight>: the list of displayed flights
     */
    public ArrayList<Flight> getDisplayedFlights() {
        return this.displayedFlights;
    }

    /*
    * Get a list of historic flights according to the params provided
    * @param params: a hashmap containing the parameters for the API call
    * @return ArrayList<Flight>: a list of historic flights
     */
    public ArrayList<Flight> getHistoricFlights(HashMap<String, String> params) {
        return null;
    }

    /*
    * Get a list of real time flights according to the params provided
    * @param params: a hashmap containing the parameters for the API call
    * @return ArrayList<Flight>: a list of real time flights
     */
    public ArrayList<Flight> getRealTimeFlights(HashMap<String, String> params) {
        //send a request to the endpoint
        String response = this.realTimeEndpoint.search(params);
        //parse the json response
        //build a list of flights
        ArrayList<Flight> flights = new ArrayList<>();

        JSONObject obj = new JSONObject(response);
        JSONArray data = obj.getJSONArray("data");
        for (int i = 0; i < data.length(); i++) {
            JSONObject flight = data.getJSONObject(i);
            //get the flight details
            LocalDate flightDate = LocalDate.parse(flight.getString("flight_date"));
            String flightStatus = flight.getString("flight_status");
            //build the airport objects
            JSONObject depObj = flight.getJSONObject("departure");
            JSONObject arrObj = flight.getJSONObject("arrival");
            String depAirportIATA = depObj.getString("iata");
            String arrAirportIATA = arrObj.getString("iata");
            Airport depAirport = this.buildAirport(depAirportIATA);
            Airport arrAirport = this.buildAirport(arrAirportIATA);
            //Set up the location
            Location location;
            double altitude;
            boolean isGround;
            double direction;
            //check if live location is available
            //if not - set the location to departure airport location
            if (flight.isNull("live")) {
                try {
                    location = depAirport.getLocation();
                } catch (Exception e) {
                    location = new Location(0, 0);
                }
                //location = depAirport.getLocation();
                altitude = 0;
                isGround = true;
                direction = 0;
            }
            //if live location is available - set the location to the live location
            else {
                JSONObject live = flight.getJSONObject("live");
                double longitude = live.getDouble("longitude");
                double latitude = live.getDouble("latitude");
                location = new Location(longitude, latitude);
                altitude = live.getDouble("altitude");
                isGround = live.getBoolean("is_ground");
                direction = live.getDouble("direction");
            }
            //build the flight object
            Flight f = new RealTimeFlight(flightDate, flightStatus, depAirport, arrAirport, location, altitude, isGround, direction);
            //add the flight to the list
            flights.add(f);
        }
        return flights;
    }
}
