package Domain;

public abstract class Report {
    private static int nextId = 1;
    private int id;
    Report(){
        this.id = nextId++;
    }
    public int getReportId(){
        return this.id;
    }

}