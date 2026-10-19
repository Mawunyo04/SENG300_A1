package gocycle.vehicles;

public class ElectricBike extends ElectricVehicle {

    /**
     * electric bike constructor
     * @param id electric bike identification
     * @param batteryLevel electric bike battery level
     */
    public ElectricBike(String id, int batteryLevel) {
        super(id, batteryLevel);
    }

    /**
     * determine if electric bike is rentable
     * @return status of the electric bike
     */
    @Override
    public boolean isRentable() {
        // Electric bikes are rentable only if:
        // 1. They are AVAILABLE
        // 2. Battery is above MIN_BATTERY
        // 3. They are not OUT_OF_SERVICE or IN_USE

        return status == VehicleStatus.AVAILABLE && batteryLevel >= MIN_BATTERY;
    }
}