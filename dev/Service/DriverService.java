package Service;
import Domain.Driver;

import java.util.ArrayList;

public class DriverService {
    private ArrayList<Driver> drivers;


    public DriverService() {
        this.drivers = new ArrayList<>();

    }

    public ArrayList<Driver> getAllDrivers() {
        return drivers;
    }

    public void addDriver(Driver driver) {
        drivers.add(driver);
    }

    public String removeDriver(Driver driver) {
        if (drivers.remove(driver)) {
            return "Driver " + driver.getDriverName() + "ID:" + driver.getId() + " removed.";
        }
        return null;
    }

    public boolean updateDriverInfo(int driverId,String newLicenseType) {
        for (Driver driver : drivers) {
            if (driver.getId() == driverId) {
                driver.setLicenseType(newLicenseType);
                return true;
            }
        }
        return false;
    }
    public Driver getDriverById(long driverId) {
        for (Driver driver : drivers) {
            if (driver.getId() == driverId) {
                return driver;
            }
        }
        return null;
    }

    public boolean isValidLicense(String requiredLicense, String driverLicense) {
        // Assuming a hierarchical order: A < B < C < D < E
        String[] licenses = {"A", "B", "C", "D","E"};
        int requiredIndex = -1;
        int driverIndex = -1;
        for (int i = 0; i < licenses.length; i++) {
            if (licenses[i].equals(requiredLicense)) {
                requiredIndex = i;
            }
            if (licenses[i].equals(driverLicense)) {
                driverIndex = i;
            }
        }
        return driverIndex >= requiredIndex;
    }
    public boolean isDriverAvailable(long driverId){
        Driver driver = getDriverById(driverId);
        return driver.isAvailable();
    }
    public boolean setDriverAvailability(long driverId, boolean available) {
        for (Driver driver : drivers) {
            if (driver.getId() == driverId) {
                driver.setAvailable(available);
                return true;
            }
        }
        return false;
    }
    @Override
    public String toString() {
        StringBuilder d_details = new StringBuilder(); // driver details
       for (Driver driver:drivers){
           d_details.append(driver.toString());
           d_details.append("\n");
       }
       return d_details.toString();
    }
}
