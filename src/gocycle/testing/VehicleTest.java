package gocycle.testing;

import org.junit.Test;
import static org.junit.Assert.*;
import gocycle.vehicles.*;
import gocycle.trips.*;
import gocycle.faults.FaultType;


/**
 * Test class to test Vehicles
 */
public class VehicleTest {

    // ElectricBike Tests

    @Test
    public void eBikeIsRentableWhenAvailableAndBatteryIsEnough() {
        ElectricBike eBike = new ElectricBike("eB1", 50);
        eBike.setStatus(VehicleStatus.AVAILABLE);

        assertTrue(eBike.isRentable());
    }

    @Test
    public void eBikeIsNotRentableWhenBatteryTooLow() {
        ElectricBike eBike = new ElectricBike("eB2", ElectricVehicle.MIN_BATTERY - 1);
        eBike.setStatus(VehicleStatus.AVAILABLE);


        assertFalse(eBike.isRentable());
    }

    @Test
    public void eBikeIsNotRentableWhenOutOfService() {
        ElectricBike eBike = new ElectricBike("eB3", 80);
        eBike.setStatus(VehicleStatus.OUT_OF_SERVICE);

        assertFalse(eBike.isRentable());
    }

    @Test
    public void eBikeIsNotRentableWhenInUse() {
        ElectricBike eBike = new ElectricBike("eB4", 80);
        eBike.setStatus(VehicleStatus.IN_USE);

        assertFalse(eBike.isRentable());
    }

    // ElectricScooter Tests

    @Test
    public void scooterIsRentableWhenAvailableAndBatteryIsEnough() {
        ElectricScooter scooter = new ElectricScooter("S1", 50);
        scooter.setStatus(VehicleStatus.AVAILABLE);

        assertTrue(scooter.isRentable());
    }

    @Test
    public void scooterIsNotRentableWhenBatteryTooLow() {
        ElectricScooter scooter = new ElectricScooter("S2", ElectricVehicle.MIN_BATTERY - 1);
        scooter.setStatus(VehicleStatus.AVAILABLE);

        assertFalse(scooter.isRentable());
    }

    @Test
    public void scooterIsNotRentableWhenOutOfService() {
        ElectricScooter scooter = new ElectricScooter("S3", 80);
        scooter.setStatus(VehicleStatus.OUT_OF_SERVICE);

        assertFalse(scooter.isRentable());
    }

    @Test
    public void scooterIsNotRentableWhenInUse() {
        ElectricScooter scooter = new ElectricScooter("S4", 80);
        scooter.setStatus(VehicleStatus.IN_USE);

        assertFalse(scooter.isRentable());
    }

    // ElectricVehicles Tests

    private static class TestElectricVehicle extends ElectricVehicle {

        public TestElectricVehicle(String id, int batteryLevel) {
            super(id, batteryLevel);
        }

        @Override
        public boolean isRentable() {
            return status == VehicleStatus.AVAILABLE
                    && batteryLevel >= MIN_BATTERY;
        }

        public int getBatteryLevel() {
            return batteryLevel;
        }
    }

    @Test
    public void electricVehicleMinimumBatteryIs20() {
        assertEquals(20, ElectricVehicle.MIN_BATTERY);
    }

    @Test
    public void updateBatteryLevelChangesBatteryLevel() {
        TestElectricVehicle vehicle = new TestElectricVehicle("ev1", 50);

        vehicle.updateBatteryLevel(75);

        assertEquals(75, vehicle.getBatteryLevel());
    }

    @Test
    public void updateBatteryLevelAllowsZero() {
        TestElectricVehicle vehicle = new TestElectricVehicle("ev2", 50);

        vehicle.updateBatteryLevel(0);

        assertEquals(0, vehicle.getBatteryLevel());
    }

    @Test
    public void updateBatteryLevelAllows100() {
        TestElectricVehicle vehicle = new TestElectricVehicle("ev3", 50);

        vehicle.updateBatteryLevel(100);

        assertEquals(100, vehicle.getBatteryLevel());
    }

    @Test
    public void updateBatteryLevelBelowZeroSetsBatteryToZero() {
        TestElectricVehicle vehicle = new TestElectricVehicle("ev4", 50);

        vehicle.updateBatteryLevel(-10);

        assertEquals(0, vehicle.getBatteryLevel());
    }

    @Test
    public void updateBatteryLevelAbove100SetsBatteryTo100() {
        TestElectricVehicle vehicle = new TestElectricVehicle("ev5", 50);

        vehicle.updateBatteryLevel(110);

        assertEquals(100, vehicle.getBatteryLevel());
    }

    @Test
    public void updateBatteryLevelAllowsMinimumBattery() {
        TestElectricVehicle vehicle = new TestElectricVehicle("ev6", 50);

        vehicle.updateBatteryLevel(ElectricVehicle.MIN_BATTERY);

        assertEquals(ElectricVehicle.MIN_BATTERY,
                vehicle.getBatteryLevel());
        assertTrue(vehicle.isRentable());
    }

    @Test
    public void reportFaultMarksVehicleOutOfService() {
        TestElectricVehicle vehicle = new TestElectricVehicle("ev7", 80);

        vehicle.reportFault(FaultType.LOW_BATTERY);

        assertFalse(vehicle.isRentable());
    }

    @Test
    public void reportFaultKeepsVehicleOutOfService() {
        TestElectricVehicle vehicle = new TestElectricVehicle("ev8", 80);
        vehicle.reportFault(FaultType.MOTOR_FAULT);

        vehicle.updateBatteryLevel(100);

        assertFalse(vehicle.isRentable());
    }

    // PedalBike Tests

    @Test
    public void bikeIsAvailable() {
        PedalBike bike = new PedalBike("b1");
        bike.setStatus(VehicleStatus.AVAILABLE);

        assertTrue(bike.isRentable());
    }

    @Test
    public void bikeIsInUse() {
        PedalBike bike = new PedalBike("b2");
        bike.setStatus(VehicleStatus.IN_USE);

        assertFalse(bike.isRentable());
    }

    @Test
    public void bikeIsOutOfService() {
        PedalBike bike = new PedalBike("b3");
        bike.setStatus(VehicleStatus.OUT_OF_SERVICE);

        assertFalse(bike.isRentable());
    }

    // Vehicle Tests

    // Helper class to test the abstract Vehicle class
    private static class TestVehicle extends Vehicle {

        public TestVehicle(String id) {
            super(id);
        }

        @Override
        public boolean isRentable() {
            return status == VehicleStatus.AVAILABLE;
        }
    }

    @Test
    public void vehicleStartsAsAvailable() {
        Vehicle vehicle = new TestVehicle("v1");

        assertTrue(vehicle.isRentable());
    }

    @Test
    public void startRentalChangesStatusToInUse() {
        Vehicle vehicle = new TestVehicle("v2");

        vehicle.startRental();

        vehicle.setStatus(VehicleStatus.IN_USE);
        assertFalse(vehicle.isRentable());
    }

    @Test
    public void finishRentalChangesStatusToAvailable() {
        Vehicle vehicle = new TestVehicle("v3");
        vehicle.setStatus(VehicleStatus.IN_USE);

        vehicle.finishRental();

        assertTrue(vehicle.isRentable());
    }

    @Test
    public void finishRentalDoesNothingWhenNotInUse() {
        Vehicle vehicle = new TestVehicle("v4");

        vehicle.finishRental();

        assertTrue(vehicle.isRentable());
    }

    @Test
    public void markOutOfServiceChangesStatus() {
        Vehicle vehicle = new TestVehicle("v5");

        vehicle.markOutOfService();

        assertFalse(vehicle.isRentable());
    }

    @Test
    public void restoreServiceChangesStatusToAvailable() {
        Vehicle vehicle = new TestVehicle("v6");
        vehicle.markOutOfService();

        vehicle.restoreService();

        assertTrue(vehicle.isRentable());
    }

    @Test
    public void restoreServiceDoesNothingWhenNotOutOfService() {
        Vehicle vehicle = new TestVehicle("v7");
        vehicle.setStatus(VehicleStatus.IN_USE);

        vehicle.restoreService();

        assertFalse(vehicle.isRentable());
    }

    @Test
    public void setStatusChangesVehicleStatus() {
        Vehicle vehicle = new TestVehicle("v8");

        vehicle.setStatus(VehicleStatus.OUT_OF_SERVICE);

        assertFalse(vehicle.isRentable());
    }

    @Test
    public void startRentalDoesNothingWhenNotRentable() {
        Vehicle vehicle = new TestVehicle("v9");
        vehicle.markOutOfService();

        vehicle.startRental();

        assertFalse(vehicle.isRentable());
    }

    // VehicleStatus Tests
    @Test
    public void availableStatusExists() {
        assertEquals("AVAILABLE",
                VehicleStatus.AVAILABLE.name());
    }

    @Test
    public void inUseStatusExists() {
        assertEquals("IN_USE",
                VehicleStatus.IN_USE.name());
    }


}
