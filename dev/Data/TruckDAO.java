package Data;


import Domain.Truck;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class TruckDAO {  //singleton
    private Connection conn;
    private static TruckDAO single_instance = null;
    private HashMap<String, Truck> truckCache;

    private TruckDAO() {
        truckCache = new HashMap<>();
    }

    public static synchronized TruckDAO getInstance() {
        if (single_instance == null) {
            single_instance = new TruckDAO();
        }
        return single_instance;
    }
    public ArrayList<Truck> getAllTrucks(){
        ArrayList<Truck> trucks = new ArrayList<>();
        try{
            conn = DataSource.openConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM trucks");
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
               String licenseNumber = rs.getString("license_number");
               String model = rs.getString("model");
               double netWeight = rs.getDouble("net_weight");
               double maxWeight = rs.getDouble("max_weight");
               String requiredLicense = rs.getString("required_license");
               boolean available = rs.getBoolean("available");
               Truck newTruck = new Truck(licenseNumber,model,netWeight,maxWeight,requiredLicense,available);
               trucks.add(newTruck);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
        DataSource.closeConnection();
        return trucks;
    }

    public Truck getTruck(String licensenumber) {
        try {
            conn = DataSource.openConnection();
            Truck truck1 = truckCache.get(licensenumber);
            if (truck1 == null) {
                PreparedStatement statement = conn.prepareStatement("SELECT * FROM trucks  WHERE license_number = ?");
                statement.setString(1,licensenumber);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    // Retrieve by column name
                    String model = rs.getString("model");
                    double netWeight = rs.getDouble("net_weight");
                    double maxWeight = rs.getDouble("max_weight");
                    String requiredLicense = rs.getString("required_license");
                    boolean available = rs.getBoolean("available");
                    Truck truck2 = new Truck(licensenumber, model, netWeight, maxWeight, requiredLicense,available);
                    DataSource.closeConnection();
                    return truck2;
                }
            }else {
                return truck1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return null;

    }
    public boolean addTruck(Truck truck) {
        conn = DataSource.openConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO trucks (license_number, model, net_weight, max_weight, required_license, available) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setString(1, truck.getLicenseNumber());
            statement.setString(2, truck.getModel());
            statement.setDouble(3, truck.getNetWeight());
            statement.setDouble(4, truck.getMaxWeight());
            statement.setString(5, truck.getRequiredLicense());
            statement.setBoolean(6, truck.isAvailable());
            int rowsInserted = statement.executeUpdate();
            DataSource.closeConnection();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTruck(Truck truck) {
        conn = DataSource.openConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE trucks SET model = ?, net_weight = ?, max_weight = ?, required_license = ?, available = ? WHERE license_number = ?");
            statement.setString(1, truck.getModel());
            statement.setDouble(2, truck.getNetWeight());
            statement.setDouble(3, truck.getMaxWeight());
            statement.setString(4, truck.getRequiredLicense());
            statement.setBoolean(5, truck.isAvailable());
            statement.setString(6, truck.getLicenseNumber());
            int rowsUpdated = statement.executeUpdate();
            DataSource.closeConnection();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteTruck(String licenseNumber) {
        conn = DataSource.openConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM trucks WHERE license_number = ?");
            statement.setString(1, licenseNumber);
            int rowsDeleted = statement.executeUpdate();
            DataSource.closeConnection();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}


