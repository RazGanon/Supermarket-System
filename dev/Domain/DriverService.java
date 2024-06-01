package Domain;

import java.util.HashMap;
import java.util.ArrayList;

public class DriverService {
    private ArrayList<Driver> drivers;
    private Map<Driver, Transport> assignedDrivers;

    public DriverService() {
        this.drivers = new ArrayList<>();
        this.assignedDrivers = new HashMap<>();
    }

    public ArrayList<Driver> viewDrivers() {
        return drivers;
    }

    public String assignDriverToTransport(Driver driver, Transport transport) {
        if (!drivers.contains(driver)) {
            return "Driver not found.";
        }

        if (!driver.isAvailable()) {
            return "Driver is not available.";
        }

        // Additional logic to check if the driver has the appropriate license type for the transport
        if (!isLicenseValid(driver, transport)) {
            return "Driver does not have the appropriate license.";
        }

        assignedDrivers.put(driver, transport);
        driver.setAvailable(false);
        return "Driver " + driver.getDriverName() + " assigned to transport.";
    }

    public boolean isLicenseValid(Driver driver, Transport transport) {
        // Placeholder logic for checking if the driver's license is valid for the transport
        // This can be expanded based on the actual requirements of the transport
        return driver.getLicenseType().equals(transport.getRequiredLicenseType());
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
