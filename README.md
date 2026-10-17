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
        
             
        ├── (ADD UR PACKAGE STRUCTURES HERE)                 
        ├── 
        ├── 
        ├── 
        └── [Main.java]                
```


## Build and run

(NEED TO DO)




Or open the project in IntelliJ and run `[MainClassName]`.

## Git workflow
Each member works on a feature branch and merges into `main` through a pull request .

Branches: 

    - feature/payments
    - (ADD UR BRANCHES HERE) 
