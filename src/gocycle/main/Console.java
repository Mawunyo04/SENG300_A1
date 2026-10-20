package gocycle.main;

/* 
* The purpose of this class is to store and manage text outputs for the GoCycle user interface.
* Having the inputs in one class makes it easy to make changes to the app in the future.
*/
public class Console {

    /* Main program outputs */
    public String WELCOME_MESSAGE = "Welcome to the GoCycle app! Please select from the following options:";
    public String ENTER_OPTION    = "Please enter a menu number: ";
    public String ERROR_MESSAGE   = "Please select a valid option.";
    public String GOODBYE_MESSAGE = "Thank you for using the GoCycle app!";

    public String[] APP_OPTIONS = {

        "1) Rider options",
        "2) Operator options",
        "0) Exit"
    };

    /* Outputs for Rider functionality */
    public String[] NEW_RIDER = {

        "Enter your name:   ",
        "Enter your email:   "
    };

    public String[] RIDER_OPTIONS = {

        /* As per FR 1-5, FR 10-13 */
        "1) View stations and available vehicles",
        "2) Start a ride",
        "3) Return a vehicle",
        "4) Report a vehicle problem",
        "0) Back"
    };

    /* Outputs for Operator functionality */
    public String[] OPERATOR_OPTIONS = {

        /* As per FR 16, FR 21-23 */
        "1) View stations and vehicle status",
        "2) View reported faults",
        "3) Relocate a vehicle",
        "4) Take a vehicle out of service",
        "5) Restore a vehicle to service",
        "0) Back"
    };

    /* Default constructor */
    public Console () {

    }

}
