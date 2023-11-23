package FlightModel.APIs.LocalData;

import FlightModel.Airports.iataAirport;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AirportAPI implements LocalDataFile{
    // get airport by iata
    // get airport by name
    // get airport by country
    // get airports in a radius
    // return raw data from this file, flight explorer will make airport objects using that data
    // populate the airports list in the constructor


    private List<iataAirport> airports;

    /*
     * load the data from the local data file into the airports list
     */
    private void loadDataFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip the header line
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    iataAirport airport = createAirportFromCSV(parts);
                    airports.add(airport);
                } else {
                    System.err.println("Invalid CSV line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private iataAirport createAirportFromCSV(String[] data) {
        String country = data[0].replaceAll("\"", "");
        String region = data[1].replaceAll("\"", "");
        String iata = data[2].replaceAll("\"", "");
        String icao = data[3].replaceAll("\"", "");
        String name = data[4].replaceAll("\"", "");
        double latitude = Double.parseDouble(data[5].replaceAll("\"", ""));
        double longitude = Double.parseDouble(data[6].replaceAll("\"", ""));

        // Construct iataAirport object
        return new iataAirport(iata, name, country, latitude, longitude);
    }

    /*
     * Constructor for AirportAPI, creates a list of iataAirport objects that can be used to get airport information, populated from the local data file.
     */
    public AirportAPI() {
        this.airports = new ArrayList<>();
        loadDataFromFile("FlightModel/dataFiles/iataAirports.csv");
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
