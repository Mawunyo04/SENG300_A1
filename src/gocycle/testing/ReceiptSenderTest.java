package gocycle.testing;
import gocycle.payments.AppReceiptSender;
import gocycle.payments.EmailReceiptSender;
import gocycle.trips.Rider;
import gocycle.trips.RiderRegistrationService;
import gocycle.trips.Trip;
import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReceiptSenderTest {
    private AppReceiptSender appSender = new AppReceiptSender();
    private EmailReceiptSender emailSender = new EmailReceiptSender();
    private Trip tripWithFare() {
        Rider rider =new RiderRegistrationService().registerCasual("Ramsha","ramsha@idk.com");
        Trip trip=new Trip(rider);
        trip.setFare(new BigDecimal("7.50"));
        return trip;
    }

    //trip that has not been priced yet, so it has no fare
    private Trip tripWithoutFare() {
        Rider rider = new RiderRegistrationService().registerCasual("Ramsha", "ramsha@example.com");
        return new Trip(rider);
    }
    //trip with a fare but rider has no email address
    private Trip tripWithRiderWithoutEmail() {
        Rider rider=new RiderRegistrationService().registerCasual("Ramsha", "");
        Trip trip=new Trip(rider);
        trip.setFare(new BigDecimal("7.50"));
        return trip;
    }
    @Test
    public void testAppSenderSendsReceipt() {
        Trip trip=tripWithFare();
        appSender.send(trip);
        assertNotNull(trip.getFare());
    }
    @Test
    public void testEmailSenderSendsReceipt() {
        Trip trip = tripWithFare();
        emailSender.send(trip);
        assertEquals("ramsha@idk.com", trip.getRider().getEmail());
    }

    //there is no trip to make a receipt for
    @Test(expected=IllegalArgumentException.class)
    public void testAppSenderRejectsNullTrip() {
        appSender.send(null);
    }
    @Test(expected=IllegalStateException.class)
    public void testSenderRejectsTripWithoutFare() {
        appSender.send(tripWithoutFare());
    }
    //if nowhere to send the email, so it fails with a clear error */
    @Test(expected =IllegalStateException.class)
    public void testEmailSenderRejectsRiderWithoutEmail() {
        emailSender.send(tripWithRiderWithoutEmail());
    }
}