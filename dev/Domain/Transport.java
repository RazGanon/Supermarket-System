package Domain;

import java.util.ArrayList;

public class Transport {
    private Truck truck;
    private Driver driver;
    private Site originAddress;
    private ArrayList<Site> destinations;
    private ArrayList<SiteProductsReport> siteAndProducts;
    private String requestedTime;
    private String requestDay;
    private TransportReport transportReport;

    public Transport(Truck truck, Driver driver, Site originAddress, TransportReport transportReport, String requestedTime, String requestDay, ArrayList<SiteProductsReport> siteAndProducts,ArrayList<Site> dest) {
        this.truck = truck;
        this.driver = driver;
        this.originAddress = originAddress;
        this.transportReport = transportReport;
        this.requestedTime = requestedTime;
        this.requestDay = requestDay;
        this.siteAndProducts = siteAndProducts;
        this.destinations = dest;
    }

    // Getters and Setters
    public int getTransportId() {
        return transportReport.getReportId();
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public Driver getDriver() {
        return driver;
    }

    public Site getOriginAddress() {
        return originAddress;
    }

    public ArrayList<Site> getDestinations() {
        return destinations;
    }

    public ArrayList<SiteProductsReport> getSiteAndProducts() {
        return siteAndProducts;
    }

    public String getRequestedTime() {
        return requestedTime;
    }

    public String getRequestDay() {
        return requestDay;
    }

    public TransportReport getTsp() {
        return transportReport;
    }
}
