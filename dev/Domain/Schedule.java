package Domain;

import java.time.LocalDate;
import java.util.List;

public class Schedule {
    private LocalDate date;
    private List<Shift> shifts;

    public Schedule(LocalDate date, List<Shift> shifts) {
        this.date = date;
        this.shifts = shifts;
    }

    public LocalDate getDate() {
        return date;
    }


    public List<Shift> getShifts() {
        return shifts;
    }

}
