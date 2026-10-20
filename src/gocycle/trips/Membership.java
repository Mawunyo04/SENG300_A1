package gocycle.trips;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Membership {
    private String plan;
    private BigDecimal price;
    private LocalDate startDate;
    private LocalDate endDate;

    public Membership(String plan, BigDecimal price, LocalDate startDate, LocalDate endDate) {
        this.plan = plan;
        this.price = price;
        this.startDate = (startDate != null) ? startDate : LocalDate.now();
        this.endDate = endDate;
    }

    public boolean isActive() {
        return (endDate == null);
    }

    public void end() {
        this.endDate = LocalDate.now();
    }

    public String getPlan() {
        return plan;
    }

    public BigDecimal getMonthlyFee() {
        return price;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
