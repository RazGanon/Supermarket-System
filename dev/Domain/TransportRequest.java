package Domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TransportRequest {
    private LocalDate requestDay;
    private LocalTime requestedTime;
    private String truckNumber;
    private String driverName;
    private Site originAddress;
    private List<Site> destinationAddresses;

    // Constructors, getters, and setters
    public TransportRequest(LocalDate requestDay, LocalTime requestedTime, String truckNumber, String driverName, Site originAddress, List<Site> destinationAddresses) {
        this.requestDay = requestDay;
        this.requestedTime = requestedTime;
        this.truckNumber = truckNumber;
        this.driverName = driverName;
        this.originAddress = originAddress;
        this.destinationAddresses = destinationAddresses;
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

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckLicenseNumber) {
        this.truckNumber = truckLicenseNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Site getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(Site originAddress) {
        this.originAddress = originAddress;
    }

    public List<Site> getDestinationAddresses() {
        return destinationAddresses;
    }

    public void setDestinationAddresses(List<Site> destinationAddresses) {
        this.destinationAddresses = destinationAddresses;
    }
}