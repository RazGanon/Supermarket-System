package Domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Schedule {
    private static String store;
    private static Map<ShiftPair, Shift> shifts;

    public Schedule(String store, LocalDate first_day, LocalTime morning_start, LocalTime morning_end, LocalTime evening_start, LocalTime evening_end, SuperMarket branch) {
        this.store = store;
        this.shifts = new HashMap<>();
    }

    public static String getStore() {
        return store;
    }

    public static Map<ShiftPair, Shift> getShifts() {
        return shifts;
    }
}
