package gocycle.faults;

import cle.fleet.Vehicle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FaultService {
    private final FleetAccess fleet;
    private final List<FaultReport> reports = new ArrayList<>();
    private final Set<FaultReport> openReports = new LinkedHashSet<>();
    private final Set<FaultObserver> observers = new LinkedHashSet<>();
    private final List<String> notificationFailures = new ArrayList<>();

    public FaultService(FleetAccess fleet) {
        this.fleet = Objects.requireNonNull(fleet, "fleet");
    }

    public synchronized void addObserver(FaultObserver observer) {
        observers.add(Objects.requireNonNull(observer, "observer"));
    }

    public synchronized void removeObserver(FaultObserver observer) {
        observers.remove(Objects.requireNonNull(observer, "observer"));
    }

    public synchronized FaultReport reportFault(Vehicle vehicle, FaultType type,
                                                FaultSource source, String description) {
        Objects.requireNonNull(vehicle, "vehicle");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(source, "source");
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("A fault description is required.");
        }
        FaultReport report = new FaultReport(vehicle, type, source, description.trim());

        fleet.takeOutOfService(vehicle);
        reports.add(report);
        openReports.add(report);
        for (FaultObserver observer : new ArrayList<>(observers)) {
            try {
                observer.onFault(report);
            } catch (RuntimeException failure) {
                
                notificationFailures.add(observer.getClass().getName() + ": " + failure);
            }
        }
        return report;
    }

    public synchronized void resolveFault(FaultReport report) {
        Objects.requireNonNull(report, "report");
        if (!reports.contains(report)) {
            throw new IllegalArgumentException("This service did not record that report.");
        }
        if (!openReports.remove(report)) {
            throw new IllegalStateException("This fault has already been resolved.");
        }
    }

    public synchronized boolean hasOpenFaults(Vehicle vehicle) {
        return !getOpenFaults(vehicle).isEmpty();
    }

    public synchronized List<FaultReport> getOpenFaults(Vehicle vehicle) {
        Objects.requireNonNull(vehicle, "vehicle");
        List<FaultReport> result = new ArrayList<>();
        for (FaultReport report : openReports) {
            if (report.getVehicle().equals(vehicle)) {
                result.add(report);
            }
        }
        return Collections.unmodifiableList(result);
    }

    public synchronized List<FaultReport> getReports() {
        return Collections.unmodifiableList(new ArrayList<>(reports));
    }

    public synchronized List<String> getNotificationFailures() {
        return Collections.unmodifiableList(new ArrayList<>(notificationFailures));
    }

    public synchronized void takeOutOfService(Vehicle vehicle) {
        fleet.takeOutOfService(Objects.requireNonNull(vehicle, "vehicle"));
    }

    public synchronized void restoreService(Vehicle vehicle) {
        if (hasOpenFaults(vehicle)) {
            throw new IllegalStateException("Resolve all open faults before restoring service.");
        }
        fleet.restoreService(vehicle);
    }

    FleetAccess getFleetAccess() {
        return fleet;
    }
}
