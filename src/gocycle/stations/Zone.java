package gocycle.stations;

import gocycle.payments.PaymentMethodType;
import java.util.HashSet;
import java.util.Set;


public class Zone {

    private String name;
    private String logoPath;
    private String primaryColour;
    private Set<PaymentMethodType> allowedPayments;

    public Zone(String name, String logoPath, String primaryColour) {
        this.name = name;
        this.logoPath = logoPath;
        this.primaryColour = primaryColour;
        this.allowedPayments = new HashSet<PaymentMethodType>(); 
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getPrimaryColour() {
        return primaryColour;
    }
    //save new color
    public void setPrimaryColour(String primaryColour) {
        this.primaryColour = primaryColour;
    }

    
    public void addAllowedPayment(PaymentMethodType method) {
        allowedPayments.add(method);
    }

   
    public void removeAllowedPayment(PaymentMethodType method) {
        allowedPayments.remove(method);
    }

    
    public boolean allows(PaymentMethodType method) {
        return allowedPayments.contains(method);
    }
}