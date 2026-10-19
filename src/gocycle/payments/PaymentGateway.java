package gocycle.payments;

import gocycle.trips.*;
import java.math.BigDecimal;
public interface PaymentGateway {
    /**
     * Places a refundable deposit hold when a trip starts.
     * @param rider
     * @param amount
     * @param method
     * @return id of hold
     * @throws IllegalArgumentException if argument is null or amount is not positive
     * @throws IllegalStateException if hold cannot be placed
     */
    String placeHold(Rider rider, BigDecimal amount, PaymentMethodType method);

    /**
     * Stops a hold when the trip ends of fails to start.
     * @param holdId
     */
    void stopHold(String holdId);

    /**
     * Charges the final far
     * @param rider
     * @param amount
     * @param method
     * @return true if payment is successful, false if declined
     * @throws IllegalArgumentException if argument is null or amount is negative
     */
    boolean pay(Rider rider, BigDecimal amount, PaymentMethodType method);
}
