package Domain;

import java.util.HashMap;
import java.util.Map;

public class WorkArangment {

    private final Map<ShiftPair, Shift> dailySchedules;

    public WorkArangment() {
        dailySchedules = new HashMap<>();
    }

    public void addDaySchedule(String date, String shift, DaySchedule daySchedule) {
        Map<String, DaySchedule> shiftSchedules = dailySchedules.get(date);
        if (shiftSchedules == null) {
            shiftSchedules = new HashMap<>();
            dailySchedules.put(date, shiftSchedules);
        }
        shiftSchedules.put(shift, daySchedule);
    }

    public DaySchedule getDaySchedule(String date, String shift) {
        Map<String, DaySchedule> shiftSchedules = dailySchedules.get(date);
        if (shiftSchedules != null) {
            return shiftSchedules.get(shift);
        }
        return null;
    }
}
