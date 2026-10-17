package gocycle.payments;

import java.math.BigDecimal;
public interface PaymentGateway {
    /**
     * Places a refundable deposit hold when a trip starts.
     * @param rider
     * @param amount to hold, must be >0
     * @param method how rider is paying
     * @return id of hold
     * @throws IllegalArgumentException if argument is null or amount is not positive
     * @throws IllegalStateException if hold cannot be placed
     */
    String placeHold(Rider rider, BigDecimal amount, PaymentMethodType method);

    /**
     * Stops a hold when the trip ends of fails to start.
     */
    void releaseHold(String holdId);

    /**
     * Charges the final far
     * @param rider
     * @param amount fare to charge, cannot be null/-
     * @param method how rider is paying
     * @return true if payment is successful, false if declined
     * @throws IllegalArgumentException if argument is null or amount is negative
     */
    boolean pay(Rider rider, BigDecimal amount, PaymentMethodType method);
}
