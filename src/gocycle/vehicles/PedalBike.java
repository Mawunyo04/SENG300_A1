package vehicles;

public class PedalBike extends Vehicle {

    public PedalBike(String id) {
        super(id);
    }

    @Override
    public boolean isRentable() {
        // Pedal bikes are rentable only if:
        // 1. They are AVAILABLE
        // 2. They are not OUT_OF_SERVICE or IN_USE

        return status == VehicleStatus.AVAILABLE;
    }
}