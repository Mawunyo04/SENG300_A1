package gocycle.vehicles;

/**
 * Represents an abstract vehicle that can be rented
 * Stores the vehicle's ID and current status
 */
public abstract class Vehicle {

    protected String id;
    protected VehicleStatus status;

    /**
     * Constructs a vehicle with the given ID and sets its status to AVAILABLE
     * @param id the unique identifier of the vehicle
     */
    public Vehicle(String id) {
        this.id = id;
        this.status = VehicleStatus.AVAILABLE;
    }

    /**
     * Checks whether the vehicle is available for rental
     *
     * @return true if the vehicle is rentable, false otherwise
     */
    public abstract boolean isRentable();

    /**
     * Starts a rental if the vehicle is rentable
     * Changes the vehicle's status to IN_USE
     */
    public void startRental() {
        // Can only start rental if the vehicle is rentable
        if (isRentable()) {
            this.status = VehicleStatus.IN_USE;
        }
    }
    /**
     * Finishes a rental if the vehicle is currently in use
     * Changes the vehicle's status to AVAILABLE
     */
    public void finishRental() {
        // Can only finish rental if currently in use
        if (this.status == VehicleStatus.IN_USE) {
            this.status = VehicleStatus.AVAILABLE;
        }
    }

    /**
     * Marks the vehicle as out of service
     * Changes the vehicle's status to OUT_OF_SERVICE
     */
    public void markOutOfService() {
        this.status = VehicleStatus.OUT_OF_SERVICE;
    }

    /**
     * Restores the vehicle to service if it is currently out of service
     * Changes the vehicle's status to AVAILABLE
     */
    public void restoreService() {
        // Restore to AVAILABLE only if previously out of service
        if (this.status == VehicleStatus.OUT_OF_SERVICE) {
            this.status = VehicleStatus.AVAILABLE;
        }
    }


    /**
     * Sets the vehicle's status to the specified status
     * @param status the new status of the vehicle
     */
    public void setStatus(VehicleStatus status) {
        this.status = status;
    }
}
