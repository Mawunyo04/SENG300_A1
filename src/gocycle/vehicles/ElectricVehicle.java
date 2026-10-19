package gocycle.vehicles;

public abstract class ElectricVehicle extends Vehicle {

    protected int batteryLevel;
    public static final int MIN_BATTERY = 20;

    public ElectricVehicle(String id, int batteryLevel) {
        super(id);
        this.batteryLevel = batteryLevel;
    }

    @Override
    public abstract boolean isRentable();

    public void updateBatteryLevel(int level) {
        // Update battery level, but ensure it stays within valid bounds
        if (level < 0) {
            this.batteryLevel = 0;
        } else if (level > 100) {
            this.batteryLevel = 100;
        } else {
            this.batteryLevel = level;
        }
    }

    public void reportFault(FaultType type) {
        // For now, just mark the vehicle OUT_OF_SERVICE
        // (expand this later if you implement FaultService)
        this.status = VehicleStatus.OUT_OF_SERVICE;
    }
}
