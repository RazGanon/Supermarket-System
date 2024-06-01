package Domain;

public abstract class Report {
    private String reportId;
    Report(String r_Id){
        this.reportId = r_Id;
    }
    public String getReportId(){
        return reportId;
    }

}