package gocycle.stations;

import java.util.List;


public class Station {

    private String id;
    private double latitude;
    private double longitude;
    private Zone zone;
    private List<DockingPoint> points;
    private StationScreen screen;

    public Station(String id, double latitude, double longitude, Zone zone, int numberOfPoints) {
        
    }

    public String getId() {
        return null;
    }

    public Zone getZone() {
        return null; 
    }

    public StationScreen getScreen() {
        return null; 
    }

    public List<DockingPoint> getPoints() {
        return null; 
    }

    
    public DockingPoint findFreePoint() {
        //retrun first free point, null if there are none

        
        return null;
    }

    //full if there is nop free point
    public boolean isFull() {
        return false; 
    }

    //caclulate approximate dist. to another station in km
    public double distanceTo(Station other) {
        
        return 0.0;
    }

    //print station and all its points
    //prints station id zone points and a note if station is full
    public void printState() {
        
    }

    public String toString() {
        return null; 
    }
}
