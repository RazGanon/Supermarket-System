package Domain;

public class TransportReport extends Report {
    private double initialWeight;
    private String changesMade;
    TransportReport(String r_Id,double i_weight) {
        super(r_Id);
        this.changesMade = "";
        this.initialWeight = i_weight;
    }

    public void setChangesMade(String changesMade) {
        this.changesMade += changesMade;
    }

    public void setInitialWeight(double initialWeight) {
        this.initialWeight = initialWeight;
    }

    @Override
    public String getReportId() {
        return super.getReportId();
    }

    public double getInitialWeight() {
        return initialWeight;
    }

    public String getChangesMade() {
        return changesMade;
    }
}