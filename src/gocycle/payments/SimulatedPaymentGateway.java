package gocycle.payments;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SimulatedPaymentGateway implements PaymentGateway {
    private final Set<String> activeHolds =new HashSet<>();
    private final boolean failPayments;

    public SimulatedPaymentGateway(){
        this(false);
    }
    public SimulatedPaymentGateway(boolean failPayments) {
        this.failPayments =failPayments;
    }

    @Override
    public String placeHold(Rider rider,BigDecimal amount,PaymentMethodType method) {
        String holdId = "HOLD-" +UUID.randomUUID();
        activeHolds.add(holdId);
        System.out.println("[PAYMENT] Hold "+holdId + " of $" + amount + " via " + method);
        return holdId;
    }

    @Override
    public void releaseHold(String holdId) {
        if (activeHolds.remove(holdId)) {
            System.out.println("[PAYMENT] Released hold " +holdId);
        }
    }

    @Override
    public boolean pay(Rider rider, BigDecimal amount,PaymentMethodType method){
        if (failPayments)
            return false;
        System.out.println("[PAYMENT] Charged $"+amount + " via "+method);
        return true;
    }
}
