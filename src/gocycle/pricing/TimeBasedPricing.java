package gocycle.pricing;

import java.math.BigDecimal;

public abstract class TimeBasedPricing implements PricingStrategy {

    protected BigDecimal unlockFee;
    protected BigDecimal longTripSurcharge;
    protected int longTripMinutes = 120;

    protected abstract BigDecimal perMinuteRate();
}