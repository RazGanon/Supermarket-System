package Domain;

public class Shift {
    private String shiftType;
    private String day;
    private int Shiftid;

    public Shift(String day , String shiftType,int shiftid) {
        this.shiftType = shiftType;
        this.day = day;
        this.Shiftid = shiftid;

    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }


    public int getShiftid() {
        return this.Shiftid;
    }


    public String getday() {
        return this.day;
    }

}
