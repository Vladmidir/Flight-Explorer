package FlightModel.Flights;

import FlightModel.Airports.Airport;
import FlightModel.Airports.Location;

import java.time.LocalDate;
//import java.util.Date;
import java.util.HashMap;

/**
 * This class contains information about a real time flight
 */
public class RealTimeFlight implements Flight{

    private LocalDate date;    //Date of the flight
    private String status;      //Status of the flight
    private Airport depAirport;     //Departure airport
    private Airport arrAirport;     //Arrival airport
    private Location location;    //Live location of the plane
    private double altitude;    //Live altitude of the plane
    private boolean isGround;    //Is the plane on the ground?
    private double direction;    //Live direction of the plane


    /**
     * Constructor
     */
    public RealTimeFlight(LocalDate date, String status, Airport depAirport, Airport arrAirport, Location location, double altitude, boolean isGround, double direction) {
        this.date = date;
        this.status = status;
        this.depAirport = depAirport;
        this.arrAirport = arrAirport;
        this.location = location;
        this.altitude = altitude;
        this.isGround = isGround;
        this.direction = direction;
    }

    @Override
    public HashMap<String, String> getShortDetails() {
        HashMap<String, String> flightDetails = new HashMap<String, String>();

        flightDetails.put("date", this.date.toString());
        flightDetails.put("status", this.status);
        flightDetails.put("depAirport", this.depAirport.getDetails());
        flightDetails.put("arrAirport", this.arrAirport.getDetails());

        return flightDetails;
    }

    @Override
    public HashMap<String, String> getLongDetails() {
        HashMap<String, String> flightDetails = this.getShortDetails();
        flightDetails.put("location", this.location.toString());
        flightDetails.put("altitude", Double.toString(this.altitude));
        flightDetails.put("isGround", Boolean.toString(this.isGround));
        flightDetails.put("direction", Double.toString(this.direction));

        return flightDetails;
    }

    @Override
    public Airport getDepAirport() {
        return this.depAirport;
    }

    @Override
    public Airport getArrAirport() {
        return this.arrAirport;
    }

    @Override
    public void updateFlightInfo(LocalDate date, String status, Airport depAirport, Airport arrAirport, Location location, double altitude, boolean isGround, double direction) {
        this.date = date;
        this.status = status;
        this.depAirport = depAirport;
        this.arrAirport = arrAirport;
        this.location = location;
        this.altitude = altitude;
        this.isGround = isGround;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "Flight: " + this.depAirport.getId() + " to " + this.arrAirport.getId() + " on " + this.date.toString();
    }
}
