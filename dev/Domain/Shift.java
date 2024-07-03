package Domain;

public class Shift {
    private String shiftType;
    private String day;

    public Shift(String day , String shiftType) {
        this.shiftType = shiftType;
        this.day = day;

    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }




    public String getday() {
        return this.day;
    }

}
