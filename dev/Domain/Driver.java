package Domain;

public class Driver {
    private String licenseType;
    private String driverName;
    private boolean available;
    private int id;
    public Driver(String licenseType, String driverName, boolean available,int id) {
        this.licenseType = licenseType;
        this.driverName = driverName;
        this.available = available;
        this.id = id;

    }
    public int getId(){
        return this.id;
    }
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


    public String toString() {
        return "Driver Name: " + driverName + ", License Type: " + licenseType + ", Available: " + available;
    }
}
