package FlightModel.APIs.LocalData;

import FlightModel.Airports.iataAirport;

import java.util.ArrayList;
import java.util.List;

public class AirportAPI implements LocalDataFile{
    // get airport by iata
    // get airport by name
    // get airport by country
    // get airports in a radius
    // return raw data from this file, flight explorer will make airport objects using that data


    private List<iataAirport> airports;

    /*
     * Constructor for AirportAPI, creates a list of iataAirport objects.
     */
    public AirportAPI(List<iataAirport> airports) {
        this.airports = airports;
    }


    /*
     * Returns the iataAirport object with the given name.
     */
    public iataAirport getAirportByName(String name) {
        for (iataAirport airport : airports) {
            if (airport.getDetails().contains(name)) {
                return airport;
            }
        }
        throw new IllegalArgumentException("Airport not found");
    }

    /*
     * Returns a list of iataAirport objects in the given country.
     */
    public List<iataAirport> getAirportsByCountry(String country) {
        List<iataAirport> result = new ArrayList<>();
        for (iataAirport airport : airports) {
            if (airport.getDetails().contains(country)) {
                result.add(airport);
            }
        }
        return result;
    }

    /*
     * Returns the iataAirport object with the given iata code.
     */
    public iataAirport getAirportByIata(String iata) {
        for (iataAirport airport : airports) {
            if (airport.getId().equals(iata)) {
                return airport;
            }
        }
        throw new IllegalArgumentException("Airport not found");
    }

    /*
     * Returns a list of iataAirport objects within the given radius of the given center.
     */
    public List<iataAirport> getAirportsInCircle(double centerLat, double centerLon, double radius) {
        List<iataAirport> result = new ArrayList<>();
        for (iataAirport airport : airports) {
            if (inBoundary(airport.getLocation().getLat(), airport.getLocation().getLon(), centerLat, centerLon, radius)) {
                result.add(airport);
            }
        }
        return result;
    }

    /*
     * Helper method for getAirportsInCircle, returns true if the given airport is within the given radius of the given center.
     */
    private boolean inBoundary(double airportLat, double airportLon, double centerLat, double centerLon, double radius) {
        double latDiff = airportLat - centerLat;
        double lonDiff = airportLon - centerLon;
        return (latDiff * latDiff) + (lonDiff * lonDiff) < radius * radius;
    }


}
