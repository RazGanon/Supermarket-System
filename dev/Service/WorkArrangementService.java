package Service;

import Domain.Shift;
import Domain.ShiftPair;
import Domain.ShiftType;
import Domain.WorkArangment;

import java.time.LocalDate;

public class WorkArrangementService {
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
