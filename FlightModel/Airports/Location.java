package FlightModel.Airports;

/***
 * Location class, to hold Airport locations
 */
public class Location {

    private final double lat; //once initialized, these are fixed
    private final double lon;

    /***
     * Make a Location.
     *
     * @param lat latitude
     * @param lon longitude
     */
    public Location(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    /***
     * Getter
     *
     * @return  latitude
     */
    public double getLat() {
        return lat;
    }

    /***
     * Getter
     *
     * @return  longitude
     */
    public double getLon() {
        return lon;
    }

    /***
     * Getter
     *
     * @return  array of doubles, containing longitude and longitude coordinates
     */
    public double[] getCoords() {
        return new double[]{this.lat, this.lon};
    }

    /**
     * @see Object.toString()
     */
    @Override public String toString() {
        return "[" + this.getLat()
                + ", " + this.getLon()
                + "]";
    }

    /**
     * @see Object.equals()
     */
    @Override public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Location)) {
            return false;
        }

        Location that = (Location) thatObject;
        return this.lat == that.lat
                && this.lon == that.lon;
    }

    /**
     * @see Object.hashCode()
     */
    @Override public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) this.lat;
        result = prime * result + (int) this.lon;
        return result;
    }


    /***
     * Tests if the given (lon, lat) coordinate is within a given radius
     *
     * @param lat latitude
     * @param lon longitude
     * @param radius radius
     * @return true, if lat,lon coordinate is within radius
     */
    public boolean inBoundary(double lat, double lon, double radius) {
        return (this.getLat() - lat) * (this.getLat() - lat) + (this.getLon() - lon) * (this.getLon() - lon) < radius * radius;
    }
}
