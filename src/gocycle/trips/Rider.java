package gocycle.trips;

import gocycle.pricing.CasualPricing;
import gocycle.pricing.PricingStrategy;

public class Rider {

    private String id;
    private String name;
    private String email;
    private PricingStrategy pricing;
    private Membership membership;

    public Rider(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.pricing = new CasualPricing(); // as a default before it gets
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PricingStrategy getPricing() {
        return pricing;
    }

    public void setPricing(PricingStrategy pricing) {
        this.pricing = pricing;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }
}
