package FlightModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import FlightModel.APIs.LocalData.AirportAPI;
import FlightModel.APIs.WebAPIs.FlightAPIEndPoint;
import FlightModel.Airports.Airport;
import FlightModel.Airports.Location;
import FlightModel.Airports.iataAirport;
import FlightModel.Flights.Flight;
import FlightModel.Flights.RealTimeFlight;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class is responsible for handling the Flight Explorer logic.
 * Additionally, this class is a Factory for the Flight objects.
 */
public class FlightExplorer {
    private ArrayList<Flight> displayedFlights; //list of the flights currently displayed on the map
    private final FlightAPIEndPoint realTimeEndpoint; //used to get real time flights
    private final FlightAPIEndPoint historicEndpoint; //used to get historic flights
    private final AirportAPI airportsEndpoint; //used to get airport objects

    /**
    * Constructor
     */
    public FlightExplorer(FlightAPIEndPoint realTimeEndpoint, FlightAPIEndPoint historicEndpoint, AirportAPI airportsEndpoint) {
        this.realTimeEndpoint = realTimeEndpoint;
        this.historicEndpoint = historicEndpoint;
        this.airportsEndpoint = airportsEndpoint;
    }

    /***
    * get the airport details from the local data file
    * build an airport object
    * return the airport object
    */
    private Airport buildAirport(String iata) {
          return airportsEndpoint.getAirportByIata(iata);
    }


    /**
    * Build a flight object
    * @param flightType: the type of flight (historic or real time)
    * @param details: a hashmap containing the flight details. Note that "live" may be in json format.
     *               Otherwise, live parameters must be provided.
     */
    public Flight buildFlight(String flightType, HashMap<String, String> details) {
        flightType = flightType.toLowerCase();
        flightType = flightType.replaceAll("\\s", "");
        if (flightType.equals("historic")) {
            throw new IllegalArgumentException("Historic flights are not supported");
        } else if (flightType.equals("realtime")) {
            //get the flight details
            LocalDate flightDate = LocalDate.parse(details.get("flight_date"));
            String flightStatus = details.get("flight_status");
            //build the airport objects
            String depAirportIATA = details.get("dep_iata");
            String arrAirportIATA = details.get("arr_iata");
            Airport depAirport = this.buildAirport(depAirportIATA);
            Airport arrAirport = this.buildAirport(arrAirportIATA);
            if (depAirport == null || arrAirport == null) {
                return null;
            }
            //Set up the location
            Location location;
            double altitude;
            boolean isGround;
            double direction;
            //check if live location is available
            //if not - set the location to departure airport location
            if (details.get("live") == null) {
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
                //check if live in json format
                try {
                    JSONObject live = new JSONObject(details.get("live"));
                    double longitude = live.getDouble("longitude");
                    double latitude = live.getDouble("latitude");
                    location = new Location(longitude, latitude);
                    altitude = live.getDouble("altitude");
                    isGround = live.getBoolean("is_ground");
                    direction = live.getDouble("direction");
                } catch (Exception e) {
                    double longitude = Double.parseDouble(details.get("longitude"));
                    double latitude = Double.parseDouble(details.get("latitude"));
                    location = new Location(longitude, latitude);
                    altitude = Double.parseDouble(details.get("altitude"));
                    isGround = Boolean.parseBoolean(details.get("is_ground"));
                    direction = Double.parseDouble(details.get("direction"));
                }
            }
            //build the flight object
            return new RealTimeFlight(flightDate, flightStatus, depAirport, arrAirport, location, altitude, isGround, direction);
        } else {
            throw new IllegalArgumentException("Invalid flight type");
        }
    }

    /**
    * Update the flight information about a specific flight
    * Use flight IATA and Flight number to identify the flight
     */
    public void updateFlightInfo(Flight flight) {
        return;
    }

    /**
    * Get the list of displayed flights
    * @return ArrayList<Flight>: the list of displayed flights
     */
    public ArrayList<Flight> getDisplayedFlights() {
        return this.displayedFlights;
    }

    /**
    * Get a list of historic flights according to the params provided
    * @param params: a hashmap containing the parameters for the API call
    * @return ArrayList<Flight>: a list of historic flights
    * CAN NOT BE IMPLEMENTED DUE TO API LIMITATIONS
     */
    public ArrayList<Flight> getHistoricFlights(HashMap<String, String> params) {
        return null;
    }

    /**
    * Get a list of real time flights according to the params provided
    * @param params: a hashmap containing the parameters for the API call
    * @return ArrayList<Flight>: a list of real time flights
     */
    public ArrayList<Flight> getRealTimeFlights(HashMap<String, String> params) {
        //send a request to the endpoint
        String response = this.realTimeEndpoint.search(params);
        //build a list of flights
        ArrayList<Flight> flights = new ArrayList<>();
        //parse the json response
        JSONObject obj = new JSONObject(response);
        JSONArray data = obj.getJSONArray("data");
        for (int i = 0; i < data.length(); i++) {
            HashMap<String, String> flightDetails = new HashMap<>();
            JSONObject flight = data.getJSONObject(i);
            //get the flight details
            flightDetails.put("flight_date", flight.getString("flight_date"));
            flightDetails.put("flight_status", flight.getString("flight_status"));

            JSONObject depObj = flight.getJSONObject("departure");
            JSONObject arrObj = flight.getJSONObject("arrival");
            flightDetails.put("dep_iata", depObj.getString("iata")) ;
            flightDetails.put("arr_iata", arrObj.getString("iata")) ;
            //Set up the location
            Location location;
            //check if live location is available
            //if not - set the location to departure airport location
            //if live location is available - set the location to the live location
            if (!flight.isNull("live")) {
                flightDetails.put("live", flight.getJSONObject("live").toString());
            }
            //build the flight object
            Flight f = buildFlight("realtime", flightDetails);
            //add the flight to the list
            if (f != null){
                flights.add(f);
            }
        }
        return flights;
    }
}
