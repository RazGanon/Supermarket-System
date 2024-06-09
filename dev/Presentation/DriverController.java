package Presentation;

import Domain.Driver;
import Service.DriverService;
import com.google.gson.Gson;

public class DriverController {
    private static DriverController instance;
    private DriverService driverService;
    private Gson gson;

    private DriverController() {
        this.driverService = new DriverService();
        this.gson = new Gson();
    }

    public static DriverController getInstance() {
        if (instance == null) {
            instance = new DriverController();
        }
        return instance;
    }

    public DriverService getDriverService() {
        return driverService;
    }

    public String addDriver(String json) {
        Driver driver = gson.fromJson(json, Driver.class);

        // Validate driver data
        if (driver.getDriverName() == null || driver.getDriverName().isEmpty()) {
            return "Error: Driver name is required.";
        }
        if (driver.getLicenseType() == null || driver.getLicenseType().isEmpty()) {
            return "Error: License type is required.";
        }

        driverService.addDriver(driver);
        return "Driver added successfully.";
    }

    public String removeDriver(long id) {
        Driver driver = driverService.getDriverById(id);
        if (driver != null) {
            return driverService.removeDriver(driver);
        } else {
            return "Error: Driver not found.";
        }
    }

    public String setDriverAvailability(long id, boolean available) {
        if (driverService.setDriverAvailability(id, available)) {
            return "Driver availability updated.";
        } else {
            return "Error: Driver not found.";
        }
    }

    public String getAllDrivers() {
        return gson.toJson(driverService.getAllDrivers());
    }

    public String getDriverById(long id) {
        Driver driver = driverService.getDriverById(id);
        return driver != null ? gson.toJson(driver) : null;
    }

    public String updateDriverInfo(int id, String newLicenseType) {
        if (newLicenseType == null || newLicenseType.isEmpty()) {
            return "Error: New license type is required.";
        }
        if (driverService.updateDriverInfo(id, newLicenseType)) {
            return "Driver license updated";
        } else {
            return "Error: Driver not found.";
        }
    }
}
