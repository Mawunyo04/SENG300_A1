package gocycle.trips;

import gocycle.vehicles.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Duration;

public class Trip {

    private String id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String holdId;
    private BigDecimal fare;
    private boolean paid;
    private Rider rider;
    private Vehicle vehicle;

    public Trip(String id, Rider rider, String holdId, BigDecimal fare) {
        this.id = id;
        this.startTime = LocalDateTime.now();
        this.holdId = holdId;
        this.fare = fare;
        this.paid = false;
        this.rider = rider;
    }

    public Trip(String id, Rider rider, Vehicle vehicle, String holdId) {
        this.id = id;
        this.startTime = LocalDateTime.now();
        this.rider = rider;
        this.vehicle = vehicle;
        this.holdId = holdId;
        this.fare = null;
        this.paid = false;
    }

    public boolean isActive() {
        return (endTime == null);
    }

    public void end(LocalDateTime at) {
        endTime = (at != null) ? at : LocalDateTime.now();
    }

    public long getDurationMinutes() {
        LocalDateTime end = (endTime != null) ? endTime : LocalDateTime.now();
        Duration duration = Duration.between(startTime, end);
        return Math.max(0, duration.toMinutes());
    }

    public void markPaid() {
        paid = true;
    }

    public String getId() {
        return this.id;
    }

    public BigDecimal getFare() {
        return this.fare;
    }

    public void setFare(BigDecimal fare) {
        this.fare = fare;
    }

    public boolean isPaid() {
        return this.paid;
    }

    public Rider getRider() {
        return this.rider;
    }
    public void setFare(BigDecimal fare) {this.fare = fare;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public String getHoldId() {
        return this.holdId;
    }
}
