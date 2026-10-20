package gocycle.vehicles;

public class PedalBike extends Vehicle {

    /**
     * pedal bike constructor
     * @param id pedal bike identification
     */
    public PedalBike(String id) {
        super(id);
    }

    /**
     * determines if pedal bike is rentable
     * @return status of pedal bike
     */
    @Override
    public boolean isRentable() {
        // Pedal bikes are rentable only if:
        // 1. They are AVAILABLE
        // 2. They are not OUT_OF_SERVICE or IN_USE

        return status == VehicleStatus.AVAILABLE;
    }
}