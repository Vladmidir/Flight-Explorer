package FlightModel.Flights;

import FlightModel.Airports.Airport;
import FlightModel.Airports.Location;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
/**
 * This class contains information about a historic flight
 */
public class HistoricFlight implements Flight{

    private LocalDate date;   //Date of the flight
    private String status;    //Status of the flight
    private Airport depAirport;     //Departure airport
    private Airport arrAirport;     //Arrival airport

    /**
    * Constructor
     */
    public HistoricFlight(LocalDate date, String status, Airport depAirport, Airport arrAirport) {
        this.date = date;
        this.status = status;
        this.depAirport = depAirport;
        this.arrAirport = arrAirport;
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
        return this.getShortDetails();
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
    }
}
