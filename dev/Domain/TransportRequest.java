package Domain;

import java.util.ArrayList;

public class TransportRequest {
    private String requestDay;
    private String requestedTime;
    private Site originAddress;
    private ArrayList<SiteProducts> siteProducts;
    private int driverId;
    private String truckLicenseNumber;

    // Constructors, getters, and setters
    public TransportRequest(String requestDay, String requestedTime, Site originAddress, ArrayList<SiteProducts> siteProducts, String truckLicenseNumber, int driverId) {
        this.requestDay = requestDay;
        this.requestedTime = requestedTime;
        this.originAddress = originAddress;
        this.siteProducts = siteProducts;
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

    public int getDriverId() {
        return driverId;
    }

    public Site getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(Site originAddress) {
        this.originAddress = originAddress;
    }

    public ArrayList<SiteProducts> getSiteAndProducts(){
        return siteProducts;
    }
    public ArrayList<Site> getDestinations(){
        ArrayList<Site> dests = new ArrayList<>();
        for(SiteProducts siteProducts : this.siteProducts){
            dests.add(siteProducts.getSite());
        }
        return dests;
    }

}
