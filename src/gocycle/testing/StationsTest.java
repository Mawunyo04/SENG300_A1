package gocycle.testing;
 
import static org.junit.jupiter.api.Assertions.*;
import gocycle.payments.PaymentMethodType;
import gocycle.stations.*;
import gocycle.vehicles.PedalBike;
import gocycle.vehicles.Vehicle;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
 

public class StationsTest {
 
    private final PrintStream realOut = System.out;
    private ByteArrayOutputStream out;
    private Zone zone;
    private Station s1, s2, s3; // s2 is ~0.44 km from s1, s3 is ~2.2 km from s1
    private StationNetwork network;
    private StationScreen screen; // the screen of s1
 
    @BeforeEach
    void setUp() { /* set up objects for testing */
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        zone = new Zone("North", "north.png", "#0055AA");
        zone.addAllowedPayment(PaymentMethodType.CREDIT_CARD);
        s1 = new Station("S1", 51.0447, -114.0719, zone, 2);
        s2 = new Station("S2", 51.0486, -114.0708, zone, 1);
        s3 = new Station("S3", 51.0600, -114.0900, zone, 1);
        network = new StationNetwork();
        network.addStation(s1);
        network.addStation(s2);
        network.addStation(s3);
        screen = s1.getScreen();
    }
 
    @AfterEach
    void tearDown() {
        System.setOut(realOut);
    }
 
    private static Vehicle bike(String id) {
        return new PedalBike(id); 
    }
 
    private void fill(Station station) {
        while (!station.isFull()) {
            station.findFreePoint().dock(bike("B"));
        }
    }
 
    private String printed() {
        String text = out.toString();
        out.reset();
        return text;
    }
 
    /* tests for docking point */

    @Test
    void cannotDockNullVehicle() {
    DockingPoint p = new DockingPoint("P1");
    assertFalse(p.dock(null));
    }

    @Test 
    void dockingNullLeavesPointFree(){
    DockingPoint p = new DockingPoint("P1");
    p.dock(null);
    assertTrue(p.isFree());    
    }
 
    @Test
    void newDockingPointIsFree() {
        assertTrue(new DockingPoint("P1").isFree());
    }
 
    @Test
    void canDockAndLockVehicle() {
        assertTrue(new DockingPoint("P1").dock(bike("B1")));
        DockingPoint p = new DockingPoint("P1");
        p.dock(bike("B1"));
        assertTrue(p.isLocked());
    }
 
    @Test
    void occupiedDockingPointRefusesASecondVehicle() {
        DockingPoint p = new DockingPoint("P1");
        p.dock(bike("B1"));
        assertFalse(p.dock(bike("B2")));
    }
 
    @Test
    void releaseGivesBackTheVehicle() {
        DockingPoint p = new DockingPoint("P1");
        Vehicle b = bike("B1");
        p.dock(b);
        assertSame(b, p.releaseVehicle());
    }
 
    @Test
    void releaseUnlocksTheDockingPoint() {
        DockingPoint p = new DockingPoint("P1");
        p.dock(bike("B1"));
        p.releaseVehicle();
        assertFalse(p.isLocked());
    }
 
    /* tests for zone */
 
    @Test
    void zoneKeepsItsLogoAndColor() {
        assertEquals("north.png", zone.getLogoPath());
         assertEquals("#0055AA", zone.getPrimaryColour());
    }
 
 
    @Test
    void zoneAllowsAnAddedPaymentMethod() {
        assertTrue(zone.allows(PaymentMethodType.CREDIT_CARD));
    }
 
    @Test
    void zoneRefusesAPaymentMethodThatWasNotAdded() {
        assertFalse(zone.allows(PaymentMethodType.TRANSIT_CARD));
    }
 
    /* tests for station */
 
    @Test
    void stationNeedsAtLeastOneDockingPoint() {
        assertThrows(IllegalArgumentException.class, () -> new Station("X", 0, 0, zone, 0));
    }
 
    @Test
    void emptyStationIsNotFull() {
        assertFalse(s1.isFull());
    }
 
    @Test
    void stationWithEveryPointOccupiedIsFull() {
        fill(s1);
        assertTrue(s1.isFull());
    }
 
    @Test
    void fullStationHasNoFreePoint() {
        fill(s1);
        assertNull(s1.findFreePoint());
    }
 
    @Test
    void distanceBetweenStationsIsInKilometres() {
        assertEquals(0.44, s1.distanceTo(s2), 0.04);
    }
 
    /* tests for station network */
 
    @Test
    void nearestStationWithSpaceIsFound() {
        assertSame(s2, network.findNearestWithFreePoint(s1));
    }
 
    @Test
    void fullStationIsSkippedWhenFindingTheNearest() {
        fill(s2);
        assertSame(s3, network.findNearestWithFreePoint(s1));
    }
 
    @Test
    void noStationIsFoundWhenAllOthersAreFull() {
        fill(s2);
        fill(s3);
        assertNull(network.findNearestWithFreePoint(s1));
    }
 
    @Test
    void networkStatePrintsEveryStation() {
        printed();
        network.printNetworkState();
        String text = printed();
        assertTrue(text.contains("S1") && text.contains("S2") && text.contains("S3"));
    }
 
    /* tests for station screen */
 
    @Test
    void screenShowsTheMessage() {
        screen.show("Welcome to GoCycle");
        assertTrue(printed().contains("Welcome to GoCycle"));
    }
 
    @Test
    void screenShowsTheZoneLogoAndColor() {
        screen.show("Welcome to GoCycle");
        assertTrue(printed().contains("north.png"));
        assertTrue(printed().contains("#0055AA"));
    }
 
    @Test
    void screenReadsAMessageAloud() {
        screen.readAloud("Welcome to GoCycle");
        assertTrue(printed().contains("out loud"));
    }
 
    @Test
    void screenRedirectsTheRiderToAnotherStation() {
        screen.showRedirect(s2);
        assertTrue(printed().contains("Please go to station S2"));
    }
 
    @Test
    void screenTellsTheRiderWhenNoStationHasSpace() {
        screen.showRedirect(null);
        assertTrue(printed().contains("No free station is available nearby"));
    }
 
    @Test
    void screenListsAnAllowedPaymentMethod() {
        screen.showPaymentOptions();
        assertTrue(printed().contains("Credit card"));
    }
 
    @Test
    void screenDoesNotListAPaymentMethodThatIsNotAllowed() {
        screen.showPaymentOptions();
        assertFalse(printed().contains("Transit card"));
    }
 
    @Test
    void screenStartsInEnglishAndCanSwitchToFrench() {
        assertEquals("en", screen.getLanguage());
        screen.setLanguage("fr");
        assertEquals("fr", screen.getLanguage());
    }
 
    @Test
    void screenShowsFrenchTextAfterSwitchingToFrench() {
        screen.setLanguage("fr");
        screen.show("Welcome to GoCycle");
        assertTrue(printed().contains("Bienvenue chez GoCycle"));
    }
 
    @Test
    void screenRefusesAnUnsupportedLanguage() {
        screen.setLanguage("es");
        assertEquals("en", screen.getLanguage());
    }
}
 

