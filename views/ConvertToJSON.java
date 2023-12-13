package views;

import FlightModel.Airports.Airport;
import FlightModel.Flights.Flight;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class ConvertToJSON.
 * This class converts the flight list to JSON format.
 */
public class ConvertToJSON {
    private String flightListJSON = "";
    private ArrayList<Flight> flightList;
    public HashMap<Integer, HashMap<String, String>> flightsHashMap = new HashMap<Integer, HashMap<String, String>>();

    /**
     * Constructor
     *
     * @param  flightList List of all flights
     */
    public ConvertToJSON(ArrayList<Flight> flightList) {
        this.flightList = flightList; // take the list
        String temp = "{"; // start of JSON
        for (int i = 0; i < flightList.size(); i++) { // loop through the list
            HashMap<String, String> flightInfo = new HashMap<>(); // everything below this takes all the values in the flight object and puts them into the string as JSON format
            Airport arrAirport = flightList.get(i).getArrAirport();
            Airport depAirport = flightList.get(i).getDepAirport();
            flightInfo.put("date",flightList.get(i).getShortDetails().get("date"));
            flightInfo.put("status",flightList.get(i).getShortDetails().get("status"));
            flightInfo.put("depAirport",flightList.get(i).getShortDetails().get("depAirport"));
            flightInfo.put("arrAirport",flightList.get(i).getShortDetails().get("arrAirport"));
            flightInfo.put("location",flightList.get(i).getLongDetails().get("location"));
            flightInfo.put("altitude",flightList.get(i).getLongDetails().get("altitude"));
            flightInfo.put("isGround",flightList.get(i).getLongDetails().get("isGround"));
            flightInfo.put("direction",flightList.get(i).getLongDetails().get("direction"));
            flightInfo.put("arrAirportId", arrAirport.getId());
            flightInfo.put("arrAirportDetail", arrAirport.getDetails());
            flightInfo.put("arrAirportLocation", arrAirport.getLocation().toString());
            flightInfo.put("depAirportId", depAirport.getId());
            flightInfo.put("depAirportDetail", depAirport.getDetails());
            flightInfo.put("depAirportLocation", depAirport.getLocation().toString());
            flightInfo.put("flightDetail", flightList.get(i).toString().substring(8));


            temp += "\"" + i + "\"" +":{";
            temp += "\"arrAirportId\":" + "\""+arrAirport.getId()+"\",";
            temp += "\"arrAirportDetail\":";
            temp += "\""+arrAirport.getDetails()+"\",";
            temp += "\"arrAirportLocation\":"+arrAirport.getLocation().toString()+",";
            temp += "\"ShortDetails\":{" + "\"arrAirport\":\""+flightList.get(i).getShortDetails().get("arrAirport")+"\",";
            temp += "\"status\":\""+flightList.get(i).getShortDetails().get("status")+"\",";
            temp += "\"depAirport\":\""+flightList.get(i).getShortDetails().get("depAirport")+"\",";
            temp += "\"date\":\""+flightList.get(i).getShortDetails().get("date")+"\"},";
            temp += "\"depAirportLocation\":"+depAirport.getLocation().toString()+",";
            temp += "\"depAirportDetail\":"+"\""+depAirport.getDetails()+"\",";
            temp += "\"depAirportId\":"+"\""+depAirport.getId()+"\",";
            temp += "\"LongDetails\":{" + "\"isGround\":\""+flightList.get(i).getLongDetails().get("isGround")+"\",";
            temp += "\"direction\":"+flightList.get(i).getLongDetails().get("direction")+",";
            temp += "\"altitude\":"+flightList.get(i).getLongDetails().get("altitude")+",";
            temp += "\"location\":"+flightList.get(i).getLongDetails().get("location")+"},";
            temp += "\"flightDetail\":"+"\""+flightList.get(i).toString().substring(8)+"\"},";
            this.flightsHashMap.put(i, flightInfo);
        }
        temp = temp.substring(0, temp.length() - 1); // remove last comma
        temp += "}";
        if (temp.equals("}")) { // if the flightlist is empty, make the JSON = {}
            temp = "{}";
        }
        this.flightListJSON = temp;

    }

    /**
     * getter for search result
     *
     * @return an arraylist of flights
     */
    public String getFlightListJSON() {
        return this.flightListJSON;
    }

    /**
     * getter for entire flight list
     *
     * @return an arraylist of flights
     */
    public ArrayList<Flight> getFlightList() {
        return this.flightList;
    }


    /**
     * creates a hashmap for flight objects attributes
     *
     * @return hashmap of flights details
     */
    public void createHashMap() {
        HashMap<Integer, HashMap<String, String>> flightDetails = new HashMap<Integer, HashMap<String, String>>(); // does the same thing as the constructor but instead does it to a hashmap
        for (int i = 0; i < flightList.size(); i++) {
            Airport arrAirport = flightList.get(i).getArrAirport();
            Airport depAirport = flightList.get(i).getDepAirport();
            HashMap<String, String> flightInfo = new HashMap<>();
            flightInfo.put("date",flightList.get(i).getShortDetails().get("date"));
            flightInfo.put("status",flightList.get(i).getShortDetails().get("status"));
            flightInfo.put("depAirport",flightList.get(i).getShortDetails().get("depAirport"));
            flightInfo.put("arrAirport",flightList.get(i).getShortDetails().get("arrAirport"));
            flightInfo.put("location",flightList.get(i).getLongDetails().get("location"));
            flightInfo.put("altitude",flightList.get(i).getLongDetails().get("altitude"));
            flightInfo.put("isGround",flightList.get(i).getLongDetails().get("isGround"));
            flightInfo.put("direction",flightList.get(i).getLongDetails().get("direction"));

            flightInfo.put("flightDetail", flightList.get(i).toString().substring(8));
            flightInfo.put("arrAirportDetail", arrAirport.getDetails());
            flightInfo.put("arrAirportId", arrAirport.getId());
            flightInfo.put("arrAirportLocation", arrAirport.getLocation().toString());
            flightInfo.put("depAirportDetail", depAirport.getDetails());
            flightInfo.put("depAirportId", depAirport.getId());
            flightInfo.put("depAirportLocation", depAirport.getLocation().toString());
            flightDetails.put(i, flightInfo);
        }
        this.flightsHashMap = flightDetails;
    }

    /**
     * getter for flights with hashmap bales
     *
     * @return gets hashmap
     */
    public HashMap<Integer, HashMap<String, String>> getHashMap() {
        return this.flightsHashMap;
    }
}