package Domain;

public class Driver {
    private String licenseType;
    private String driverName;
    private boolean available;

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public String getDriverName() {
        return driverName;
    }

    public boolean isAvailable() {
        return available;
    }

    public Driver(String licenseType, String driverName, boolean available) {
        this.licenseType = licenseType;
        this.driverName = driverName;
        this.available = available;
    }
}
