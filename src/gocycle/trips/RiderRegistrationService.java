package gocycle.trips;

import gocycle.pricing.CasualPricing;
import gocycle.pricing.MemberPricing;
import gocycle.pricing.StudentPricing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class RiderRegistrationService {

    public Rider registerCasual(String name, String email) {
        String id = "RIDER-" + UUID.randomUUID().toString();
        Rider rider = new Rider(id, name, email);
        rider.setPricing(new CasualPricing());
        return rider;
    }

    public Rider registerMember(String name, String email, String plan) {
        String id = "RIDER-" + UUID.randomUUID().toString();
        Membership membership = new Membership(plan, new BigDecimal("20.00"), LocalDate.now(), null);
        Rider rider = new Rider(id, name, email);
        rider.setPricing(new MemberPricing());
        rider.setMembership(membership);
        return rider;
    }

    public Rider registerStudent(String name, String email) {
        String id = "RIDER-" + UUID.randomUUID().toString();
        Rider rider = new Rider(id, name, email);
        rider.setPricing(new StudentPricing());
        return rider;
    }
}
