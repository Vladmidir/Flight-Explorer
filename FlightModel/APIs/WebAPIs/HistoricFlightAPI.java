package FlightModel.APIs.WebAPIs;

import java.util.HashMap;

/** This endpoint requires paid subscription to aviationstack.com
 * Which we do not have the budget for :(
 *  Therefore, this class is not implemented.
 */
public class HistoricFlightAPI implements FlightAPIEndPoint{
    /**
     *
     * @param params - the parameters to be added to the url
     * @return String: the response body from the api
     */
    @Override
    public String search(HashMap<String, String> params) {
        return null;
    }
}
