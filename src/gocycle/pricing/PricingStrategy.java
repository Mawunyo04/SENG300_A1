package gocycle.pricing;

import java.math.BigDecimal;

/**
* Defines the operation for calculating a trip's fare from its duration 
* in minutes
*/
public interface PricingStrategy {
    
    /**
    * Calculates the fare using this pricing strategy (refer to FR 7, FR 9)
    *
    * @param minutes nonnegative billable trip duration in minutes
    * @return the calculated trip fare
    */
    BigDecimal calculateFare(long minutes);
}