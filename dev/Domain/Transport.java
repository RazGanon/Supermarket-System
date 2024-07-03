package Domain;

import java.util.ArrayList;

public class Transport {
    private Truck truck;
    private Driver driver;
    private Site originAddress;
    private ArrayList<Site> destinations;
    private String requestedTime;
    private String requestDay;
    private TransportReport transportReport;


    public Transport(Truck truck, Driver driver, Site originAddress, String requestedTime, String requestDay, ArrayList<Site> destinations, TransportReport transportReport) {
        this.truck = truck;
        this.driver = driver;
        this.originAddress = originAddress;
        this.requestedTime = requestedTime;
        this.requestDay = requestDay;
        this.destinations = destinations;
        this.transportReport = transportReport;

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

    public String getRequestedTime() {
        return requestedTime;
    }

    public String getRequestDay() {
        return requestDay;
    }

    public void setDestinations(ArrayList<Site> destinations) {
        this.destinations = destinations;
    }

    public TransportReport getTransportReport() {
        return transportReport;
    }

    public void setTransportReport(TransportReport transportReport) {
        this.transportReport = transportReport;
    }

}
