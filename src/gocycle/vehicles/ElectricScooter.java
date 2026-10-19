package vehicles;

public class ElectricScooter extends ElectricVehicle {

    public ElectricScooter(String id, int batteryLevel) {
        super(id, batteryLevel);
    }

    @Override
    public boolean isRentable() {
        // TODO: implement
        return false;
    }
}