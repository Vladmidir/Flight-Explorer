package views;

import FlightModel.Flights.Flight;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Class SearchFlights.
 * This class searches and fliters the flight list.
 */
public class SearchFlights {

    private HashMap<Integer, HashMap<String, String>> flightsHashMap;

    private String flightListJSON;

    private ArrayList<Flight> flightList;

    private ArrayList<Flight> searchResult;


    /**
     * Constructor
     *
     * @param  flightList List of all flights
     */
    public SearchFlights(ArrayList<Flight> flightList) {


        this.flightList = flightList;
        this.flightsHashMap = new HashMap<Integer, HashMap<String, String>>();
        this.searchResult = flightList;
        this.flightListJSON = this.getFlightListJSON();



    }

    /**
     * searches by param
     *
     * @param  params the parameter to search by
     * @param  value search term
     * @return an array list of flights
     */
    public ArrayList<Flight> search (String params, String value) {
        this.searchResult = new ArrayList<Flight>();
        ConvertToJSON searchHash = new ConvertToJSON(this.flightList);
        searchHash.createHashMap();
        HashMap<Integer, HashMap<String, String>> flightsHashMap = searchHash.getHashMap();
        HashMap<String, String> syn = new HashMap<String, String>();
        syn.put("date", "date");
        syn.put("flight status", "status");
        syn.put("altitude", "altitude");
        syn.put("flight name", "flightDetail");
        syn.put("grounded", "isGround");
        syn.put("direction", "direction");
        syn.put("arrival airport", "arrAirport");
        syn.put("arrival airport ID", "arrAirportId");
        syn.put("arrival airport details", "arrAirportDetail");
        syn.put("arrival airport location", "arrAirportLocation");
        syn.put("departure airport", "depAirport");
        syn.put("departure airport ID", "depAirportId");
        syn.put("departure airport details", "depAirportDetail");
        syn.put("departure airport location", "depAirportLocation");
        syn.put("location of flight", "location");

        params = syn.get(params);






        for (int i = 0; i < flightsHashMap.size(); i++ ) {

            if (flightsHashMap.get(i).get(params) != null) {
                if (flightsHashMap.get(i).get(params).equals(value)) {
                    this.searchResult.add(this.flightList.get(i));
                }

            }


        }
        return this.searchResult;


    }

    /**
     * getter for JSON
     *
     * @return a string in JSON format
     */
    public String getJSONSearched(){
        ConvertToJSON convertToJSON = new ConvertToJSON(this.searchResult);
        return convertToJSON.getFlightListJSON();
    }


    /**
     * getter for search result
     *
     * @return an arraylist of flights
     */
    public ArrayList<Flight> getSearchResult() {
        return this.searchResult;
    }


    /**
     * getter for search result
     *
     * @return an arraylist of flights
     */
    public String getFlightListJSON() {

        return new ConvertToJSON(this.flightList).getFlightListJSON();
    }



    /**
     * getter for all flights
     *
     * @return an arraylist of all flights
     */
    public ArrayList<Flight> getFlightList() {
        return this.flightList;
    }






}
