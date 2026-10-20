package gocycle.vehicles;

import gocycle.faults.FaultType;

public abstract class ElectricVehicle extends Vehicle {

    protected int batteryLevel;
    protected FaultType fault;
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
     * Reports a fault and marks the vehicle as unrentable.
     *
     * @param type the type of fault reported
     */
    public void reportFault(FaultType type) {
        this.fault = type;
        this.status = VehicleStatus.OUT_OF_SERVICE;
    }

    public FaultType getFault() {
        return fault;
    }

    public boolean hasFault() {
        return fault != null;
    }
}
