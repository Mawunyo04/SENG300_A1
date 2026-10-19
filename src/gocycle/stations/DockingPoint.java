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
            System.out.println("Point " + id + ": there is no vehicle to dock.");
            return false;
        }
        if (!isFree()){
            System.out.println("Point " + id + ": is occupied.");
            return false;
        }
        vehicle = v;
        locked = true;
        System.out.println("Point " + id + ": " + v + " is docked and locked.");
        return true;
        
    }

    
    public Vehicle releaseVehicle() {
        // unlock point and give vehicle back
        //return null if empty

        if (isFree()){
            System.out.println("Point " + id + ": Empty");
            return null;
        }
        Vehicle released = vehicle;
        vehicle = null;
        locked = false;
        System.out.println("Point " + id + ": " + released + " was released."); 
        return released;     
    }

    public String toString() {
        if (isFree()){
            return "Point " + id + " is free";
        }
        return "Point " + id + " holds " + vehicle + " (locked: " + locked + ")";
    }
}