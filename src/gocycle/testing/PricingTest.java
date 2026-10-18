package gocycle.testing;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import gocycle.pricing.*;

public class PricingTest {

    private PricingStrategy pricing = new CasualPricing();

    /* Edge Case: Rider has zero minutes ridden, just unlocked the vehicle */
    @Test 
    public void testZeroMins() {

        /* Only the unlock fee should be due */
        BigDecimal expected = new BigDecimal("1.50");

        BigDecimal actual = pricing.calculateFare(0);

        assertNotNull(actual);
        assertEquals(expected, actual);



    }












}