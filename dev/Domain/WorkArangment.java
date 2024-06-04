package Domain;

import java.util.HashMap;
import java.util.Map;

public class WorkArangment {
    private Map<String, DaySchedule> dailySchedules; //day, dayschedule

    // Constructor
    public void WorkArangment() {
        this.dailySchedules = new HashMap<>();
    }

    public void WorkArangment(Map<String, DaySchedule> dailySchedules) {
        this.dailySchedules = dailySchedules;
    }

    // Method to add a DaySchedule for a specific day
    public void addDaySchedule(String day, DaySchedule schedule) {
        dailySchedules.put(day, schedule);
    }

    // Method to get the DaySchedule for a specific day
    public DaySchedule getDaySchedule(String day) {
        return dailySchedules.get(day);
    }

    // Method to iterate through all DaySchedules (optional)
    public void iterateThroughSchedules() {
        for (Map.Entry<String, DaySchedule> entry : dailySchedules.entrySet()) {
            System.out.println("Day: " + entry.getKey());
            // Access manager and employees using getManager() and getEmployees() from DaySchedule
        }
    }
}
