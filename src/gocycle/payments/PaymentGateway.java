package gocycle.payments;
import gocycle.trips.Rider;
import java.math.BigDecimal;
public interface PaymentGateway {

    //places a refundable deposit hold when a trip starts.
    String placeHold(Rider rider, BigDecimal amount,PaymentMethodType method);

    //stops a hold when the trip ends or fails to start.
    void releaseHold(String holdId);

    //charges final fare
    boolean pay(Rider rider, BigDecimal amount, PaymentMethodType method);
}
