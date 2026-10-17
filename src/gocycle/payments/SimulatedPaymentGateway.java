package gocycle.payments;

import gocycle.trips.Rider;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Pretend payment provider for the console application and for tests.
 */
public class SimulatedPaymentGateway implements PaymentGateway {

    //all holds that are currently in place w/ hold id mapped to the amount
    private final Map<String,BigDecimal> activeHolds = new HashMap<>();
    private final boolean declinePayments;

    //creates a gateway whose payments are successful
    public SimulatedPaymentGateway() {
        this(false);
    }

    /**
     * Creates a gateway with a chosen payment.
     * @param declinePayments
     */
    public SimulatedPaymentGateway(boolean declinePayments) {
        this.declinePayments=declinePayments;
    }

    @Override
    public String placeHold(Rider rider, BigDecimal amount,PaymentMethodType method) {
        requireValid(rider,amount,method);

        //hold of 0 is rejected
        if (amount.signum()==0) {
            throw new IllegalArgumentException("A deposit hold must be greater than zero");
        }

        //random id keeps every hold unique without needing a counter.
        String holdId = "HOLD-"+UUID.randomUUID();
        activeHolds.put(holdId, amount);

        System.out.println("[PAYMENT] Placed hold "+holdId +" of $"+ amount.toPlainString() + " via " +method);
        return holdId;
    }

    @Override
    public void releaseHold(String holdId) {
        //remove() returns null when the id is unknown
        BigDecimal released=activeHolds.remove(holdId);
        if (released !=null){
            System.out.println("[PAYMENT] Released hold " + holdId + " of $"+ released.toPlainString());
        }
    }

    @Override
    public boolean pay(Rider rider,BigDecimal amount, PaymentMethodType method) {
        requireValid(rider, amount, method);

        //a decline returns false
        //TripService decides what to do after
        if (declinePayments) {
            System.out.println("[PAYMENT] Declined $"+amount.toPlainString()+" via " +method);
            return false;
        }
        System.out.println("[PAYMENT] Charged $" + amount.toPlainString()+ " via "+method);
        return true;
    }

    /**
     * @param holdId the hold id to look up
     * @return true if that hold is still in place
     */
    public boolean isHoldActive(String holdId){
        return activeHolds.containsKey(holdId);
    }

    /**
     *checks arguments shared by {@link #placeHold} and {@link #pay}.
     *
     * @param rider cant not be null
     * @param amount cant not be null/-
     * @param method payment method cant be null
     * @throws IllegalArgumentException if the rider or method is null or the amount is null/-
     */
    private static void requireValid(Rider rider,BigDecimal amount,PaymentMethodType method){
        if (rider==null) {
            throw new IllegalArgumentException("rider must not be null");
        }
        if (method ==null){
            throw new IllegalArgumentException("payment method must not be null");
        }
        if (amount==null || amount.signum()<0) {
            throw new IllegalArgumentException("amount must not be null or negative");
        }
    }
}