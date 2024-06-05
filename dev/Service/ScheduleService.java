package Service;

import Domain.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class ScheduleService {
    private Schedule schedule;

    public ScheduleService(String store, LocalDate first_day, LocalTime morning_start, LocalTime morning_end, LocalTime evening_start, LocalTime evening_end, SuperMarket branch) {
        this.schedule = new Schedule(store, first_day, morning_start, morning_end, evening_start, evening_end, branch);
        initialize_week_shifts(first_day, branch);
    }


        public static String getCurrentSchedule(){
            StringBuilder scheduleString = new StringBuilder();
            scheduleString.append("Schedule for store: ").append(Schedule.getStore()).append("\n");
            for (Map.Entry<ShiftPair, Shift> entry : Schedule.getShifts().entrySet()) {
                scheduleString.append(entry.getKey()).append(" => ").append(entry.getValue()).append("\n");
            }
            return scheduleString.toString();
        }


    public ShiftPair check_shift(LocalDate shift_date, ShiftType shift_type) {
        ShiftPair searchPair = new ShiftPair(shift_date, shift_type);
        for (ShiftPair shift_pair : schedule.getShifts().keySet()) {
            if (shift_pair.equals(searchPair)) {
                return shift_pair;
            }
        }
        return null;
    }

    private void initialize_week_shifts(LocalDate week_start_date, SuperMarket branch) {
        DayShift[] dayShifts = DayShift.values();
        int daysInWeek = dayShifts.length;
        for (int i = 0; i < daysInWeek; i++) {
            LocalDate currentDate = week_start_date.plusDays(i);
            DayShift currentDayShift = dayShifts[i % 7];
            schedule.getShifts().put(new ShiftPair(currentDate, ShiftType.MORNING), new Shift(branch, ShiftType.MORNING, currentDayShift));
            schedule.getShifts().put(new ShiftPair(currentDate, ShiftType.EVENING), new Shift(branch, ShiftType.EVENING, currentDayShift));
        }
    }

    public String addEmpAvailability(String employee_id, LocalDate shift_date, ShiftType shift_type) {
        ShiftPair shiftPair = check_shift(shift_date, shift_type); // check if the shift exists
        if (shiftPair != null) {
            Shift shift = schedule.getShifts().get(shiftPair);
            shift.addEmployeeAvailability(employee_id); //adding employ availability
            return "Employee availability added";
        }
        return "Shift doesn't exist";
    }

    public void printSchedule() {
        System.out.println("Schedule for store: " + schedule.getStore());
        for (Map.Entry<ShiftPair, Shift> entry : schedule.getShifts().entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
    }
}
