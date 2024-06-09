package Presentation;

import Domain.Truck;
import Service.TruckService;
import com.google.gson.Gson;

public class TruckController {
    private static TruckController instance;
    private TruckService truckService;
    private Gson gson;

    private TruckController() {
        this.truckService = new TruckService();
        this.gson = new Gson();
    }

    public static TruckController getInstance() {
        if (instance == null) {
            instance = new TruckController();
        }
        return instance;
    }

    public String addTruck(String json) {
        Truck truck = gson.fromJson(json, Truck.class);

        // Validate truck data
        if (truck.getLicenseNumber() == null || truck.getLicenseNumber().isEmpty()) {
            return "Error: Truck license number is required.";
        }
        if (truck.getModel() == null || truck.getModel().isEmpty()) {
            return "Error: Truck model is required.";
        }
        if (truck.getRequiredLicense() == null || truck.getRequiredLicense().isEmpty()) {
            return "Error: Required license is required.";
        }

        truckService.addTruck(truck);
        return "Truck added successfully.";
    }

    public String removeTruck(String licenseNumber) {
        boolean result = truckService.removeTruck(licenseNumber);
        return result ? "Truck removed successfully." : "Truck not found.";
    }

    public String getAllTrucks() {
        return gson.toJson(truckService.getAllTrucks());
    }

    public TruckService getTruckService() {
        return truckService;
    }

    public String setTruckAvailability(String licenseNumber, boolean available) {
        if (truckService.setTruckAvailability(licenseNumber, available)) {
            return "Truck availability updated.";
        } else {
            return "Truck not found.";
        }
    }

    public String getTruckByLicenseNumber(String id) {
        return gson.toJson(truckService.getTruckByLicenseNumber(id));
    }

}
