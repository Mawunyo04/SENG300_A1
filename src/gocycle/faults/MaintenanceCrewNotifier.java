package gocycle.faults;

import java.util.Objects;
import java.util.function.Consumer;

public class MaintenanceCrewNotifier implements FaultObserver {
    private final Consumer<String> output;

    public MaintenanceCrewNotifier() {
        this(System.out::println);
    }

    public MaintenanceCrewNotifier(Consumer<String> output) {
        this.output = Objects.requireNonNull(output, "output");
    }

    @Override
    public void onFault(FaultReport report) {
        Objects.requireNonNull(report, "report");
        output.accept("Maintenance alert: vehicle=" + report.getVehicle()
                + ", time=" + report.getReportedAt() + ", " + report);
    }
}
