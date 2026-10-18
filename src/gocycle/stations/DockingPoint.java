package gocycle.stations;

import gocycle.vehicles.Vehicle;


public class DockingPoint {

    private String id;
    private boolean locked;
    private Vehicle vehicle; 

    public DockingPoint(String id) {
        //save id, set locked to false, set vehicle to null
       
    }

    public String getId() {
        return null; 
    }

    
    public Vehicle getVehicle() {
        return null; 
    }

    public boolean isFree() {
        return false; //point is free if vehicle is null
    }

    public boolean isLocked() {
        return false; 
    }

    
    public boolean dock(Vehicle v) {
       //returns true only if vehicle is held and locked
        
        return false;
    }

    
    public Vehicle releaseVehicle() {
        // unlock point and give vehicle back
        //return null if empty
        return null;
    }

    public String toString() {
        return null; 
    }
}