package FlightModel.APIs.WebAPIs;

import java.util.HashMap;

public interface FlightAPIEndPoint {

    /**
    * Send a request to the AviationStack API
    * @param params - the parameters to be added to the url
    * @return the response body from the api
     */
    public String search(HashMap<String, String> params);

}
