package Domain;

import java.time.LocalDate;
import java.util.List;

public class Schedule {
    private int WeekNum;
    private String supermarketAddress;  // Add this field
    private List<Shift> shifts;

    public Schedule(int weekNum, String supermarketAddress, List<Shift> shifts) {  // Adjust constructor
        this.WeekNum = weekNum;
        this.supermarketAddress = supermarketAddress;  // Initialize field
        this.shifts = shifts;
    }

    public int getWeekNUm() {
        return this.WeekNum;
    }

    public String getSupermarketAddress() {
        return supermarketAddress;  // Add getter
    }

    public List<Shift> getShifts() {
        return shifts;
    }
}
