package FlightModel.Airports;

/**
 * This class contains information about an airport with an IATA code.
 */
public class iataAirport implements Airport{

    private final Location location;  //Location of the airport
    private final String iata;  //IATA code of the airport
    private final String name;  //Name of the airport
    private final String country; //Country of the airport
    //NOTE: city would be nice to have. Need to find a dataset for it.

    /**
     * Constructor
     */
    public iataAirport(String iata, String name, String country, double lat, double lon){
        this.iata = iata;
        this.name = name;
        this.country = country;
        this.location = new Location(lat, lon);
    }

    @Override
    public String getDetails() {
        return this.name + ", " + this.country;
    }

    @Override
    public String getId() {
        return this.iata;
    }

    @Override
    public Location getLocation() {
        return location;
    }
}
