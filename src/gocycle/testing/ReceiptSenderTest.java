package gocycle.testing;
import gocycle.payments.AppReceiptSender;
import gocycle.payments.EmailReceiptSender;
import gocycle.trips.Rider;
import gocycle.trips.Trip;
import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReceiptSenderTest {
    private AppReceiptSender appSender=new AppReceiptSender();
    private EmailReceiptSender emailSender =new EmailReceiptSender();
    private Trip tripWithFare() {
        Rider rider = new Rider("R1", "Ramsha", "ramsha@idk.com");
        return new Trip("T1",rider,"HOLD-1",new BigDecimal("7.50"));
    }
    private Trip tripWithoutFare() {
        Rider rider = new Rider("R1", "Ramsha", "ramsha@idk.com");
        return new Trip("T2", rider,"HOLD-2",null);
    }

    private Trip tripWithRiderWithoutEmail() {
        Rider rider = new Rider("R2", "Sam", "");
        return new Trip("T3",rider, "HOLD-3", new BigDecimal("7.50"));
    }
    @Test
    public void testAppSenderSendsReceipt() {
        Trip trip = tripWithFare();
        appSender.send(trip);
        assertNotNull(trip.getFare());
    }
    @Test
    public void testEmailSenderSendsReceipt() {
        Trip trip = tripWithFare();
        emailSender.send(trip);
        assertEquals("ramsha@idk.com", trip.getRider().getEmail());
    }
    @Test(expected=IllegalArgumentException.class)
    public void testAppSenderRejectsNullTrip() {
        appSender.send(null);
    }
    @Test(expected=IllegalStateException.class)
    public void testSenderRejectsTripWithoutFare() {
        appSender.send(tripWithoutFare());
    }

    @Test(expected =IllegalStateException.class)
    public void testEmailSenderRejectsRiderWithoutEmail() {
        emailSender.send(tripWithRiderWithoutEmail());
    }
}