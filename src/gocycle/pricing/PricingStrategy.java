package gocycle.pricing;

import java.math.BigDecimal;

public interface PricingStrategy {
    
    BigDecimal calculateFare(long minutes);
}