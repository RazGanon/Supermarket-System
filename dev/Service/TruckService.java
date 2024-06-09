package Service;
import Domain.Truck;

import java.util.ArrayList;
import java.util.List;


public class TruckService {
    ArrayList<Truck> trucks;
    public TruckService() {
        this.trucks = new ArrayList<>();
    }

    // Method to add a new truck
    public void addTruck(Truck truck) {
        trucks.add(truck);
    }


    public boolean removeTruck(String licenseNumber) {
        return trucks.removeIf(truck -> truck.getLicenseNumber().equals(licenseNumber));
    }

    // Method to get a truck by license number
    public Truck getTruckByLicenseNumber(String licenseNumber) {
       for (Truck truck : trucks){
           if (truck.getLicenseNumber().equals(licenseNumber)){
               return truck;
           }
       }
       return null;
    }

    // Method to get all trucks
    public List<Truck> getAllTrucks() {
        return new ArrayList<>(trucks);
    }

    // Method to check availability of a truck
    public boolean isTruckAvailable(String licenseNumber) {
        Truck truck = getTruckByLicenseNumber(licenseNumber);
        return truck != null && truck.isAvailable();
    }
    public boolean isTrackValid(Truck truck,double weight){
        return !(truck.getNetWeight() + weight > truck.getMaxWeight());
    }


    public Truck getAvailableTruck(double weight) throws Exception {
        for(Truck truck : trucks){
            if(truck.isAvailable() && truck.getMaxWeight()>= weight){
                truck.setAvailable(false);
                return truck;
            }
            }

        throw new Exception("No available truck");
    }
    public boolean setTruckAvailability(String licenseNumber, boolean available) {
        Truck truck = getTruckByLicenseNumber(licenseNumber);
        if (truck != null) {
            truck.setAvailable(available);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder t_details = new StringBuilder();
        for (Truck truck : trucks){
            t_details.append(truck.toString());
            t_details.append("\n");
        }
        return t_details.toString();
    }
}