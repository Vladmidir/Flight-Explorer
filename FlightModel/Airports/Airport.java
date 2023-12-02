package FlightModel.Airports;

/**
 * Contains information about an airport.
 */
public interface Airport {

    /**
    * Returns the location of the airport
     */
    public Location getLocation();

    /**
     * Returns the id of the airport
     */
    public String getId();

    /**
     * Returns the name and country of the airport
     */
    public String getDetails();

}
