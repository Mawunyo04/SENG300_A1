package gocycle.pricing;

import java.math.BigDecimal;

/**
* Specifies the casual rider rate of $0.40 per minute.
* As per QR 1, the casual rider rate is kept separate from the shared fare
* calculation so it can be easily adjusted as required in the future.
*/
public class CasualPricing extends TimeBasedPricing {

    @Override
    protected BigDecimal perMinuteRate() {

        BigDecimal rate = new BigDecimal("0.40");

        return rate;
    }
}     