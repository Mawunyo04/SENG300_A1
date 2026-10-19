package gocycle.faults;

import gocycle.vehicles.Vehicle;
import java.time.LocalDateTime;

public class FaultReport {
    // Fields: the blanks on the form
    private final Vehicle vehicle;
    private final FaultType type;
    private final FaultSource source;
    private final String description;
    private final LocalDateTime reportedAt;

    // Constructor: filling the form in
    public FaultReport(Vehicle vehicle, FaultType type, FaultSource source, String description){
     this.vehicle = vehicle;
     this.type = type;
     this.source = source;
     this.description = description;
     this.reportedAt = LocalDateTime.now();
    }

    // Getters: reading the form
    public Vehicle getVehicle() { return vehicle; }
    public FaultType getType() { return type; }
    public FaultSource getSource() { return source; }
    public String getDescription() { return description; }
    public LocalDateTime getReportedAt() { return reportedAt; }

    // How the report looks when printed
    @Override
    public String toString(){
        return type + " reported by " + source + ": " + description;
    }
}
