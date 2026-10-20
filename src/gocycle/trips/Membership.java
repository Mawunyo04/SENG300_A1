package gocycle.trips;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Membership {
    String plan;
    BigDecimal price;
    LocalDate startDate;
    LocalDate endDate;

    public Membership(String plan, BigDecimal price, LocalDate startDate, LocalDate endDate) {
        this.plan = plan;
        this.price = price;
        this.startDate = LocalDate.now();
    }

    public boolean isActive(){
        return (endDate == null);
    }

    //TODO: add end() method and put in UML.
}
