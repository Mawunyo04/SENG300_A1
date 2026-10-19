package gocycle.stations;

import java.util.ArrayList;
import java.util.List;


public class Station {

    private String id;
    private double latitude;
    private double longitude;
    private Zone zone;
    private List<DockingPoint> points;
    private StationScreen screen;

    public Station(String id, double latitude, double longitude, Zone zone, int numberOfPoints) {
        // a station must have at least one docking point
        if (numberOfPoints < 1) {
            throw new IllegalArgumentException("A station needs at least one docking point.");
        }
 
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zone = zone;
        this.screen = new StationScreen(zone);
 
        // build the docking points: S1-P1, S1-P2, ...
        this.points = new ArrayList<DockingPoint>();
        for (int i = 1; i <= numberOfPoints; i++) {
            DockingPoint p = new DockingPoint(id + "-P" + i);
            points.add(p);
        }

        
    }

    public String getId() {
        return id;
    }

    public Zone getZone() {
        return zone; 
    }

    public StationScreen getScreen() {
        return screen; 
    }

    public List<DockingPoint> getPoints() {
        return points; 
    }

    
    public DockingPoint findFreePoint() {
        //retrun first free point, null if there are none
        for (DockingPoint p : points) {
            if (p.isFree()) {
                return p;
            }
        }
        return null;
    }

    //full if there is no free point
    public boolean isFull() {
        return findFreePoint() == null; 
    }

    //caclulate approximate dist. to another station in km
    public double distanceTo(Station other) {
        double kmPerDegree = 111.0;
        double averageLatitude = (this.latitude + other.latitude) / 2;
 
        double northSouth = other.latitude - this.latitude;
        double eastWest = (other.longitude - this.longitude)
                * Math.cos(Math.toRadians(averageLatitude));
 
        double degrees = Math.sqrt(northSouth * northSouth + eastWest * eastWest);
        return degrees * kmPerDegree;
    }

    //print station and all its points
    //prints station id zone points and a note if station is full
    public void printState() {
        System.out.println("Station " + id + " (" + zone.getName() + ")");
        for (DockingPoint p : points) {
            System.out.println("   " + p);
        }
        if (isFull()) {
            System.out.println("   -> This station is full");
        }
    }

    public String toString() {
        return "Station" + id; 
    }
}
