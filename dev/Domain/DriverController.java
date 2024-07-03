package Domain;
import Data.DriverDAO;

import java.util.ArrayList;

public class DriverController {
    private DriverDAO driverDAO;


    public DriverController() {
       driverDAO = DriverDAO.getInstance();

    }

    public ArrayList<Driver> getAllDrivers() {
        return driverDAO.getAllDrivers();
    }

    public void addDriver(Driver driver) {
        driverDAO.addDriver(driver);
    }

    public boolean removeDriver(Driver driver) {
        return driverDAO.removeDriver(driver.getId());

    }

    public boolean updateDriverInfo(int driverId,String newLicenseType) {
       return driverDAO.updateDriverLicenseType(driverId,newLicenseType);
    }
    public Driver getDriverById(int driverId) {
        return driverDAO.getDriver(driverId);
    }

    public boolean isValidLicense(String requiredLicense, String driverLicense) {
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
    public boolean isDriverAvailable(int driverId){
        Driver driver = getDriverById(driverId);
        return driver.isAvailable();
    }
    public boolean setDriverAvailability(int driverId, boolean available) {
        Driver driver = getDriverById(driverId);
        driver.setAvailable(available);
        return driverDAO.updateDriverAvailability(driverId,available);
    }
    @Override
    public String toString() {
        StringBuilder d_details = new StringBuilder();
       for (Driver driver:driverDAO.getAllDrivers()){
           d_details.append(driver.toString());
           d_details.append("\n");
       }
       return d_details.toString();
    }
}
