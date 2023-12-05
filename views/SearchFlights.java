package views;

import FlightModel.Flights.Flight;

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
        this.flightListJSON = "";
        this.flightsHashMap = new HashMap<Integer, HashMap<String, String>>();
        this.searchResult = new ArrayList<Flight>();




    }

    /**
     * searches by param
     *
     * @param  params the parameter to search by
     * @param  value search term
     * @return an array list of flights
     */
    public ArrayList<Flight> search (String params, String value) {

        for (int i = 0; i < flightList.size(); i++ ){



            if (params.equals("date")){//            flightInfo.put("date",flightList.get(i).getShortDetails().get("date"));
                if (this.flightList.get(i).getShortDetails().get("date").equals(value)){
                    this.searchResult.add(this.flightList.get(i));
                }
            }
            else if (params.equals("flight status")){//            flightInfo.put("status",flightList.get(i).getShortDetails().get("status"));
                if (this.flightList.get(i).getShortDetails().get("status").equals(value)){
                    this.searchResult.add(this.flightList.get(i));
                }
            }
            else if (params.equals("altitude")){//            flightInfo.put("altitude",flightList.get(i).getLongDetails().get("altitude"));
                if (this.flightList.get(i).getLongDetails().get("altitude").equals(value)){
                    this.searchResult.add(this.flightList.get(i));
                }
            }
            else if (params.equals("flight name")){//            flightInfo.put("flightDetail", flightList.get(i).toString().substring(8));
                if (this.flightList.get(i).toString().substring(8).equals(value)){
                    this.searchResult.add(this.flightList.get(i));
                }
            }
            else if (params.equals("grounded")){//            flightInfo.put("isGround",flightList.get(i).getLongDetails().get("isGround"));
                if (this.flightList.get(i).getLongDetails().get("isGround").equals(value)){
                    this.searchResult.add(this.flightList.get(i));
                }
            }
            else if (params.equals("direction")){//            flightInfo.put("direction",flightList.get(i).getLongDetails().get("direction"));
                if (this.flightList.get(i).getLongDetails().get("direction").equals(value)){
                    this.searchResult.add(this.flightList.get(i));
                }
            }
            else if (params.equals("arrival airport")){//            flightInfo.put("arrAirport",flightList.get(i).getShortDetails().get("arrAirport"));
                if (this.flightList.get(i).getShortDetails().get("arrAirport").equals(value)){
                    this.searchResult.add(this.flightList.get(i));
                }
            }
            else if (params.equals("arrival airport ID")){//            flightInfo.put("arrAirportId", flightList.get(i).getArrAirport().getId());
                if (this.flightList.get(i).getArrAirport().getId().equals(value)){
                    this.searchResult.add(this.flightList.get(i));
                }
            }
            else if (params.equals("arrival airport details")){//            flightInfo.put("arrAirportDetail", flightList.get(i).getArrAirport().getDetails());
                if (this.flightList.get(i).getArrAirport().getDetails().equals(value)){
                    this.searchResult.add(this.flightList.get(i));
                }
            }
            else if (params.equals("arrival airport location")){//            flightInfo.put("arrAirportLocation", flightList.get(i).getArrAirport().getLocation().toString());
                if (this.flightList.get(i).getArrAirport().getLocation().toString().equals(value)){
                    this.searchResult.add(this.flightList.get(i));
                }
            }
            else if (params.equals("departure airport")){//            flightInfo.put("depAirport",flightList.get(i).getShortDetails().get("depAirport"));
                if (this.flightList.get(i).getShortDetails().get("depAirport").equals(value)){
                    this.searchResult.add(this.flightList.get(i));
                }
            }
            else if (params.equals("departure airport ID")){//            flightInfo.put("depAirportId", flightList.get(i).getDepAirport().getId());
                if (this.flightList.get(i).getDepAirport().getId().equals(value)){
                    this.searchResult.add(this.flightList.get(i));
                }
            }
            else if (params.equals("departure airport details")){//            flightInfo.put("depAirportDetail", flightList.get(i).getDepAirport().getDetails());
                if (this.flightList.get(i).getDepAirport().getDetails().equals(value)){
                    this.searchResult.add(this.flightList.get(i));
                }
            }
            else if (params.equals("departure airport location")){//            flightInfo.put("depAirportLocation", flightList.get(i).getDepAirport().getLocation().toString());
                if (this.flightList.get(i).getDepAirport().getLocation().toString().equals(value)){
                    this.searchResult.add(this.flightList.get(i));
                }
            }
            else if (params.equals("location of flight")){//            flightInfo.put("location",flightList.get(i).getLongDetails().get("location"));
                if (this.flightList.get(i).getLongDetails().get("location").equals(value)){
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
        return this.flightListJSON;
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
