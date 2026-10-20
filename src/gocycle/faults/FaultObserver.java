package gocycle.faults;

public interface FaultObserver {
    void onFault(FaultReport report);
}
