package gocycle.stations;

import gocycle.vehicles.Vehicle;


public class DockingPoint {

    private String id;
    private boolean locked;
    private Vehicle vehicle; 

    public DockingPoint(String id) {
        //save id, set locked to false, set vehicle to null
        this.id = id;
        this.locked = false;
        this.vehicle = null;
    }

    public String getId() {
        return id; 
    }

    
    public Vehicle getVehicle() {
        return vehicle; 
    }

    public boolean isFree() {
        return vehicle == null; //point is free if vehicle is null
    }

    public boolean isLocked() {
        return locked; 
    }

    
    public boolean dock(Vehicle v) {
        //returns true only if vehicle is held and locked

        if (v == null){
            return false;
        }
        if (!isFree()){
            return false;
        }
        vehicle = v;
        locked = true;
        return true;
        
    }

    
    public Vehicle releaseVehicle() {
        // unlock point and give vehicle back
        //return null if empty

        if (isFree()){
            return null;
        }
        Vehicle released = vehicle;
        vehicle = null;
        locked = false;
        return released;     
    }

    public String toString() {
        if (isFree()){
            return "Point " + id + " is free";
        }
        return "Point " + id + " holds " + vehicle + " (locked: " + locked + ")";
    }
}