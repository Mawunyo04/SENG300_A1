package gocycle.testing;
 
import static org.junit.Assert.*;
import gocycle.payments.PaymentMethodType;
import gocycle.stations.*;
import gocycle.vehicles.PedalBike;
import gocycle.vehicles.Vehicle;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
 

public class StationsTest {
 
    private final PrintStream realOut = System.out;
    private ByteArrayOutputStream out;
    private Zone zone;
    private Station s1, s2, s3; // s2 is ~0.44 km from s1, s3 is ~2.2 km from s1
    private StationNetwork network;
    private StationScreen screen; // the screen of s1
 
    @Before
    public void setUp() { /* set up objects for testing */
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        zone = new Zone("North", "north.png", "#33aa00");
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
 
    @After
    public void tearDown() {
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
    public void cannotDockNullVehicle() {
    DockingPoint p = new DockingPoint("P1");
    assertFalse(p.dock(null));
    }

    @Test 
    public void dockingNullLeavesPointFree(){
    DockingPoint p = new DockingPoint("P1");
    p.dock(null);
    assertTrue(p.isFree());    
    }
 
    @Test
    public void newDockingPointIsFree() {
        assertTrue(new DockingPoint("P1").isFree());
    }
 
    @Test
    public void canDockAndLockVehicle() {
        assertTrue(new DockingPoint("P1").dock(bike("B1")));
        DockingPoint p = new DockingPoint("P1");
        p.dock(bike("B1"));
        assertTrue(p.isLocked());
    }
 
    @Test
    public void occupiedDockingPointRefusesASecondVehicle() {
        DockingPoint p = new DockingPoint("P1");
        p.dock(bike("B1"));
        assertFalse(p.dock(bike("B2")));
    }
 
    @Test
    public void releaseGivesBackTheVehicle() {
        DockingPoint p = new DockingPoint("P1");
        Vehicle b = bike("B1");
        p.dock(b);
        assertSame(b, p.releaseVehicle());
    }
 
    @Test
    public void releaseUnlocksTheDockingPoint() {
        DockingPoint p = new DockingPoint("P1");
        p.dock(bike("B1"));
        p.releaseVehicle();
        assertFalse(p.isLocked());
    }
 
    /* tests for zone */
 
    @Test
    public void zoneKeepsItsLogoAndColor() {
        assertEquals("north.png", zone.getLogoPath());
         assertEquals("#33aa00", zone.getPrimaryColour());
    }
 
 
    @Test
    public void zoneAllowsAnAddedPaymentMethod() {
        assertTrue(zone.allows(PaymentMethodType.CREDIT_CARD));
    }
 
    @Test
    public void zoneRefusesAPaymentMethodThatWasNotAdded() {
        assertFalse(zone.allows(PaymentMethodType.TRANSIT_CARD));
    }
 
    /* tests for station */
 
    @Test
    public void stationNeedsAtLeastOneDockingPoint() {
        assertThrows(IllegalArgumentException.class, () -> new Station("X", 0, 0, zone, 0));
    }
 
    @Test
    public void emptyStationIsNotFull() {
        assertFalse(s1.isFull());
    }
 
    @Test
    public void stationWithEveryPointOccupiedIsFull() {
        fill(s1);
        assertTrue(s1.isFull());
    }
 
    @Test
    public void fullStationHasNoFreePoint() {
        fill(s1);
        assertNull(s1.findFreePoint());
    }
 
    @Test
    public void distanceBetweenStationsIsInKilometres() {
        assertEquals(0.44, s1.distanceTo(s2), 0.04);
    }
 
    /* tests for station network */
 
    @Test
    public void nearestStationWithSpaceIsFound() {
        assertSame(s2, network.findNearestWithFreePoint(s1));
    }
 
    @Test
    public void fullStationIsSkippedWhenFindingTheNearest() {
        fill(s2);
        assertSame(s3, network.findNearestWithFreePoint(s1));
    }
 
    @Test
    public void noStationIsFoundWhenAllOthersAreFull() {
        fill(s2);
        fill(s3);
        assertNull(network.findNearestWithFreePoint(s1));
    }
 
    @Test
    public void networkStatePrintsEveryStation() {
        printed();
        network.printNetworkState();
        String text = printed();
        assertTrue(text.contains("S1") && text.contains("S2") && text.contains("S3"));
    }
 
    /* tests for station screen */
 
    @Test
    public void screenShowsTheMessage() {
        screen.show("Welcome to GoCycle");
        assertTrue(printed().contains("Welcome to GoCycle"));
    }
 
    @Test
    public void screenShowsTheZoneLogo() {
        screen.show("Welcome to GoCycle");
        String text = printed();
        assertTrue(text.contains("north.png"));
        
    }
    @Test 
    public void screenShowsTheZoneColor(){
        screen.show("Welcome to GoCycle");
        assertTrue(printed().contains("#33aa00"));
    }
 
    @Test
    public void screenReadsAMessageAloud() {
        screen.readAloud("Welcome to GoCycle");
        assertTrue(printed().contains("out loud"));
    }
 
    @Test
    public void screenRedirectsTheRiderToAnotherStation() {
        screen.showRedirect(s2);
        assertTrue(printed().contains("Please go to station S2"));
    }
 
    @Test
    public void screenTellsTheRiderWhenNoStationHasSpace() {
        screen.showRedirect(null);
        assertTrue(printed().contains("No free station is available nearby"));
    }
 
    @Test
    public void screenListsAnAllowedPaymentMethod() {
        screen.showPaymentOptions();
        assertTrue(printed().contains("Credit card"));
    }
 
    @Test
    public void screenDoesNotListAPaymentMethodThatIsNotAllowed() {
        screen.showPaymentOptions();
        assertFalse(printed().contains("Transit card"));
    }
 
    @Test
    public void screenStartsInEnglishAndCanSwitchToFrench() {
        assertEquals("en", screen.getLanguage());
        screen.setLanguage("fr");
        assertEquals("fr", screen.getLanguage());
    }
 
    @Test
    public void screenShowsFrenchTextAfterSwitchingToFrench() {
        screen.setLanguage("fr");
        screen.show("Welcome to GoCycle");
        assertTrue(printed().contains("Bienvenue chez GoCycle"));
    }
 
    @Test
    public void screenRefusesAnUnsupportedLanguage() {
        screen.setLanguage("es");
        assertEquals("en", screen.getLanguage());
    }
}
 

