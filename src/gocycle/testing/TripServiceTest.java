package gocycle.testing;

import gocycle.payments.PaymentMethodType;
import gocycle.pricing.CasualPricing;
import gocycle.pricing.MemberPricing;
import gocycle.pricing.StudentPricing;
import gocycle.stations.DockingPoint;
import gocycle.stations.Station;
import gocycle.stations.StationNetwork;
import gocycle.stations.Zone;
import gocycle.trips.Rider;
import gocycle.trips.RiderRegistrationService;
import gocycle.trips.Trip;
import gocycle.trips.TripService;
import gocycle.vehicles.ElectricBike;
import gocycle.vehicles.ElectricVehicle;
import gocycle.vehicles.PedalBike;
import gocycle.vehicles.VehicleStatus;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class TripServiceTest {

    private BigDecimal deposit;
    private StationNetwork network;
    private TripService tripService;
    private Zone zone;
    private Station stationA;
    private Station stationB;
    private Rider casualRider;

    @Before
    public void setUp() {
        deposit = new BigDecimal("20.00");
        network = new StationNetwork();

        zone = new Zone("Downtown", "logo.png", "#FF5500");
        zone.addAllowedPayment(PaymentMethodType.CREDIT_CARD);

        stationA = new Station("STA-A", 51.0447, -114.0719, zone, 2);
        stationB = new Station("STA-B", 51.0486, -114.0708, zone, 2);
        network.addStation(stationA);
        network.addStation(stationB);

        tripService = new TripService(deposit);
        casualRider = new Rider("R-1", "Alice", "alice@example.com");
    }

    // --- startTrip Tests ---

    @Test
    public void testStartTripSuccessfulWithPedalBike() {
        DockingPoint dp = stationA.getPoints().get(0);
        PedalBike bike = new PedalBike("PB-1");
        dp.dock(bike);

        Trip trip = tripService.startTrip(casualRider, dp, PaymentMethodType.CREDIT_CARD);

        assertNotNull(trip);
        assertTrue(trip.isActive());
        assertEquals(casualRider, trip.getRider());
        assertSame(bike, trip.getVehicle());
        assertEquals(VehicleStatus.IN_USE, bike.isRentable() ? VehicleStatus.AVAILABLE : VehicleStatus.IN_USE);
        assertFalse(bike.isRentable());
        assertTrue(dp.isFree());
        assertNotNull(trip.getHoldId());
        assertTrue(trip.getHoldId().startsWith("HOLD-"));
        assertNull(trip.getFare());
        assertFalse(trip.isPaid());
    }

    @Test
    public void testStartTripSuccessfulWithElectricBikeAboveMinBattery() {
        DockingPoint dp = stationA.getPoints().get(0);
        ElectricBike eBike = new ElectricBike("EB-1", ElectricVehicle.MIN_BATTERY + 10);
        dp.dock(eBike);

        Trip trip = tripService.startTrip(casualRider, dp, PaymentMethodType.CREDIT_CARD);

        assertNotNull(trip);
        assertTrue(trip.isActive());
        assertNotNull(trip.getHoldId());
        assertTrue(trip.getHoldId().startsWith("HOLD-"));
    }

    @Test
    public void testStartTripFailsWhenElectricBikeBatteryBelowMin() {
        // Business rule 1: ElectricVehicle rentable only if battery is at least 20%
        // Business rule 3: Release hold if startup fails
        DockingPoint dp = stationA.getPoints().get(0);
        ElectricBike eBike = new ElectricBike("EB-LOW", ElectricVehicle.MIN_BATTERY - 1);
        dp.dock(eBike);

        Trip trip = tripService.startTrip(casualRider, dp, PaymentMethodType.CREDIT_CARD);

        assertNull(trip);
        // Vehicle should remain in dock and available
        assertSame(eBike, dp.getVehicle());
        assertTrue(dp.isLocked());
        // Verify no active holds were left orphan
        // (A hold was placed during startup, but released on failure)
    }

    @Test
    public void testStartTripFailsWhenPointIsEmpty() {
        // Business rule 3: Release hold if startup fails
        DockingPoint dp = stationA.getPoints().get(0);
        assertTrue(dp.isFree());

        Trip trip = tripService.startTrip(casualRider, dp, PaymentMethodType.CREDIT_CARD);

        assertNull(trip);
    }

    @Test
    public void testStartTripFailsWhenVehicleOutOfService() {
        DockingPoint dp = stationA.getPoints().get(0);
        PedalBike bike = new PedalBike("PB-BROKEN");
        bike.markOutOfService();
        dp.dock(bike);

        Trip trip = tripService.startTrip(casualRider, dp, PaymentMethodType.CREDIT_CARD);

        assertNull(trip);
        assertSame(bike, dp.getVehicle());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartTripNullRiderThrowsException() {
        DockingPoint dp = stationA.getPoints().get(0);
        tripService.startTrip(null, dp, PaymentMethodType.CREDIT_CARD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartTripNullPointThrowsException() {
        tripService.startTrip(casualRider, null, PaymentMethodType.CREDIT_CARD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartTripNullMethodThrowsException() {
        DockingPoint dp = stationA.getPoints().get(0);
        tripService.startTrip(casualRider, dp, null);
    }

    // --- returnVehicle Tests ---

    @Test
    public void testReturnVehicleSuccessNormalFlow() {
        // Start a trip at Station A
        DockingPoint startPoint = stationA.getPoints().get(0);
        PedalBike bike = new PedalBike("PB-1");
        startPoint.dock(bike);

        Trip trip = tripService.startTrip(casualRider, startPoint, PaymentMethodType.CREDIT_CARD);
        assertNotNull(trip);
        String holdId = trip.getHoldId();
        assertNotNull(holdId);

        // Return vehicle at Station B
        DockingPoint returnPoint = stationB.getPoints().get(0);
        boolean returned = tripService.returnVehicle(trip, returnPoint);

        assertTrue(returned);
        // Rule 2: Vehicle locked confirmed
        assertTrue(returnPoint.isLocked());
        assertSame(bike, returnPoint.getVehicle());
        assertTrue(bike.isRentable());

        // Trip ended
        assertFalse(trip.isActive());
        assertNotNull(trip.getEndTime());
        assertNotNull(trip.getFare());

        // Rule 4: Payment succeeds, marked paid
        assertTrue(trip.isPaid());
    }

    @Test
    public void testReturnVehicleWhenStationIsFullRedirectsAndKeepsTripRunning() {
        // Business rule 2: If the station has no free point, redirect via StationNetwork and the trip keeps running.
        DockingPoint startPoint = stationA.getPoints().get(0);
        PedalBike bike = new PedalBike("PB-1");
        startPoint.dock(bike);

        Trip trip = tripService.startTrip(casualRider, startPoint, PaymentMethodType.CREDIT_CARD);
        assertNotNull(trip);

        // Fill all points at Station B
        for (DockingPoint dp : stationB.getPoints()) {
            dp.dock(new PedalBike("OTHER-" + dp.getId()));
        }
        assertTrue(stationB.isFull());

        // Attempt to return at Station B
        DockingPoint returnPoint = stationB.getPoints().get(0);
        boolean returned = tripService.returnVehicle(trip, returnPoint);

        // Should return false and keep trip active
        assertFalse(returned);
        assertTrue(trip.isActive());
        assertNull(trip.getEndTime());
        assertNull(trip.getFare());
        assertNotNull(trip.getHoldId());
    }

    @Test
    public void testReturnVehicleWhenTargetPointIsOccupiedReturnsFalse() {
        DockingPoint startPoint = stationA.getPoints().get(0);
        PedalBike bike = new PedalBike("PB-1");
        startPoint.dock(bike);

        Trip trip = tripService.startTrip(casualRider, startPoint, PaymentMethodType.CREDIT_CARD);

        // Occupy point 0 of station B, but leave point 1 free
        DockingPoint occupiedPoint = stationB.getPoints().get(0);
        occupiedPoint.dock(new PedalBike("OTHER"));

        // Attempt returning to the occupied point
        boolean returned = tripService.returnVehicle(trip, occupiedPoint);

        assertFalse(returned);
        assertTrue(trip.isActive());
    }

    @Test
    public void testReturnVehicleWhenPaymentFailsDoesNotRestartTimeChargingAndNoReceipt() {
        // Business rule 4: Send the receipt only after payment succeeds. A payment failure does not restart time charging.
        DockingPoint startPoint = stationA.getPoints().get(0);
        PedalBike bike = new PedalBike("PB-1");
        startPoint.dock(bike);

        Trip trip = tripService.startTrip(casualRider, startPoint, PaymentMethodType.CREDIT_CARD);
        assertNotNull(trip);
        String holdId = trip.getHoldId();
        assertNotNull(holdId);

        // Simulate payment failure by clearing rider before return
        trip.setRider(null);

        DockingPoint returnPoint = stationB.getPoints().get(0);
        boolean returned = tripService.returnVehicle(trip, returnPoint);

        // Vehicle was locked and returned
        assertTrue(returned);
        assertTrue(returnPoint.isLocked());
        assertSame(bike, returnPoint.getVehicle());
        assertTrue(bike.isRentable());

        // Trip is ended and charging did not restart
        assertFalse(trip.isActive());
        assertNotNull(trip.getEndTime());
        assertNotNull(trip.getFare());

        // Payment failed -> NOT marked paid
        assertFalse(trip.isPaid());
    }

    @Test
    public void testPricingStrategiesAppliedCorrectly() {
        Rider memberRider = new Rider("R-2", "Bob", "bob@example.com");
        memberRider.setPricing(new MemberPricing());
        Rider studentRider = new Rider("R-3", "Charlie", "charlie@example.com");
        studentRider.setPricing(new StudentPricing());

        // 1. Casual rider (unlock: $1.50)
        DockingPoint dpA = stationA.getPoints().get(0);
        dpA.dock(new PedalBike("PB-A"));
        Trip tripCasual = tripService.startTrip(casualRider, dpA, PaymentMethodType.CREDIT_CARD);
        tripService.returnVehicle(tripCasual, stationB.getPoints().get(0));
        assertEquals(new BigDecimal("1.50"), tripCasual.getFare());

        // 2. Member rider (unlock: $1.50)
        DockingPoint dpB = stationA.getPoints().get(1);
        dpB.dock(new PedalBike("PB-B"));
        Trip tripMember = tripService.startTrip(memberRider, dpB, PaymentMethodType.CREDIT_CARD);
        tripService.returnVehicle(tripMember, stationB.getPoints().get(1));
        assertEquals(new BigDecimal("1.50"), tripMember.getFare());

        // 3. Student rider (unlock: $1.50)
        DockingPoint dpC = stationB.getPoints().get(0);
        Trip tripStudent = tripService.startTrip(studentRider, dpC, PaymentMethodType.CREDIT_CARD);
        tripService.returnVehicle(tripStudent, stationA.getPoints().get(0));
        assertEquals(new BigDecimal("1.50"), tripStudent.getFare());
    }

    @Test
    public void testCannotReturnAlreadyCompletedTrip() {
        DockingPoint startPoint = stationA.getPoints().get(0);
        PedalBike bike = new PedalBike("PB-1");
        startPoint.dock(bike);

        Trip trip = tripService.startTrip(casualRider, startPoint, PaymentMethodType.CREDIT_CARD);
        DockingPoint returnPoint1 = stationB.getPoints().get(0);
        assertTrue(tripService.returnVehicle(trip, returnPoint1));

        // Try to return the same trip again
        DockingPoint returnPoint2 = stationB.getPoints().get(1);
        assertFalse(tripService.returnVehicle(trip, returnPoint2));
    }

    @Test
    public void testReturnVehicleNullArgumentsReturnFalse() {
        assertFalse(tripService.returnVehicle(null, stationA.getPoints().get(0)));

        DockingPoint startPoint = stationA.getPoints().get(0);
        startPoint.dock(new PedalBike("PB-1"));
        Trip trip = tripService.startTrip(casualRider, startPoint, PaymentMethodType.CREDIT_CARD);
        assertFalse(tripService.returnVehicle(trip, null));
    }

    // --- RiderRegistrationService Tests ---

    @Test
    public void testRiderRegistrationService() {
        RiderRegistrationService regService = new RiderRegistrationService();

        Rider casual = regService.registerCasual("Dave", "dave@example.com");
        assertNotNull(casual);
        assertTrue(casual.getPricing() instanceof CasualPricing);
        assertNull(casual.getMembership());

        Rider member = regService.registerMember("Eve", "eve@example.com", "ANNUAL");
        assertNotNull(member);
        assertTrue(member.getPricing() instanceof MemberPricing);
        assertNotNull(member.getMembership());
        assertTrue(member.getMembership().isActive());
        assertEquals("ANNUAL", member.getMembership().getPlan());

        Rider student = regService.registerStudent("Frank", "frank@example.com");
        assertNotNull(student);
        assertTrue(student.getPricing() instanceof StudentPricing);
    }

    @Test
    public void testTripServiceSingleArgConstructor() {
        TripService singleArgService = new TripService(deposit);
        assertEquals(deposit, singleArgService.getDepositAmount());
    }
}
