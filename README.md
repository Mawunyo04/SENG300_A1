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
        ├── pricing/                  
        │   ├── CasualPricing.java       
        │   ├── MemberPricing.java       
        │   ├── PricingStrategy.java       
        │   ├── StudenPricing.java       
        │   ├── TimeBasedPricing.java
        ├── stations/                  
        │   ├── DockingPoint.java       
        │   ├── Station.java       
        │   ├── StationNetwork.java       
        │   ├── StationScreen.java       
        │   ├── Zone.java      
        ├── trips/                  
        │   ├── Rider.java       
        │   ├── Membership.java       
        │   ├── Trip.java             
        ├── testing/              
        │   ├── PaymentGatewayTest.java       
        │   ├── PricingTest.java       
        │   ├── ReceiptSenderTest.java


        └──(ADD UR PACKAGES HERE)

```
## Requirements
- Java 11 or newer (a JDK)
- IntelliJ IDEA

## Build
From the project's top folder (macOS/Linux terminal):

    javac -d out $(find src -name "*.java" -not -path "*/testing/*")

No output means the code compiled. The compiled files go in the `out` folder.

In IntelliJ (any operating system): Build > Build Project.

## Run
This project is a set of core classes (trips, pricing, payments, faults, etc.) and has
no main program. The classes are exercised by the JUnit tests in `src/gocycle/testing`.

1. Open any test file. If `org.junit` is red, hover over it and choose
   "Add JUnit4 to classpath".
2. Right-click `src/gocycle/testing` > Run 'Tests in gocycle.testing'.

The payment classes print their actions (for example "[PAYMENT] Placed hold ...") in the
IntelliJ console while the tests run.
## Git workflow
Each member works on a feature branch and merges into `main` through a pull request .

Branches: 

    - main
    - feature/payments
    - feat_trips
    - chylsy-work
    - feature/stations
    - fix/gitignore-indentation
    - sara's-work
    (ADD UR BRANCHES HERE) 

