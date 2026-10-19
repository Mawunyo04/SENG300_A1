package gocycle.pricing;

import java.math.BigDecimal;

/**
* Specifies the student rider rate of $0.15 per minute.
* As per QR 1, the student rider rate is kept separate from the shared fare
* calculation so it can be easily adjusted as required in the future.
*/
public class StudentPricing extends TimeBasedPricing {

    @Override
    protected BigDecimal perMinuteRate() {

        BigDecimal rate = new BigDecimal("0.15");

        return rate;
    }
}