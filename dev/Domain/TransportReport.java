package Domain;

public class TransportReport extends Report {
    private double initialWeight;
    private String changesMade;
    TransportReport(double i_weight) {
        super();
        this.changesMade = "";
        this.initialWeight = i_weight;
    }

    public void addChangesMade(String changesMade) {
        this.changesMade += changesMade;
    }

    public void setInitialWeight(double initialWeight) {
        this.initialWeight = initialWeight;
    }

    @Override
    public int getReportId() {
        return super.getReportId();
    }

    public double getInitialWeight() {
        return initialWeight;
    }

    public String getChangesMade() {
        return changesMade;
    }
}