package gocycle.vehicles;

public abstract class Vehicle {

    protected String id;
    protected VehicleStatus status;

    public Vehicle(String id) {
        this.id = id;
        this.status = VehicleStatus.AVAILABLE;
    }

    public abstract boolean isRentable();

    public void startRental() {
        // Can only start rental if the vehicle is rentable
        if (isRentable()) {
            this.status = VehicleStatus.IN_USE;
        }
    }

    public void finishRental() {
        // Can only finish rental if currently in use
        if (this.status == VehicleStatus.IN_USE) {
            this.status = VehicleStatus.AVAILABLE;
        }
    }

    public void markOutOfService() {
        this.status = VehicleStatus.OUT_OF_SERVICE;
    }

    public void restoreService() {
        // Restore to AVAILABLE only if previously out of service
        if (this.status == VehicleStatus.OUT_OF_SERVICE) {
            this.status = VehicleStatus.AVAILABLE;
        }
    }
}
