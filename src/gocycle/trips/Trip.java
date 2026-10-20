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


    public boolean isActive() {
        return (endTime == null);
    }

    public void end(LocalDateTime at) {
        endTime = at;
    }

    public long getDurationMinutes() {
        Duration duration = Duration.between(startTime, endTime);
        return duration.toMinutes();
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

    public Rider getRider() {
        return this.rider;
    }

}
