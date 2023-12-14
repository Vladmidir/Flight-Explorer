package FlightModel.Injectors;

import FlightModel.FlightExplorer;

/**
 * This interface is used to inject a flight API into the FlightExplorer class.
 */
public interface FlightAPIInjector {

    /**
     * This method is used to inject a flight API into the FlightExplorer class.
     * @return - the FlightExplorer object
     * @throws Exception - if the FlightExplorer object cannot be created
     */
    public FlightExplorer buildFlightExplorer() throws Exception;
}
