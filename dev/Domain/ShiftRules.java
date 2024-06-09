package Domain;

public class ShiftRules implements rules {
    private int morningManagerCount;
    private int morningEmployeeCount;
    private int eveningManagerCount;
    private int eveningEmployeeCount;

    public ShiftRules(int morningManagerCount, int morningEmployeeCount,int eveningManagerCount,int eveningEmployeeCount) {
        this.morningManagerCount = morningManagerCount;
        this.morningEmployeeCount = morningEmployeeCount;
        this.eveningManagerCount = eveningManagerCount;
        this.eveningEmployeeCount = eveningEmployeeCount;
    }

    @Override
    public int getMorningManagerCount() {
        return morningManagerCount;
    }

    @Override
    public int getMorningEmployeeCount() {
        return morningEmployeeCount;
    }

    @Override
    public int getEveningManagerCount() {
        return eveningManagerCount;
    }

    @Override
    public int getEveningEmployeeCount() {
        return eveningEmployeeCount;
    }

    @Override
    public void setMorningManagerCount(int morningManagerCount) {
        this.morningManagerCount = morningManagerCount;
    }
    @Override

    public void setMorningEmployeeCount(int morningEmployeeCount) {
        this.morningEmployeeCount = morningEmployeeCount;
    }

    public void setEveningManagerCount(int eveningManagerCount) {
        this.eveningManagerCount = eveningManagerCount;
    }

    public void setEveningEmployeeCount(int eveningEmployeeCount) {
        this.eveningEmployeeCount = eveningEmployeeCount;
    }

    @Override
    public int getManagerCount() {
        return morningManagerCount+eveningManagerCount;
    }

    @Override
    public int getEmployeeCount() {
        return morningEmployeeCount+eveningEmployeeCount;
    }
}