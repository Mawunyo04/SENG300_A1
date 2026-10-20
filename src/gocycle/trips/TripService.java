package gocycle.trips;

import gocycle.payments.AppReceiptSender;
import gocycle.payments.PaymentGateway;
import gocycle.payments.PaymentMethodType;
import gocycle.payments.ReceiptSender;
import gocycle.payments.SimulatedPaymentGateway;
import gocycle.pricing.CasualPricing;
import gocycle.pricing.PricingStrategy;
import gocycle.stations.DockingPoint;
import gocycle.stations.Station;
import gocycle.stations.StationNetwork;
import gocycle.vehicles.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TripService {

    private BigDecimal depositAmount;

    public TripService(BigDecimal depositAmount) {
        if (depositAmount == null || depositAmount.signum() <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        this.depositAmount = depositAmount;
    }

    /**
     * Starts a new trip for a rider at the specified docking point.
     * Places a deposit hold and unlocks the vehicle.
     * If startup fails (e.g., docking point is empty, vehicle is not rentable), the hold is released.
     *
     * @param rider  the rider starting the trip
     * @param point  the docking point containing the vehicle
     * @param method the payment method used for the deposit and subsequent fare
     * @return the created Trip if successful, or null if startup failed
     */
    public Trip startTrip(Rider rider, DockingPoint point, PaymentMethodType method) {
        if (rider == null || point == null || method == null) {
            throw new IllegalArgumentException("Rider, docking point, and payment method must not be null.");
        }

        // Rule 3: Place the deposit hold when the trip starts
        PaymentGateway paymentGateway = new SimulatedPaymentGateway();
        String holdId;
        try {
            holdId = paymentGateway.placeHold(rider, depositAmount, method);
        } catch (Exception e) {
            return null;
        }

        if (holdId == null) {
            return null;
        }

        // If startup fails, release the deposit hold (Rule 3)
        try {
            if (point.isFree() || point.getVehicle() == null) {
                paymentGateway.releaseHold(holdId);
                return null;
            }

            Vehicle vehicle = point.getVehicle();

            // Rule 1: ElectricVehicle rentable only if status is AVAILABLE and battery is at least 20%
            // Rule 8: Vehicle: at most one active Trip at a time
            if (!vehicle.isRentable()) {
                paymentGateway.releaseHold(holdId);
                return null;
            }

            Vehicle releasedVehicle = point.releaseVehicle();
            if (releasedVehicle == null) {
                paymentGateway.releaseHold(holdId);
                return null;
            }

            releasedVehicle.startRental();

            String tripId = "TRIP-" + UUID.randomUUID().toString();
            return new Trip(tripId, rider, releasedVehicle, holdId);
        } catch (Exception e) {
            paymentGateway.releaseHold(holdId);
            return null;
        }
    }

    /**
     * Returns a vehicle at a docking point, ends the trip, releases deposit hold, calculates fare,
     * charges the rider, and sends receipt upon payment success.
     *
     * @param trip  the active trip being completed
     * @param point the docking point where vehicle is being returned
     * @return true if vehicle was successfully returned and locked, false otherwise
     */
    public boolean returnVehicle(Trip trip, DockingPoint point) {
        if (trip == null || point == null) {
            return false;
        }

        if (!trip.isActive() || trip.getVehicle() == null) {
            return false;
        }

        Vehicle vehicle = trip.getVehicle();
        StationNetwork stationNetwork = new StationNetwork();
        Station station = findStationForPoint(point, stationNetwork);

        // Rule 2: If the station has no free point, redirect via StationNetwork and the trip keeps running.
        if (station != null && station.isFull()) {
            Station nearest = stationNetwork.findNearestWithFreePoint(station);
            if (station.getScreen() != null) {
                station.getScreen().showRedirect(nearest);
            }
            return false;
        }

        if (!point.isFree()) {
            return false;
        }

        // Rule 2: The trip ends and charging stops only after the DockingPoint confirms the vehicle is locked.
        boolean docked = point.dock(vehicle);
        if (!docked || !point.isLocked()) {
            return false;
        }

        vehicle.finishRental();
        trip.end(LocalDateTime.now());

        // Rule 3: Release deposit hold when the trip ends
        PaymentGateway paymentGateway = new SimulatedPaymentGateway();
        if (trip.getHoldId() != null) {
            paymentGateway.releaseHold(trip.getHoldId());
        }

        // Compute trip fare
        long minutes = trip.getDurationMinutes();
        Rider rider = trip.getRider();
        PricingStrategy pricing = (rider != null && rider.getPricing() != null)
                ? rider.getPricing()
                : new CasualPricing();
        BigDecimal fare = pricing.calculateFare(minutes);
        trip.setFare(fare);

        // Rule 4: Send receipt only after payment succeeds. Payment failure does not restart time charging.
        PaymentMethodType method = PaymentMethodType.CREDIT_CARD;
        if (station != null && station.getZone() != null) {
            for (PaymentMethodType pmt : PaymentMethodType.values()) {
                if (station.getZone().allows(pmt)) {
                    method = pmt;
                    break;
                }
            }
        }

        boolean paymentSuccess = false;
        try {
            paymentSuccess = paymentGateway.pay(rider, fare, method);
        } catch (Exception e) {
            paymentSuccess = false;
        }

        if (paymentSuccess) {
            trip.markPaid();
            ReceiptSender receiptSender = new AppReceiptSender();
            receiptSender.send(trip);
        }

        return true;
    }

    private Station findStationForPoint(DockingPoint point, StationNetwork stationNetwork) {
        if (stationNetwork == null || point == null) {
            return null;
        }
        for (Station s : stationNetwork.getStations()) {
            if (s.getPoints() != null && s.getPoints().contains(point)) {
                return s;
            }
        }
        return null;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

}
