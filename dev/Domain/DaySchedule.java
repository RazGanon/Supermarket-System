package Domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Schedule {
    private String store;
    private  Map<ShiftPair, Shift> shifts;


    public Schedule (String store, LocalDate first_day, LocalTime morning_start, LocalTime morning_end, LocalTime evening_start, LocalTime evening_end) {
        this.store = store;
        shifts = new HashMap<>();
        initialize_week_shifts(first_day, morning_start, morning_end, evening_start, evening_end);
    }
    public ShiftPair get_shift(String emp_id , LocalDate shift_date,ShiftType shift_type){


    }


    private void initialize_week_shifts(LocalDate week_start_date,SuperMarket branch  ) {
        for (int i = 0; i < 7; i++) {
            shifts.put(new ShiftPair(week_start_date, ShiftType.MORNING), new Shift(branch, ShiftType.MORNING,DayShift.SUNDAY));
            shifts.put(new ShiftPair(week_start_date, ShiftType.EVENING), new Shift(branch,  ShiftType.EVENING, DayShift.SUNDAY));
            week_start_date = week_start_date.plusDays(1);
            shifts.put(new ShiftPair(week_start_date, ShiftType.MORNING), new Shift(branch, ShiftType.MORNING,DayShift.MONDAY));
            shifts.put(new ShiftPair(week_start_date, ShiftType.EVENING), new Shift(branch,  ShiftType.EVENING, DayShift.MONDAY));
            week_start_date = week_start_date.plusDays(1);
            shifts.put(new ShiftPair(week_start_date, ShiftType.MORNING), new Shift(branch, ShiftType.MORNING,DayShift.TUESDAY));
            shifts.put(new ShiftPair(week_start_date, ShiftType.EVENING), new Shift(branch,  ShiftType.EVENING, DayShift.TUESDAY));
            week_start_date = week_start_date.plusDays(1);
            shifts.put(new ShiftPair(week_start_date, ShiftType.MORNING), new Shift(branch, ShiftType.MORNING,DayShift.WEDNESDAY));
            shifts.put(new ShiftPair(week_start_date, ShiftType.EVENING), new Shift(branch,  ShiftType.EVENING, DayShift.WEDNESDAY));
            week_start_date = week_start_date.plusDays(1);
            shifts.put(new ShiftPair(week_start_date, ShiftType.MORNING), new Shift(branch, ShiftType.MORNING,DayShift.THURSDAY));
            shifts.put(new ShiftPair(week_start_date, ShiftType.EVENING), new Shift(branch,  ShiftType.EVENING, DayShift.THURSDAY));
            week_start_date = week_start_date.plusDays(1);
            shifts.put(new ShiftPair(week_start_date, ShiftType.MORNING), new Shift(branch, ShiftType.MORNING,DayShift.FRIDAY));
            shifts.put(new ShiftPair(week_start_date, ShiftType.EVENING), new Shift(branch,  ShiftType.EVENING, DayShift.FRIDAY));
            week_start_date = week_start_date.plusDays(1);

        }

        }


    private void addEmpAvailability(Node newNode) { //with this functoin the employee send his availability
    //to the system
        ShiftPair shiftPair = get_shift(shift_date, shift_type);

    }



}