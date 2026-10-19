package gocycle.testing;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.math.*;
import gocycle.pricing.*;

public class PricingTest {

    /* PricingStrategy fields for testing purposes */
    private PricingStrategy casualPricing   = new CasualPricing();
    private PricingStrategy memberPricing   = new MemberPricing();
    private PricingStrategy studentPricing  = new StudentPricing();


    /* Edge Case: Rider has zero minutes ridden, just unlocked the vehicle */
    @Test 
    public void testUnlock() {

        /* Only the unlock fee should be due */
        BigDecimal expected = new BigDecimal("1.50");
        BigDecimal actual = casualPricing.calculateFare(0);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    /* Case: Rider rides for one minute, smallest positive whole-min duration */
    @Test
    public void testOneMin() {

        int mins = 1;  /* Duration of rider's ride */

        /* Populate lists of expected fares */
        List<BigDecimal> expectedFares = Arrays.asList(

            new BigDecimal("1.90"),     /* Casual */
            new BigDecimal("1.70"),     /* Member */
            new BigDecimal("1.65")      /* Student */
        );

        /* Populate lists of actual, calculated fares */
        List<BigDecimal> actualFares = Arrays.asList(

            casualPricing.calculateFare(mins),     /* Casual */
            memberPricing.calculateFare(mins),     /* Member */
            studentPricing.calculateFare(mins)     /* Student */
        );

        /* 
        * For each rider type: 
        *   1) check if calculateFare() even returns a value
        *   2) check if the calculated fare is correct
        */
        for (int i = 0; i < expectedFares.size(); i++) {

            assertNotNull(actualFares.get(i));
            assertEquals(expectedFares.get(i), actualFares.get(i));
        }
    }
    
    /* Case: Rider rides for 10 minutes, example of a standard case */
    @Test
    public void testTenMin() {

        int mins = 10;

        /* Populate lists for testing */
        List<BigDecimal> expectedFares = Arrays.asList(

            new BigDecimal("5.50"),     /* Casual */
            new BigDecimal("3.50"),     /* Member */
            new BigDecimal("3.00")      /* Student */
        );

        List<BigDecimal> actualFares = Arrays.asList(

            casualPricing.calculateFare(mins),     /* Casual */
            memberPricing.calculateFare(mins),     /* Member */
            studentPricing.calculateFare(mins)     /* Student */
        );

        /* Test via assertions */
        for (int i = 0; i < expectedFares.size(); i++) {

            assertNotNull(actualFares.get(i));
            assertEquals(expectedFares.get(i), actualFares.get(i));
        }
    }

    /* Case: Rider's trip lasts just below and at the surcharge threshrold. 
    *        No surcharge fee to be applied in such cases.
    */
    @Test
    public void testNoSurcharge() {

        int belowThreshold = 119;
        int atThreshold    = 120;

        /* Populate lists for testing */
        List<BigDecimal> expectedFares = Arrays.asList(

            /* Fares for 119 minute rides */
            new BigDecimal("49.10"),      /* Casual */
            new BigDecimal("25.30"),      /* Member */
            new BigDecimal("19.35"),      /* Student */

            /* Fares for 120 minute rides */
            new BigDecimal("49.50"),      /* Casual */
            new BigDecimal("25.50"),      /* Member */
            new BigDecimal("19.50")       /* Student */
        );

        List<BigDecimal> actualFares = Arrays.asList(

            casualPricing.calculateFare(belowThreshold),     /* Casual */
            memberPricing.calculateFare(belowThreshold),     /* Member */
            studentPricing.calculateFare(belowThreshold),    /* Student */

            casualPricing.calculateFare(atThreshold),        /* Casual */
            memberPricing.calculateFare(atThreshold),        /* Member */
            studentPricing.calculateFare(atThreshold)        /* Student */
        );

        /* Test via assertions */
        for (int i = 0; i < expectedFares.size(); i++) {

            assertNotNull(actualFares.get(i));
            assertEquals(expectedFares.get(i), actualFares.get(i));
        }
    }

    /* Case: Rider's trip lasts over 120 minutes, resulting in applied surcharge fee.
    *        Surcharge should only be applied once in such cases.
    */
    @Test 
    public void testSurchargeApplied() {

        int justAbove       = 121;
        int veryLongTrip    = 300;

        /* Populate lists for testing */
        List<BigDecimal> expectedFares = Arrays.asList(

            /* Fares for 121 minute rides */
            new BigDecimal("54.90"),      /* Casual */
            new BigDecimal("30.70"),      /* Member */
            new BigDecimal("24.65"),      /* Student */

            /* Fares for 300 minute rides */
            new BigDecimal("126.50"),     /* Casual */
            new BigDecimal("66.50"),      /* Member */
            new BigDecimal("51.50")       /* Student */
        );

        List<BigDecimal> actualFares = Arrays.asList(

            casualPricing.calculateFare(justAbove),           /* Casual */
            memberPricing.calculateFare(justAbove),           /* Member */
            studentPricing.calculateFare(justAbove),          /* Student */
            
            casualPricing.calculateFare(veryLongTrip),        /* Casual */
            memberPricing.calculateFare(veryLongTrip),        /* Member */
            studentPricing.calculateFare(veryLongTrip)        /* Student */
        );

        /* Test via assertions */
        for (int i = 0; i < expectedFares.size(); i++) {

            assertNotNull(actualFares.get(i));
            assertEquals(expectedFares.get(i), actualFares.get(i));
        }
    }
}