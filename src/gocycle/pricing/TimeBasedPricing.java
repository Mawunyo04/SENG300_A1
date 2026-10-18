package gocycle.pricing;

import java.math.BigDecimal;

public abstract class TimeBasedPricing implements PricingStrategy {

    /* Sample fees chosen for this implementation */    
    protected BigDecimal unlockFee          = new BigDecimal("1.50");
    protected BigDecimal longTripSurcharge  = new BigDecimal("5.00");

    protected int longTripMinutes           = 120;

    /* Define the accessor that each pricing type will use */
    protected abstract BigDecimal perMinuteRate();

    /**
    * Calculates the fare using this pricing strategy (refer to FR 7, FR 9)
    *
    * @param minutes nonnegative billable trip duration in minutes
    * @return the calculated trip fare
    */
    @Override
    public BigDecimal calculateFare(long minutes) {

        BigDecimal fee;
        BigDecimal minRate;

        /* Calculate rider's accumulated minutely rate */
        BigDecimal mins = BigDecimal.valueOf(minutes);
        minRate         = perMinuteRate().multiply(mins);

        fee = unlockFee.add(minRate);

        /* Determine if fare requires surcharge fee by rider's total time */
        if (minutes > longTripMinutes) fee = fee.add(longTripSurcharge);

        return fee;
    }

}