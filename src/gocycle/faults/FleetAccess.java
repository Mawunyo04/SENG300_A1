package gocycle.faults;

import gocycle.vehicles.Vehicle;
import java.util.Map;

public interface FleetAccess {
    
    void takeOutOfService(Vehicle vehicle);

    void restoreService(Vehicle vehicle);

    void relocateVehicle(Vehicle vehicle, String destinationStationId);

    Map<String, String> getVehicleStatuses();

    Map<String, String> getStationStatuses();
}
