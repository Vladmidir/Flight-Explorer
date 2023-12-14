package FlightModel.APIs.WebAPIs;

import java.util.HashMap;

public interface FlightAPIEndPoint {

    /**
    * Query the Flight API for data based on the parameters
    * @param params - the parameters to be added to the query
    * @return the raw response body from the api
     */
    public String search(HashMap<String, String> params);

}
