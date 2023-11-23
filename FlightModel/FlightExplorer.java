package FlightModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import FlightModel.APIs.LocalData.AirportAPI;
import FlightModel.APIs.LocalData.LocalDataFile;
import FlightModel.APIs.WebAPIs.FlightAPIEndPoint;
import FlightModel.Airports.Airport;
import FlightModel.Airports.iataAirport;
import FlightModel.Flights.Flight;

/*
 * This class is responsible for handling the Flight Explorer logic.
 * Additionally, this class is a Factory for the Flight objects.
 */
public class FlightExplorer {
    private ArrayList<Flight> displayedFlights;
    private FlightAPIEndPoint realTimeEndpoint;
    private FlightAPIEndPoint historicEndpoint;
    private AirportAPI airportsEndpoint;

    /*
    * Constructor
     */
    public FlightExplorer(FlightAPIEndPoint realTimeEndpoint, FlightAPIEndPoint historicEndpoint, AirportAPI airportsEndpoint) {
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
          return airportsEndpoint.getAirportByIata(iata);
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
        return null;
    }
}
