package gocycle.stations;

import java.util.ArrayList;
import java.util.List;


public class StationNetwork {

    private List<Station> stations;

    public StationNetwork() {
        stations = new ArrayList<Station>();
        
    }

    public void addStation(Station s) {
        stations.add(s);
        
    }

    public List<Station> getStations() {
        return stations;
    }

    //find closest station that is not full 
    public Station findNearestWithFreePoint(Station from) {
       if (from == null){
        return null;
       }
       Station nearest = null;
       double shortest = 0;
       //skip station you are leaving
       for(Station s : stations){
        if (s == from){
            continue;
        }
        //skip full stations
        if (s.isFull()){
            continue;
        }
        double distance = from.distanceTo(s);
        if (nearest == null || distance < shortest){
            nearest = s;
            shortest = distance;
        }
       }
       return nearest;
    }

    //prints evey station
    public void printNetworkState() {
        System.out.println("--- NETWORK STATE ---");
        for (Station s : stations){
            s.printState();
        }
        
    }
}