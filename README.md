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
        ├── main/
        │   ├── Main.java
        └──

```
## Requirements

- Java JDK 21 or newer (tested with Temurin 21)
- A terminal (Terminal on Mac/Linux, PowerShell on Windows)

## Build

From the project root folder:

```
mkdir -p out
javac -d out $(find src -name "*.java")
```

## Run

```
java -cp out gocycle.main.Main
```

Type a menu number and press Enter. Choose `1` for Rider options or `2` for Operator options, and `0` to go back or exit.


## Running in IntelliJ IDEA

1. Open the project folder in IntelliJ.
2. Open `src/gocycle/main/Main.java`.
3. Click the green arrow next to `public static void main` and choose Run.


Branches: 

    - main
    - feature/payments
    - feature/faults
    - feat_trips
    - feature/stations
    - feature/payments
    - chylsy-work
    - sara's-work
