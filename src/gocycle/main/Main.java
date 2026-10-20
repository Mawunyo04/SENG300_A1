package gocycle.main;

import java.util.*;

/* CONFIG imports */
import gocycle.payments.*;
import gocycle.pricing.*;
import gocycle.trips.*;
import gocycle.vehicles.*;
import gocycle.stations.*;

public class Main {
    
    /* Class fields */
    public static int USER_OPTION;

    public static Console console = new Console();
    public static Scanner scanner = new Scanner(System.in);

    /* 
    * GoCycle app needs to do the following:
    *   1) Rider options
    *   2) Operator options                 <- Not implementing role checking/security
    *   3) Output demonstration options
    *   0) Exit
    * 
    * NOTE: There's only one Rider/Operator per run anyway...
    */


    /* This program will run using System.out.println and Scanner */
    public static void main(String[] args) {

        while (true) {

            /* Begin program */
            System.out.println(console.WELCOME_MESSAGE);

            try {

                USER_OPTION = userOption();
            } catch (NumberFormatException e) {

                System.out.println(console.ENTER_OPTION);
                continue;
            }

            switch (USER_OPTION) {
                case 1:

                    riderWork();
                    break;
                case 2:

                    operatorWork();
                    break;
                case 0:

                    System.out.println(console.GOODBYE_MESSAGE);
                    return;
                default:

                    System.out.println(console.ERROR_MESSAGE);
            }
        }
    }

    /* Helper method to collect user selection and run app according to user's entered value */
    public static int userOption() {

        int option = 0;
        for (String x : console.APP_OPTIONS) System.out.println(x + "\n");

        /* Do we have time to do error checking */
        option = Integer.parseInt(scanner.nextLine().trim());

        return option;
    }

    /* Handles all Rider work and features */
    public static void riderWork() {

        /* Collect Rider information */
        System.out.println(console.NEW_RIDER[0] + "\n");
        String riderName = scanner.nextLine();

        System.out.println(console.NEW_RIDER[1] + "\n");
        String riderEmail = scanner.nextLine();

        /* Instantiate new Rider with a fixed ID for this session */
        Rider rider = new Rider("R1", riderName, riderEmail);

        while (true) {
            for (String x : console.RIDER_OPTIONS) System.out.println(x + "\n");

            /* Collect the rider's menu selection */
            int rOption;

            try {
                rOption = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(console.ENTER_OPTION);
                continue;
            }

            switch (rOption) {
                case 1:
                    // {view station data stuff here}
                    break;
                case 2:
                    // rider.{start new ride logic here}
                    break;
                case 3:
                    // {return a vehicle after a ride}
                    break;
                case 4:
                    // {report vehicle issues}
                    break;
                case 0:
                    return;
                default:
                    System.out.println(console.ERROR_MESSAGE);
            }
        }
    }

    /* Handles all available operator features */
    public static void operatorWork() {

        while (true) {
            for (String x : console.OPERATOR_OPTIONS) {
                System.out.println(x);
            }

            /* Collect the operator's menu selection */
            int oOption;

            try {
                oOption = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(console.ENTER_OPTION);
                continue;
            }

            switch (oOption) {
                case 1:
                    // {view stations and vehicle status}
                    break;
                case 2:
                    // {view reported faults}
                    break;
                case 3:
                    // {relocate a vehicle}
                    break;
                case 4:
                    // {take a vehicle out of service}
                    break;
                case 5:
                    // {restore a vehicle to service}
                    break;
                case 0:
                    return;
                default:
                    System.out.println(console.ERROR_MESSAGE);
            }
        }
    }
    
}
