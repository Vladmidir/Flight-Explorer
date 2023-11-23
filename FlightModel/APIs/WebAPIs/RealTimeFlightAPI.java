package FlightModel.APIs.WebAPIs;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;

/**
* This class is responsible for fetching data from the "flights" endpoint of the AviationStack API.
* Does not allow "flight_date" parameter, as that would be a historic flight.
 */
public class RealTimeFlightAPI implements FlightAPIEndPoint{

        private String url = "http://api.aviationstack.com/v1/flights" +
                "?access_key=";
        private final List<String> allowedParams;

        private final HttpClient client;

        /**
        * Constructor
        * @param APIkey - the api key for the AviationStack api
        */
        public RealTimeFlightAPI(String APIkey) {
            //add the api key to the url
            this.url += APIkey;
            //build the client
            this.client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
            //set the allowed params
            this.allowedParams = List.of(
                    "airline_name",
                    "flight_status",
                    "flight_number",
                    "flight_iata",
                    "flight_icao",
                    "dep_iata",
                    "arr_iata",
                    "limit",
                    "offset");
        }

        @Override
        public String search(HashMap<String, String> params) {
            HashMap<String, String> validParams = new HashMap<>();
            //check if params are valid
            for (String key : params.keySet()) {
                if (allowedParams.contains(key)) {
                    validParams.put(key, params.get(key));
                }else
                    System.out.println("Invalid parameter: " + key);
            }
            return sendRequest(validParams);
        }


        /**
        * Sends a request to the api
        * @param params - the parameters to be added to the url
        * @return the response body from the api
         */
        private String sendRequest(HashMap<String, String> params) {
            //add params to url
            String url = this.url;
            for (String key : params.keySet()) {
                url += "&" + key + "=" + params.get(key);
            }

            //build a request
            HttpRequest request = HttpRequest.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .uri(URI.create(url))
                    .build();

            //send the request synchronously
            try {
                //parse the response
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                return response.body();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
}
