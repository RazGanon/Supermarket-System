package Domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Schedule {
    private String store;
    private Map<ShiftPair, Shift> shifts;

    public Schedule(String store, LocalDate first_day, LocalTime morning_start, LocalTime morning_end, LocalTime evening_start, LocalTime evening_end, SuperMarket branch) {
        this.store = store;
        shifts = new HashMap<>();
        initialize_week_shifts(first_day, branch);
    }

    public ShiftPair check_shift(LocalDate shift_date, ShiftType shift_type) {
        for (ShiftPair shift_pair : shifts.keySet()) {
            if (shift_pair.equals(shift_date, shift_type)) {
                return shift_pair;
            }
        }
        return null;
    }

    private void initialize_week_shifts(LocalDate week_start_date, SuperMarket branch) {
        DayShift[] dayShifts = DayShift.values();
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = week_start_date.plusDays(i);
            DayShift currentDayShift = dayShifts[i % 7];
            shifts.put(new ShiftPair(currentDate, ShiftType.MORNING), new Shift(branch, ShiftType.MORNING, currentDayShift));
            shifts.put(new ShiftPair(currentDate, ShiftType.EVENING), new Shift(branch, ShiftType.EVENING, currentDayShift));
        }
    }

    public String addEmpAvailability(String employee_id, LocalDate shift_date, ShiftType shift_type) {
        ShiftPair shiftPair = check_shift(shift_date, shift_type); // check if the shift exists
        if (shiftPair != null) {
            Shift shift = shifts.get(shiftPair);
            shift.addEmployeeAvailability(employee_id); //adding employ availability
            return "Employee availability added";
        }
        return "Shift doesn't exist";
    }
}
