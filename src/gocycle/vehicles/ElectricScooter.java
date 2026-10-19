package gocycle.vehicles;

public class ElectricScooter extends ElectricVehicle {

    public ElectricScooter(String id, int batteryLevel) {
        super(id, batteryLevel);
    }

    @Override
    public boolean isRentable() {
        // Rentable if:
        // 1. They are AVAILABLE
        // 2. Battery is above MIN_BATTERY
        // 3. They are not OUT_OF_SERVICE

        return status == VehicleStatus.AVAILABLE && batteryLevel >= MIN_BATTERY;
    }
}