package gocycle.main;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.math.BigDecimal;
import gocycle.faults.*;
import gocycle.payments.*;
import gocycle.pricing.*;
import gocycle.stations.*;
import gocycle.trips.*;
import gocycle.vehicles.*;

//text menu for the GoCycle app.
public class Main {

    //menu
    static final String[] APP_OPTIONS = {
            "1) Rider options",
            "2) Operator options",
            "0) Exit"
    };

    static final String[] RIDER_OPTIONS = {
            "1) View stations and available vehicles",
            "2) Start a ride",
            "3) Return a vehicle",
            "4) Report a vehicle problem",
            "0) Back"
    };

    static final String[] OPERATOR_OPTIONS = {
            "1) View stations and vehicle status",
            "2) View reported faults",
            "3) Relocate a vehicle",
            "4) Take a vehicle out of service",
            "5) Restore a vehicle to service",
            "0) Back"
    };

    //app state
    static Scanner scanner =new Scanner(System.in);
    static StationNetwork network=new StationNetwork();
    static Map<String, Vehicle> vehicles=new LinkedHashMap<String, Vehicle>();
    static Fleet fleet = new Fleet(network, vehicles);
    static FaultService faults = new FaultService(fleet);
    static Rider rider;
    static PricingStrategy pricing;
    static Trip activeTrip;
    static Vehicle activeVehicle;
    static int tripCount = 0;

    public static void main(String[] args) {
        setup();

        while (true){
            System.out.println("Welcome to the GoCycle app! Please select from the following options:");
            switch (menu(APP_OPTIONS)) {
                case 1:
                    riderMenu();
                    break;
                case 2:
                    operatorMenu();
                    break;
                case 0:
                    System.out.println("Thank you for using the GoCycle app!");
                    return;
                default:
                    System.out.println("Please select a valid option.");
            }
        }
    }

    /* ---------------- Rider ---------------- */

    static void riderMenu() {
        //ask for rider details the first time only so an active ride stays with its rider
        if (rider==null) {
            String name =ask("Enter your name: ");
            String email =ask("Enter your email: ");
            rider =new Rider("R1",name, email);

            if (ask("Are you a student? (y/n): ").equalsIgnoreCase("y") ) {
                pricing =new StudentPricing() ;
            }
            else {
                pricing=new CasualPricing();
            }
        }

        while (true) {
            switch (menu(RIDER_OPTIONS)) {
                case 1:
                    viewVehicles();
                    break;
                case 2:
                    startRide();
                    break;
                case 3:
                    returnVehicle();
                    break;
                case 4:
                    reportProblem();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Please select a valid option.");
            }
        }
    }

    static void viewVehicles() {
        for (Station s :network.getStations()) {
            System.out.println("Station " + s.getId() + " (" + s.getZone().getName() + ")");
            for (DockingPoint p : s.getPoints()) {
                Vehicle v =p.getVehicle();
                if (v ==null) {
                    System.out.println("  " +p.getId() + ": free");
                } else if (v.isRentable()) {
                    System.out.println("  " +p.getId() + ": " + idOf(v) +" (available)");
                } else {
                    System.out.println("  " +p.getId() + ": " + idOf(v) + " (not available)");
                }
            }
        }
        System.out.println();
    }

    static void startRide() {
        if (activeTrip !=null) {
            System.out.println("You already have a ride in progress.");
            return;
        }

        Station station = findStation(ask("Enter station ID (e.g. S1): "));
        if (station==null) {
            System.out.println("No station with that ID.");
            return;
        }
        Vehicle vehicle = findVehicle(ask("Enter vehicle ID (e.g. PB1): "));
        if (vehicle ==null) {
            System.out.println("No vehicle with that ID.");
            return;
        }
        DockingPoint point =pointOf(station, vehicle);
        if (point == null){
            System.out.println("That vehicle is not docked at station " +station.getId() + ".");
            return;
        }
        if (!vehicle.isRentable()) {
            System.out.println(idOf(vehicle) +" cannot be rented right now (in use, out of service, or low battery).");
            return;
        }

        point.releaseVehicle();
        vehicle.startRental();
        tripCount++;
        activeTrip = new Trip("T" + tripCount, rider, "HOLD-" + tripCount, null);
        activeVehicle = vehicle;
        System.out.println("Ride " +activeTrip.getId() + " started on " + idOf(vehicle) + ".");
    }

    static void returnVehicle(){
        if (activeTrip ==null) {
            System.out.println("You do not have a ride in progress.");
            return;
        }

        Station station = findStation(ask("Enter station ID (e.g. S1): "));
        if (station ==null) {
            System.out.println("No station with that ID.");
            return;
        }

        //trip doesnt end until the vehicle is locked into a free point
        if (station.isFull()) {
            Station other =network.findNearestWithFreePoint(station);
            if (other ==null) {
                System.out.println("Station "+station.getId() +" is full and no other station has a free point.");
            } else {
                System.out.println("Station " + station.getId() + " is full. Nearest station with a free point: "+ other.getId() + " (" + String.format("%.2f", station.distanceTo(other)) + " km).");
            }
            return;
        }

        int minutes =number("Enter ride length in minutes (simulated): ");
        if (minutes <0) {
            System.out.println("Please enter a whole number of 0 or more.");
            return;
        }

        station.findFreePoint().dock(activeVehicle);
        activeVehicle.finishRental();
        activeTrip.end(LocalDateTime.now().plusMinutes(minutes));
        BigDecimal fare = pricing.calculateFare(activeTrip.getDurationMinutes());
        activeTrip.setFare(fare);
        activeTrip.markPaid();
        System.out.println("Ride " +activeTrip.getId() + " ended. Fare: $" + fare.toPlainString());

        ReceiptSender[] senders ={ new AppReceiptSender(), new EmailReceiptSender() };
        for (ReceiptSender sender : senders) {
            try {
                sender.send(activeTrip);
            }
            catch (IllegalStateException excep) {
                System.out.println("Receipt not sent: " + excep.getMessage());
            }
        }

        activeTrip = null;
        activeVehicle = null;
    }

    static void reportProblem() {
        Vehicle vehicle = findVehicle(ask("Enter vehicle ID (e.g. PB1): "));
        if (vehicle == null) {
            System.out.println("No vehicle with that ID.");
            return;
        }

        FaultType[] types = FaultType.values();
        for (int i=0; i <types.length; i++) {
            System.out.println((i+1) + ") " + types[i]);
        }
        int choice = number("Enter the problem number: ");
        if (choice < 1||choice >types.length) {
            System.out.println("Please select a valid option.");
            return;
        }

        String description =ask("Describe the problem: ");
        try {
            FaultReport report = faults.reportFault(vehicle, types[choice - 1], FaultSource.RIDER, description);
            System.out.println("Problem reported: " + report);
            System.out.println(idOf(vehicle) + " is now out of service.");
        } catch (IllegalArgumentException excep) {
            System.out.println(excep.getMessage());
        }
    }

    /* ---------------- Operator ---------------- */

    static void operatorMenu() {
        while (true) {
            switch (menu(OPERATOR_OPTIONS)) {
                case 1:
                    viewStatus();
                    break;
                case 2:
                    viewFaults();
                    break;
                case 3:
                    relocateVehicle();
                    break;
                case 4:
                    takeOutOfService();
                    break;
                case 5:
                    restoreToService();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Please select a valid option.");
            }
        }
    }

    static void viewStatus() {
        System.out.println("Stations:");
        for (Map.Entry<String, String> e : fleet.getStationStatuses().entrySet()) {
            System.out.println("  " + e.getKey() + ": "+ e.getValue());
        }
        System.out.println("Vehicles:");
        for (Map.Entry<String, String> e : fleet.getVehicleStatuses().entrySet()) {
            System.out.println("  " + e.getKey() + ": " + e.getValue());
        }
        System.out.println();
    }

    static void viewFaults() {
        List<FaultReport> reports =faults.getReports();
        if (reports.isEmpty()) {
            System.out.println("No faults have been reported.");
        }
        for (FaultReport r :reports) {
            boolean open =faults.getOpenFaults(r.getVehicle()).contains(r);
            System.out.println((open ? "[OPEN] " :"[RESOLVED] ") + idOf(r.getVehicle()) + ": " + r);
        }
        System.out.println();
    }

    static void relocateVehicle() {
        Vehicle vehicle = findVehicle(ask("Enter vehicle ID (e.g. PB1): "));
        if (vehicle == null) {
            System.out.println("No vehicle with that ID.");
            return;
        }
        String destination = ask("Enter destination station ID: ").toUpperCase();
        try {
            fleet.relocateVehicle(vehicle, destination);
            System.out.println(idOf(vehicle) + " moved to station " + destination + ".");
        } catch (RuntimeException e) {
            System.out.println("Could not relocate: " + e.getMessage());
        }
    }

    static void takeOutOfService(){
        Vehicle vehicle = findVehicle(ask("Enter vehicle ID (e.g. PB1): "));
        if (vehicle == null) {
            System.out.println("No vehicle with that ID.");

            return;
        }
        faults.takeOutOfService(vehicle);
        System.out.println(idOf(vehicle) +" is now out of service.");
    }

    /* Restoring a vehicle counts as the repair being done, so its open faults are resolved first */
    static void restoreToService() {
        Vehicle vehicle = findVehicle(ask("Enter vehicle ID (e.g. PB1): "));
        if (vehicle == null) {
            System.out.println("No vehicle with that ID.");
            return;
        }
        try {
            for (FaultReport r :faults.getOpenFaults(vehicle)) {
                faults.resolveFault(r);
            }
            faults.restoreService(vehicle);
            System.out.println(idOf(vehicle) + " is back in service.");
        } catch (RuntimeException excep) {
            System.out.println("Could not restore: " +excep.getMessage());
        }
    }

    /* ---------------- Setup and helpers ---------------- */

    /* 2 zones, 3 stations, 5 vehicles */
    static void setup() {
        Zone downtown =new Zone("Downtown", "logos/downtown.png", "blue");
        Zone campus =new Zone("Campus", "logos/campus.png", "red");
        for (PaymentMethodType m : PaymentMethodType.values()) {
            downtown.addAllowedPayment(m);
            campus.addAllowedPayment(m);
        }

        network.addStation(new Station("S1", 51.0447, -115.0729, downtown, 4));
        network.addStation(new Station("S2", 51.0786, -115.1209, campus, 3));
        network.addStation(new Station("S3", 51.0486, -115.0345, downtown, 2));

        addVehicle("PB1", new PedalBike("PB1"), "S1");
        addVehicle("PB2", new PedalBike("PB2"), "S1");
        addVehicle("EB1", new ElectricBike("EB1", 80), "S2");
        addVehicle("EB2", new ElectricBike("EB2", 15), "S2");   /* battery below 20, so not rentable */
        addVehicle("ES1", new ElectricScooter("ES1", 60), "S3");

        faults.addObserver(new MaintenanceCrewNotifier());
    }

    static void addVehicle(String id, Vehicle vehicle, String stationId) {
        vehicles.put(id, vehicle);
        findStation(stationId).findFreePoint().dock(vehicle);
    }

    static String ask(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /* Returns the number typed, or -1 if it is not a whole number */
    static int number(String prompt) {
        try {
            return Integer.parseInt(ask(prompt));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static int menu(String[] options) {
        for (String option : options) {
            System.out.println(option);
        }
        return number("Please enter a menu number: ");
    }

    static Station findStation(String id) {
        for (Station s : network.getStations()) {
            if (s.getId().equalsIgnoreCase(id)) {
                return s;
            }
        }
        return null;
    }

    static Vehicle findVehicle(String id) {
        return vehicles.get(id.toUpperCase());
    }

    static String idOf(Vehicle vehicle) {
        for (Map.Entry<String, Vehicle> e :vehicles.entrySet()) {
            if (e.getValue() ==vehicle) {
                return e.getKey();
            }
        }
        return "?";
    }

    static DockingPoint pointOf(Station station, Vehicle vehicle) {
        for (DockingPoint p : station.getPoints()) {
            if (p.getVehicle() == vehicle) {
                return p;
            }
        }
        return null;
    }

    static Station stationOf(Vehicle vehicle) {
        for (Station s :network.getStations()) {
            if (pointOf(s, vehicle) != null) {
                return s;
            }
        }
        return null;
    }

}
