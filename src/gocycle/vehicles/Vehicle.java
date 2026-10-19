package vehicles;

public abstract class Vehicle {

    protected String id;
    protected VehicleStatus status;

    public Vehicle(String id) {
        this.id = id;
        this.status = VehicleStatus.AVAILABLE;
    }

    public abstract boolean isRentable();

    public void startRental() {
        // TODO: implement
    }

    public void finishRental() {
        // TODO: implement
    }

    public void markOutOfService() {
        // TODO: implement
    }

    public void restoreService() {
        // TODO: implement
    }
}