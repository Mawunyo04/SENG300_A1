package gocycle.testing;

import gocycle.payments.PaymentMethodType;
import gocycle.payments.SimulatedPaymentGateway;
import gocycle.trips.Rider;
import gocycle.trips.RiderRegistrationService;
import org.junit.Test;

import java.math.BigDecimal;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *tests for SimulatedPaymentGateway and PaymentGateway
 */
public class PaymentGatewayTest {
    private SimulatedPaymentGateway gateway =new SimulatedPaymentGateway() ;
    private Rider rider= makeRider();
    private Rider makeRider(){
        return new RiderRegistrationService().registerCasual("Ramsha","ramsha@idk.com");
    }

    //hold is placed when trip starts
    @Test
    public void testPlaceHoldReturnsIdAndRecordsHold() {
        String holdId=gateway.placeHold(rider, new BigDecimal("20.00"),PaymentMethodType.CREDIT_CARD);
        assertNotNull(holdId);
        assertTrue(gateway.isHoldActive(holdId));
    }

    //case where the hold is released when the trip ends
    @Test
    public void testReleaseHoldRemovesTheHold() {
        String holdId =gateway.placeHold(rider,new BigDecimal("20.00"),PaymentMethodType.TRANSIT_CARD);
        gateway.releaseHold(holdId);
        assertFalse(gateway.isHoldActive(holdId));
    }

    //releasing the same hold twice should be harmless
    @Test
    public void testReleasingTheSameHoldTwiceIsHarmless() {

        String holdId=gateway.placeHold(rider, new BigDecimal("20.00"), PaymentMethodType.PHONE_WALLET);
        gateway.releaseHold(holdId);
        gateway.releaseHold(holdId);
        assertFalse(gateway.isHoldActive(holdId));
    }

    //all three payment methods can hold and pay
    @Test
    public void testEveryPaymentMethodIsAccepted() {
        for (PaymentMethodType method:PaymentMethodType.values() ) {
            assertNotNull(gateway.placeHold(rider, new BigDecimal("20.00"), method));
            assertTrue(gateway.pay(rider,new BigDecimal("7.50"), method));
        }
    }

    //declined payment returns false instead of throwing error
    @Test
    public void testDecliningGatewayReturnsFalse() {
        SimulatedPaymentGateway declining=new SimulatedPaymentGateway(true);
        boolean paid =declining.pay(rider, new BigDecimal("7.50"), PaymentMethodType.PHONE_WALLET);
        assertFalse(paid);
    }
}