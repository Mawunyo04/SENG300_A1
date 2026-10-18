package gocycle.pricing;

import java.math.BigDecimal;

/**
* Specifies the member rider rate of $0.20 per minute.
* As per QR 1, the member rider rate is kept separate from the shared fare
* calculation so it can be easily adjusted as required in the future.
*/
public class MemberPricing extends TimeBasedPricing {

    @Override
    protected BigDecimal perMinuteRate() {

        BigDecimal rate = new BigDecimal("0.20");

        return rate;
    }
}     