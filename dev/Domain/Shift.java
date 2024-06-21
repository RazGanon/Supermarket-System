package Domain;

public class Shift {
    private String shiftType;
    private String startTime;
    private String endTime;

    public Shift(String shiftType, String startTime, String endTime) {
        this.shiftType = shiftType;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
