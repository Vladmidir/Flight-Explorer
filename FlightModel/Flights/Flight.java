package FlightModel.Flights;

import FlightModel.Airports.Airport;
import FlightModel.Airports.Location;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

public interface Flight {

    /**
     * This method returns flight Date, Status, Departure Airport and Arrival Airport
     * @return HashMap<String, String>: date, status, depAirport, arrAirport
     */
    public HashMap<String, String> getShortDetails();

    /**
     * This method returns all available details about a flight,
     * null if a detail is not available.
     * @return HashMap<String, String>: date, status, depAirport, arrAirport, location, altitude, isGround, direction
     */
    public HashMap<String, String> getLongDetails();

    /**
     * This method returns the departure airport
     */
    public Airport getDepAirport();

    /**
     * This method returns the arrival airport
     */
    public Airport getArrAirport();

    /**
     * This method updates the flight information. NOTE: May want to use a hashmap.
     * Useful for real time flights, so that we don't have to re-create the object
     * @param date: the new date
     * @param status: the new status
     * @param depAirport: the new departure airport
     * @param arrAirport: the new arrival airport
     * @param location: the new location
     * @param altitude: the new altitude
     * @param isGround: the new isGround
     * @param direction: the new direction
     * @return void
     */
    public void updateFlightInfo(LocalDate date, String status, Airport depAirport, Airport arrAirport, Location location, double altitude, boolean isGround, double direction);


}
