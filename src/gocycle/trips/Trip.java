package gocycle.trips;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Duration;

public class Trip {

    String id;
    LocalDateTime startTime;
    LocalDateTime endTime;
    String holdId;
    BigDecimal fare;
    boolean paid;
    Rider rider;

    public Trip(String id, String holdId, BigDecimal fare) {
        this.id = id;
        this.startTime = LocalDateTime.now();
        this.holdId = holdId;
        this.fare = fare;
        this.paid = false;
    }

    public Trip(String id, Rider rider, String holdId, BigDecimal fare) {
        this.id = id;
        this.startTime = LocalDateTime.now();
        this.holdId = holdId;
        this.fare = fare;
        this.paid = false;
        this.rider = rider;
    }


    boolean isActive() {
        return (endTime == null);
    }

    void end(LocalDateTime at) {
        endTime = at;
    }

    long getDurationMinutes() {
        Duration duration = Duration.between(startTime, endTime);
        return duration.toMinutes();
    }

    void markPaid() {
        paid = true;
    }

    void getId() {
        return id;
    }

    void getFare() {
        return fare;
    }

    void getRider() {
        return rider;
    }

}
