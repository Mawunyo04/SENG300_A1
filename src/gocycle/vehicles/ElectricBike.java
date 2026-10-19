package vehicles;

public class ElectricBike extends ElectricVehicle {

    public ElectricBike(String id, int batteryLevel) {
        super(id, batteryLevel);
    }

    @Override
    public boolean isRentable() {
        // Electric bikes are rentable only if:
        // 1. They are AVAILABLE
        // 2. Battery is above MIN_BATTERY
        // 3. They are not OUT_OF_SERVICE or IN_USE

        return status == VehicleStatus.AVAILABLE
                && batteryLevel >= MIN_BATTERY;
    }
}