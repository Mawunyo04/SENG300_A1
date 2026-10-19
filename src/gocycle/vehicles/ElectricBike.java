package vehicles;

public class ElectricBike extends ElectricVehicle {

    public ElectricBike(String id, int batteryLevel) {
        super(id, batteryLevel);
    }

    @Override
    public boolean isRentable() {
        // TODO: implement
        return false;
    }
}