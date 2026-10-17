package gocycle.payments;
import gocycle.trips.Trips;

public interface ReceiptSender {
    /**
     * Sends the receipt for a paid trip
     * @param trip
     * @throws IllegalArgumentException if trip is null
     * @throws IllegalStateException if trip has no fare
     */
    void send(Trip trip);
}


