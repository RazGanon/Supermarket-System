package Domain;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class WorkArangment {

    private final Map<ShiftPair, Shift> dailySchedules;
    //constructor
    public WorkArangment() {
        dailySchedules = new HashMap<>();
    }
    //getter
    public Map<ShiftPair, Shift> GetWorkArrangement(){
        return this.dailySchedules;
    }

}
