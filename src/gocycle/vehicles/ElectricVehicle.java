package gocycle.vehicles;

public abstract class ElectricVehicle extends Vehicle {

    protected int batteryLevel;
    public static final int MIN_BATTERY = 20;

    /**
     * electric vehicle constructor
     * @param id electric vehicle identification
     * @param batteryLevel vehicle battery level
     */
    public ElectricVehicle(String id, int batteryLevel) {
        super(id);
        this.batteryLevel = batteryLevel;
    }


    @Override
    public abstract boolean isRentable();

    /**
     * update battery level of vehicle
     * @param level battery level
     */
    public void updateBatteryLevel(int level) {
        // Update battery level within valid bounds
        if (level < 0) {
            this.batteryLevel = 0;
        } else if (level > 100) {
            this.batteryLevel = 100;
        } else {
            this.batteryLevel = level;
        }
    }

    /**
     * electric vehicle reports if there is a fault and it becomes unrentable
     * @param type fault type
     */
    public void reportFault(FaultType type) {
        this.status = VehicleStatus.OUT_OF_SERVICE;
    }
}
