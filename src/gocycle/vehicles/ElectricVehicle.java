package vehicles;

public abstract class ElectricVehicle extends Vehicle {

    protected int batteryLevel;
    public static final int MIN_BATTERY = 20;

    public ElectricVehicle(String id, int batteryLevel) {
        super(id);
        this.batteryLevel = batteryLevel;
    }

    @Override
    public abstract boolean isRentable();

    public updateBatteryLevel(int level) {
        // TODO: implement
    }

    public void reportFault(FaultType type) {
        // TODO: implement
    }
}