package Domain;

import java.util.ArrayList;

public class TransportRequest {
    private String requestDay;
    private String requestedTime;
    private Site originAddress;
    private ArrayList<Site> destinationAddresses;
    private ArrayList<SiteProductsReport> siteProductsReports;
    private long driverId;
    private String truckLicenseNumber;

    // Constructors, getters, and setters
    public TransportRequest(String requestDay, String requestedTime, Site originAddress, ArrayList<SiteProductsReport> siteProductsReports, String truckLicenseNumber, long driverId,ArrayList<Site> dest) {
        this.requestDay = requestDay;
        this.requestedTime = requestedTime;
        this.originAddress = originAddress;
        this.destinationAddresses = dest;
        this.siteProductsReports = siteProductsReports;
        this.truckLicenseNumber = truckLicenseNumber;
        this.driverId = driverId;
    }

    public String getTruckLicenseNumber() {
        return truckLicenseNumber;
    }

    public String getRequestDay() {
        return requestDay;
    }

    public void setRequestDay(String requestDay) {
        this.requestDay = requestDay;
    }

    public String getRequestedTime() {
        return requestedTime;
    }

    public void setRequestedTime(String requestedTime) {
        this.requestedTime = requestedTime;
    }

    public long getDriverId() {
        return driverId;
    }

    public Site getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(Site originAddress) {
        this.originAddress = originAddress;
    }

    public ArrayList<Site> getDestinationAddresses() {
        return destinationAddresses;
    }

    public ArrayList<SiteProductsReport> getSiteProductsReports() {
        return siteProductsReports;
    }
}
