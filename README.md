# GoCycle (SENG 300 Assignment 1)

A shared bike and scooter service for the city's Mobility Office. Riders unlock
a vehicle at a docking station, ride it, and return it to any station with a
free docking point.

## Team
| Name | UCID | Section |
|---|---|---|
| Favour |30245516 | Trips and Pricing |
| Chylsy |30241044 | Trips and Pricing |
| Ramsha |30275772 | Payments and Receipts |
| Mackenzie |30242155 | Faults and Operator Functions |
| Parth |30250082 | Faults and Operator Functions |
| Sara |30241537 | Vehicles and Fleet |
| Nadine |30229594 | Stations and Zones |

## Design
Patterns used:
- **Strategy**: `PricingStrategy` with casual, member and student pricing
- **Observer**: `FaultService` notifies the maintenance crew and operators
- **Template Method**: `TimeBasedPricing`

The code follows the SOLID principles; see the report for the class diagram
and design rationale.

## Project structure

```
SENG300_A1/
├── README.md
├── .gitignore
└── src/
    └── gocycle/
        ├── payments/                  
        │   ├── PaymentMethodType.java       
        │   ├── PaymentGateway.java       
        │   ├── SimulatedPaymentGateway.java 
        │   ├── ReceiptSender.java          
        │   ├── ReceiptText.java            
        │   ├── AppReceiptSender.java       

        │   └── EmailReceiptSender.java 
        ├── faults/
        │   ├── FaultType.java
        │   ├── FaultSource.java
        │   ├── FaultReport.java
        │   ├── FaultObserver.java
        │   ├── FaultService.java
        │   ├── FleetAccess.java
        │   ├── MaintenanceCrewNotifier.java
        │   └── OperatorConsole.java
        ├── pricing/
        │   ├── CasualPricing.java
        │   ├── MemberPricing.java
        │   ├── PricingStrategy.java
        │   ├── StudentPricing.java
        │   └── TimeBasedPricing.java
        ├── stations/
        │   ├── DockingPoint.java
        │   ├── Station.java
        │   ├── StationNetwork.java
        │   ├── StationScreen.java
        │   └── Zone.java
        ├── trips/
        │   ├── Rider.java
        │   ├── Membership.java
        │   └── Trip.java
        ├── testing/
        │   ├── PaymentGatewayTest.java
        │   ├── PricingTest.java
        │   └── ReceiptSenderTest.java
        └──(ADD UR PACKAGES HERE)

```
## Requirements
- Java 11 or newer (a JDK)
- IntelliJ IDEA

## Build

--- Build and run ---

                //* Prerequisites *//

* Language Runtime: Java Development Kit (JDK) 17 or higher
* Editor: Any Java editor or IDE (VS Code with the Extension Pack for Java, IntelliJ IDEA, or Eclipse)
* The only library is the JUnit jar, which is only needed to run the tests
* System Tools: Git

                //* Environment Setup*//

Clone the repository then navigate into the project directory:
```bash
git clone https://github.com/OWNER/REPO-NAME.git
cd REPO-NAME
```
                //* Build instructions *//

OPTION 1 : Open the project folder in your Java editor. The editor compiles all of the code in `src` automatically so no build command is needed.

OPTION 2:  Compile from the terminal instead (run from the project root, the folder containing `src`)


                //* Run instructions *//

OPTION 1: Open `src/gocycle/main/Main.java` in your editor and click Run.

OPTION 2: Run from the terminal 




Branches: 

    - main
    - feature/payments
    - feature/faults
    - feat_trips
    - feature/stations
    - feature/payments
    - chylsy-work
    - sara's-work
