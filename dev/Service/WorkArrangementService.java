package Service;

import Domain.Shift;
import Domain.ShiftPair;
import Domain.ShiftType;
import Domain.WorkArangment;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Map;

public class WorkArrangementService {
    private Map<DateFormat, WorkArangment> PastWorkSchedules; //date of the schedule , the work arrangement

    static WorkArangment Wa = new WorkArangment();
    public static void addDaySchedule(LocalDate date, ShiftType shiftType, Shift shift) {
        ShiftPair shiftPair = new ShiftPair(date, shiftType);
        Wa.GetWorkArrangement().put(shiftPair, shift);
    }

    public static Shift getDaySchedule(LocalDate date, ShiftType shiftType) {
        ShiftPair shiftPair = new ShiftPair(date, shiftType);
        return Wa.GetWorkArrangement().get(shiftPair);
    }
}
