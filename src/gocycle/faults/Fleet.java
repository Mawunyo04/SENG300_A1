package gocycle.faults;

import gocycle.stations.DockingPoint;
import gocycle.stations.Station;
import gocycle.stations.StationNetwork;
import gocycle.vehicles.Vehicle;
import java.util.LinkedHashMap;
import java.util.Map;

public class Fleet implements FleetAccess {
    private final StationNetwork network;
    private final Map<String, Vehicle> vehicles;

    public Fleet(StationNetwork network, Map<String, Vehicle> vehicles) {
        this.network = network;
        this.vehicles = vehicles;
    }

    private Station findStation(String id) {
        for (Station station : network.getStations()) {
            if (station.getId().equalsIgnoreCase(id)) return station;
        }
        return null;
    }

    private String idOf(Vehicle vehicle) {
        for (Map.Entry<String, Vehicle> entry : vehicles.entrySet()) {
            if (entry.getValue() == vehicle) return entry.getKey();
        }
        return "?";
    }

    private DockingPoint pointOf(Station station, Vehicle vehicle) {
        for (DockingPoint point : station.getPoints()) {
            if (point.getVehicle() == vehicle) return point;
        }
        return null;
    }

    private Station stationOf(Vehicle vehicle) {
        for (Station station : network.getStations()) {
            if (pointOf(station, vehicle) != null) return station;
        }
        return null;
    }

    @Override
    public void takeOutOfService(Vehicle vehicle) {
        vehicle.markOutOfService();
    }

    @Override
    public void restoreService(Vehicle vehicle) {
        vehicle.restoreService();
    }

    @Override
    public void relocateVehicle(Vehicle vehicle, String destinationStationId) {
        Station from = stationOf(vehicle);
        Station to = findStation(destinationStationId);
        if (from == null) {
            throw new IllegalStateException(idOf(vehicle) + " is not docked at a station.");
        }
        if (to == null) {
            throw new IllegalArgumentException("No station with ID " + destinationStationId + ".");
        }
        if (to == from) {
            throw new IllegalArgumentException(idOf(vehicle) + " is already at station " + to.getId() + ".");
        }
        if (to.isFull()) {
            throw new IllegalStateException("Station " + to.getId() + " is full.");
        }
        pointOf(from, vehicle).releaseVehicle();
        to.findFreePoint().dock(vehicle);
    }

    @Override
    public Map<String, String> getVehicleStatuses() {
        Map<String, String> result = new LinkedHashMap<String, String>();
        for (Map.Entry<String, Vehicle> entry : vehicles.entrySet()) {
            Station station = stationOf(entry.getValue());
            String where = (station == null) ? "on a ride" : "docked at " + station.getId();
            String state = entry.getValue().isRentable() ? "available" : "not available";
            result.put(entry.getKey(), state + ", " + where);
        }
        return result;
    }

    @Override
    public Map<String, String> getStationStatuses() {
        Map<String, String> result = new LinkedHashMap<String, String>();
        for (Station station : network.getStations()) {
            int free = 0;
            for (DockingPoint point : station.getPoints()) {
                if (point.isFree()) free++;
            }
            result.put(station.getId(), station.getZone().getName() + ", " + free + " of " + station.getPoints().size() + " points free");
        }
        return result;
    }
}
