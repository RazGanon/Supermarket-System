package Domain;

public abstract class Report {
    protected static int nextId = 1;
    protected int id;
    public Report(){
        this.id = nextId++;
    }
    public int getReportId(){
        return this.id;
    }
}