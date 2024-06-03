package Domain;

import java.util.HashMap;
import java.util.ArrayList;

public class DriverService {
    private ArrayList<Driver> drivers;
    private HashMap<Driver, Transport> assignedDrivers;

    public DriverService() {
        this.drivers = new ArrayList<>();
        this.assignedDrivers = new HashMap<>();
    }

    public ArrayList<Driver> getAllDrivers() {
        return drivers;
    }



    public Driver getValidDriver(TransportRequest t_request) throws Exception {
        for(Driver driver : drivers){
            if (driver.isAvailable() && driver.getLicenseType().equals(t_request.getRequiredLicense())){
                driver.setAvailable(false);
                return driver;
            }
        }
        throw new Exception("No available driver");
    }

    public void addDriver(Driver driver) {
        drivers.add(driver);
    }

    public String removeDriver(Driver driver) {
        if (drivers.remove(driver)) {
            assignedDrivers.remove(driver);
            return "Driver " + driver.getDriverName() + " removed.";
        }
        return "Driver not found.";
    }

    public String updateDriverInfo(Driver driver, String newLicenseType, boolean newAvailability) {
        if (drivers.contains(driver)) {
            driver.setLicenseType(newLicenseType);
            driver.setAvailable(newAvailability);
            return "Driver " + driver.getDriverName() + "'s information updated.";
        }
        return "Driver not found.";
    }

    public Driver getDriverByName(String driverName) {
        for (Driver driver : drivers) {
            if (driver.getDriverName().equals(driverName)) {
                return driver;
            }
        }
        return null;
    }

    public Transport getAssignedTransport(Driver driver) {
        return assignedDrivers.get(driver);
    }

    public void releaseDriverFromTransport(Driver driver) {
        if (assignedDrivers.containsKey(driver)) {
            assignedDrivers.remove(driver);
            driver.setAvailable(true);
        }
    }

}
