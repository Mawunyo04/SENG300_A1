package gocycle.testing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import gocycle.vehicles.*;

public class VehicleTest {

    // ElectricBike Tests

    // ElectricScooter Tests

    @Test
    void scooterIsRentableWhenAvailableAndBatteryIsEnough() {
        ElectricScooter scooter = new ElectricScooter("S1", 50);
        scooter.status = VehicleStatus.AVAILABLE;

        assertTrue(scooter.isRentable());
    }

    @Test
    void scooterIsNotRentableWhenBatteryTooLow() {
        ElectricScooter scooter = new ElectricScooter("S2", ElectricVehicle.MIN_BATTERY - 1);
        scooter.status = VehicleStatus.AVAILABLE;

        assertFalse(scooter.isRentable());
    }

    @Test
    void scooterIsNotRentableWhenOutOfService() {
        ElectricScooter scooter = new ElectricScooter("S3", 80);
        scooter.status = VehicleStatus.OUT_OF_SERVICE;

        assertFalse(scooter.isRentable());
    }

    @Test
    void scooterIsNotRentableWhenInUse() {
        ElectricScooter scooter = new ElectricScooter("S4", 80);
        scooter.status = VehicleStatus.IN_USE;

        assertFalse(scooter.isRentable());
    }

    // ElectricVehicles Tests

    // PedalBike Tests

    // Vehicle Tests

    // VehicleStatus Tests
}
