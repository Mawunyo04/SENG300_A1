package gocycle.faults;

import gocycle.vehicles.Vehicle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OperatorConsole implements FaultObserver {
    private final FaultService faults;
    private final FleetAccess fleet;
    private final List<FaultReport> alerts = new ArrayList<>();

    public OperatorConsole(FaultService faults) {
        this.faults = Objects.requireNonNull(faults, "faults");
        this.fleet = faults.getFleetAccess();
    }

    @Override
    public synchronized void onFault(FaultReport report) {
        alerts.add(Objects.requireNonNull(report, "report"));
    }

    public synchronized List<FaultReport> getAlerts() {
        return Collections.unmodifiableList(new ArrayList<>(alerts));
    }

    public Map<String, String> getVehicleStatuses() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(fleet.getVehicleStatuses()));
    }

    public Map<String, String> getStationStatuses() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(fleet.getStationStatuses()));
    }

    public void takeOutOfService(Vehicle vehicle) {
        faults.takeOutOfService(vehicle);
    }

    public void resolveFault(FaultReport report) {
        faults.resolveFault(report);
    }

    public void restoreService(Vehicle vehicle) {
        faults.restoreService(vehicle);
    }

    public void relocateVehicle(Vehicle vehicle, String destinationStationId) {
        Objects.requireNonNull(vehicle, "vehicle");
        if (destinationStationId == null || destinationStationId.trim().isEmpty()) {
            throw new IllegalArgumentException("A destination station ID is required.");
        }
        fleet.relocateVehicle(vehicle, destinationStationId.trim());
    }
}
