package Domain;
import java.util.ArrayList;
import java.util.List;
//import java.util.Optional;

public class TruckService {
    ArrayList<Truck> trucks;
    public TruckService() {
        this.trucks = new ArrayList<>();
    }

    // Method to add a new truck
    public void addTruck(Truck truck) {
        trucks.add(truck);
    }

    // Method to update an existing truck
//    public boolean updateTruck(String licenseNumber, Truck updatedTruck) {
//        Optional<Truck> existingTruckOpt = trucks.stream()
//                .filter(t -> t.getLicenseNumber().equals(licenseNumber))
//                .findFirst();
//
//        if (existingTruckOpt.isPresent()) {
//            Truck existingTruck = existingTruckOpt.get();
//            existingTruck.setNetWeight(updatedTruck.getNetWeight());
//            existingTruck.setAvailable(updatedTruck.getAvailable());
//            // Other fields can be updated similarly
//            return true;
//        }
//        return false;
//    }

    // Method to remove a truck by license number
    public boolean removeTruck(String licenseNumber) {
        return trucks.removeIf(truck -> truck.getLicenseNumber().equals(licenseNumber));
    }

    // Method to get a truck by license number
    public Truck getTruckByLicenseNumber(String licenseNumber) {
        return trucks.stream()
                .filter(t -> t.getLicenseNumber().equals(licenseNumber))
                .findFirst()
                .orElse(null);
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

    // Method to set a truck's availability
    public boolean setTruckAvailability(String licenseNumber, boolean available) {
        Truck truck = getTruckByLicenseNumber(licenseNumber);
        if (truck != null) {
            truck.setAvailable(available);
            return true;
        }
        return false;
    }

    public Truck getAvailableTruck() throws Exception {
        for(Truck truck : trucks){
            if (truck.isAvailable()){
                truck.setAvailable(false);
                return truck;
            }
        }
        throw new Exception("No available truck");
    }
}