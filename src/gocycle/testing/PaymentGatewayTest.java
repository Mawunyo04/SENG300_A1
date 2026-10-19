package gocycle.testing;

import gocycle.payments.PaymentMethodType;
import gocycle.payments.SimulatedPaymentGateway;
import gocycle.trips.Rider;
import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PaymentGatewayTest {

    private SimulatedPaymentGateway gateway=new SimulatedPaymentGateway();
    private Rider rider = makeRider();
    private Rider makeRider() {
        return new Rider("R1", "Ramsha", "ramsha@example.com");
    }

    @Test
    public void testPlaceHoldReturnsIdAndRecordsHold() {
        String holdId = gateway.placeHold(rider, new BigDecimal("20.00"), PaymentMethodType.CREDIT_CARD);
        assertNotNull(holdId);
        assertTrue(gateway.isHoldActive(holdId));
    }

    @Test
    public void testReleaseHoldRemovesTheHold() {
        String holdId = gateway.placeHold(rider, new BigDecimal("20.00"), PaymentMethodType.TRANSIT_CARD);
        gateway.releaseHold(holdId);
        assertFalse(gateway.isHoldActive(holdId));
    }

    @Test
    public void testReleasingTheSameHoldTwiceIsHarmless() {
        String holdId = gateway.placeHold(rider, new BigDecimal("20.00"), PaymentMethodType.PHONE_WALLET);
        gateway.releaseHold(holdId);
        gateway.releaseHold(holdId);
        assertFalse(gateway.isHoldActive(holdId));
    }

    @Test
    public void testAllThreePaymentMethodsWork() {
        BigDecimal fare = new BigDecimal("7.50");
        assertTrue(gateway.pay(rider,fare ,PaymentMethodType.CREDIT_CARD));
        assertTrue(gateway.pay(rider,fare,PaymentMethodType.TRANSIT_CARD));
        assertTrue(gateway.pay(rider,fare, PaymentMethodType.PHONE_WALLET));
    }

    @Test
    public void testDecliningGatewayReturnsFalse() {
        SimulatedPaymentGateway declining = new SimulatedPaymentGateway(true);
        boolean paid = declining.pay(rider, new BigDecimal("7.50"), PaymentMethodType.PHONE_WALLET);
        assertFalse(paid);
    }
}