package Domain;

import java.util.ArrayList;

public class TransportReport extends Report {
    private double initialWeight;
    private String changesMade;
    private Site source;
    private ArrayList<SiteProducts> siteAndProducts;

    public TransportReport(Site source, double initialWeight, ArrayList<SiteProducts> siteAndProducts) {
        super();
        this.changesMade = "";
        this.initialWeight = initialWeight;
        this.source = source;
        this.siteAndProducts = siteAndProducts;
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

    public ArrayList<SiteProducts> getSiteAndProducts() {
        return siteAndProducts;
    }

    public void setSiteAndProducts(ArrayList<SiteProducts> siteAndProducts) {
        this.siteAndProducts = siteAndProducts;
    }

    public Site getSource() {
        return source;
    }

    public void setSource(Site source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "TransportReport ID: " + getReportId() + ", Initial Weight: " + initialWeight + ", Changes Made: " + changesMade;
    }
}
