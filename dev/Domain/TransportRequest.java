package Domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TransportRequest {
    private LocalDate requestDay;
    private LocalTime requestedTime;
    private Site originAddress;
    private ArrayList<Site> destinationAddresses;
    private ArrayList<ProductsReport> product_reports;
    private String requiredLicense;
    private double transportWeight;

    // Constructors, getters, and setters
    public TransportRequest(LocalDate requestDay, LocalTime requestedTime, Site originAddress, ArrayList<Site> destinationAddresses,ArrayList<ProductsReport> p_reports,String r_license,double t_weight) {
        this.requestDay = requestDay;
        this.requestedTime = requestedTime;
        this.originAddress = originAddress;
        this.destinationAddresses = destinationAddresses;
        this.product_reports = p_reports;
        this.requiredLicense = r_license;
        this.transportWeight = t_weight;
    }

    public String getRequiredLicense() {
        return requiredLicense;
    }

    public double getTransportWeight() {
        return transportWeight;
    }

    public LocalDate getRequestDay() {
        return requestDay;
    }

    public void setRequestDay(LocalDate requestDay) {
        this.requestDay = requestDay;
    }

    public LocalTime getRequestedTime() {
        return requestedTime;
    }

    public void setRequestedTime(LocalTime requestedTime) {
        this.requestedTime = requestedTime;
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
    public ArrayList<ProductsReport> getProduct_reports(){
        return this.product_reports;
    }

}