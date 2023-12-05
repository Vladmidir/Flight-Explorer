package tests;

import FlightModel.APIs.LocalData.AirportAPI;
import FlightModel.Airports.iataAirport;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class AirportAPITests {

    private AirportAPI airportAPI;
    private List<iataAirport> airports;

    @Before
    public void setUp() {
        airports = new ArrayList<>();
        airports.add(new iataAirport("JFK", "John F Kennedy International", "USA", 40.6413, -73.7781));
        airports.add(new iataAirport("LHR", "Heathrow Airport", "UK", 51.4700, -0.4543));
        airports.add(new iataAirport("HND", "Haneda Airport", "Japan", 35.5494, 139.7798));
        airports.add(new iataAirport("CDG", "Charles de Gaulle Airport", "France", 49.0097, 2.5479));
        airports.add(new iataAirport("DXB", "Dubai International Airport", "UAE", 25.2532, 55.3657));
        airportAPI = new AirportAPI();
    }

    @Test
    public void testGetAirportByIATA_Valid() {
        iataAirport airport = airportAPI.getAirportByIata("JFK");
        assertNotNull("Airport should be found", airport);
        assertEquals("JFK", airport.getId());
        System.out.println("Expected result: JFK");
        System.out.println("Actual result: " + airport.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAirportByIATA_Invalid() {
        try {
            airportAPI.getAirportByIata("XYZ");
        } catch (IllegalArgumentException e) {
            System.out.println("Expected result: IllegalArgumentException");
            System.out.println("Actual result: " + e.getClass().getSimpleName());
            throw e;
        }
    }

    @Test
    public void testGetAirportByName_Valid() {
        iataAirport airport = airportAPI.getAirportByName("Heathrow Airport");
        assertNotNull("Airport should be found", airport);
        assertTrue(airport.getDetails().contains("Heathrow Airport"));
        System.out.println("Expected result: Heathrow Airport, UK");
        System.out.println("Actual result: " + airport.getDetails());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAirportByName_Invalid() {
        try {
            airportAPI.getAirportByName("Mars Spaceport");
        } catch (IllegalArgumentException e) {
            System.out.println("Expected result: IllegalArgumentException");
            System.out.println("Actual result: " + e.getClass().getSimpleName());
            throw e;
        }
    }

    @Test
    public void testGetAirportsByCountry_Valid() {
        List<iataAirport> foundAirports = airportAPI.getAirportsByCountry("US");
        assertFalse("Should find airports in USA", foundAirports.isEmpty());
        assertTrue("Should contain JFK airport", foundAirports.stream().anyMatch(a -> a.getId().equals("JFK")));
        System.out.println("Expected result: Airports in USA including JFK");
        System.out.println("Actual result: " + foundAirports.size() + " airports, contains JFK: " +
                foundAirports.stream().anyMatch(a -> a.getId().equals("JFK")));
    }

    @Test
    public void testGetAirportsByCountry_Invalid() {
        List<iataAirport> foundAirports = airportAPI.getAirportsByCountry("Atlantis");
        assertTrue("Should find no airports in a non-existent country", foundAirports.isEmpty());
        System.out.println("Expected result: No airports in Atlantis");
        System.out.println("Actual result: " + foundAirports.size() + " airports");
    }

    @Test
    public void testAirportsWithinSmallRadius() {
        List<iataAirport> foundAirports = airportAPI.getAirportsInCircle(40.6413, -73.7781, 1);
        assertEquals("Should find 22 airport within a small radius", 22, foundAirports.size());
        assertEquals("TEB", foundAirports.get(0).getId());
        System.out.println("Expected result: 1 airport (TEB)");
        System.out.println("Actual result: " + foundAirports.size() + " airport (" + foundAirports.get(0).getId() + ")");
    }

    @Test
    public void testAirportsWithinLargeRadius() {
        List<iataAirport> foundAirports = airportAPI.getAirportsInCircle(51.4700, -0.4543, 100000);
        assertTrue("Should find multiple airports within a large radius", foundAirports.size() > 1);
        System.out.println("Expected result: 5 airports");
        System.out.println("Actual result: " + foundAirports.size() + " airports");
    }

    @Test
    public void testNoAirportsWithinVerySmallRadius() {
        double lat = 25.2532;
        double lon = 59.3657;
        double radius = 0.1;
        List<iataAirport> foundAirports = airportAPI.getAirportsInCircle(lat, lon, radius);

        if (!foundAirports.isEmpty()) {
            System.out.println("Airports found within a very small radius:");
            for (iataAirport airport : foundAirports) {
                System.out.println(airport.getId() + ": " + airport.getDetails());
            }
        }

        assertTrue("Should find no airports within a very small radius", foundAirports.isEmpty());
    }

    @Test
    public void testAirportsOutsideCircle() {
        // Coordinates and radius chosen to ensure they don't include Heathrow Airport
        double lat = 35.5494;  // Latitude for Haneda Airport
        double lon = 139.7798; // Longitude for Haneda Airport
        double radius = 100;   // A smaller radius

        List<iataAirport> foundAirports = airportAPI.getAirportsInCircle(lat, lon, radius);
        iataAirport heathrow = airports.stream().filter(a -> "LHR".equals(a.getId())).findFirst().orElse(null);

        assertFalse("Should not include airports outside the circle", foundAirports.contains(heathrow));
        System.out.println("Expected result: No airports outside the circle");
        System.out.println("Actual result: " + foundAirports.size() + " airports, contains LHR: " +
                (heathrow != null && foundAirports.contains(heathrow)));
    }

    @Test
    public void testGetIataFromCity_Valid() {
        ArrayList<String> iata = airportAPI.getIataFromCity("New York");
        assertNotNull("Airport should be found", iata);
        assertTrue(iata.contains("JFK"));
        assertNotNull(airportAPI.getIataFromCity("Toronto"));
    }
}