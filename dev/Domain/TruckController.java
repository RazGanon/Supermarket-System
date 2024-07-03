package Domain;

import Data.TruckDAO;
import java.util.ArrayList;
import java.util.List;

public class TruckController {
    private TruckDAO truckDAO;

    public TruckController() {
        truckDAO = TruckDAO.getInstance();
    }

    public List<Truck> getAllTrucks() {
        return truckDAO.getAllTrucks();
    }

    public void addTruck(Truck truck) {
        truckDAO.addTruck(truck);
    }

    public boolean removeTruck(String licenseNumber) {
        return truckDAO.deleteTruck(licenseNumber);
    }

    public Truck getTruckByLicenseNumber(String licenseNumber) {
        return truckDAO.getTruck(licenseNumber);
    }

    public boolean isTruckAvailable(String licenseNumber) {
        Truck truck = getTruckByLicenseNumber(licenseNumber);
        return truck != null && truck.isAvailable();
    }

    public boolean isTrackValid(Truck truck, double weight) {
        return !(truck.getNetWeight() + weight > truck.getMaxWeight());
    }

    public Truck getAvailableTruck(double weight) throws Exception {
        for (Truck truck : getAllTrucks()) {
            if (truck.isAvailable() && truck.getMaxWeight() >= weight) {
                truck.setAvailable(false);
                truckDAO.updateTruck(truck); // Update availability in the database
                return truck;
            }
        }
        throw new Exception("No available truck");
    }

    public boolean setTruckAvailability(String licenseNumber, boolean available) {
        Truck truck = getTruckByLicenseNumber(licenseNumber);
        if (truck != null) {
            truck.setAvailable(available);
            truckDAO.updateTruck(truck); // Update availability in the database
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder t_details = new StringBuilder();
        for (Truck truck : getAllTrucks()) {
            t_details.append(truck.toString());
            t_details.append("\n");
        }
        return t_details.toString();
    }
}
