package vehicles;

public class PedalBike extends Vehicle {

    public PedalBike(String id) {
        super(id);
    }

    @Override
    public boolean isRentable() {
        // TODO: implement
        return false;
    }
}