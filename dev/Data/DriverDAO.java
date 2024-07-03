package Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Domain.Driver;
import java.util.HashMap;
import java.util.ArrayList;
public class DriverDAO {   //singleton
    private Connection conn;
    private static DriverDAO single_instance;
    private HashMap<Integer, Driver> driverCache;

    private DriverDAO() {
        driverCache = new HashMap<>();
    }
    public  ArrayList<Driver> getAllDrivers() {
        ArrayList<Driver> driversList = new ArrayList<>();
        try {
            conn = DataSource.openConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM drivers");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String licenseType = rs.getString("license_type");
                boolean available = rs.getBoolean("available");
                Driver driver = new Driver(licenseType, name, available, id);
                driversList.add(driver);
                driverCache.put(id, driver);
            }
            DataSource.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return driversList;
    }
    public static synchronized DriverDAO getInstance() {
        if (single_instance == null) {
            single_instance = new DriverDAO();
        }
        return single_instance;
    }

    public Driver getDriver(int driverid) {
        try {
            conn = DataSource.openConnection();
            Driver driver = driverCache.get(driverid);
            if (driver == null) {
                PreparedStatement statement = conn.prepareStatement("SELECT * FROM drivers  WHERE ID = ?");
                statement.setInt(1, driverid);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    // Retrieve by column name
                    Integer id = rs.getInt("id");
                    String name = rs.getString("name");
                    String licenseType = rs.getString("license_type");
                    boolean available = rs.getBoolean("available");
                    driver = new Driver(licenseType,name,available,id);

                    // Add the driver to the cache
                    driverCache.put(id, driver);
                    DataSource.closeConnection();
                    return driver;
                }
            }else{
                return driver;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                DataSource.closeConnection();
            }
        }
        return null;

    }
    public void addDriver(Driver driver) {
        try {
            conn = DataSource.openConnection();
            conn.setAutoCommit(false);  // Start transaction
            PreparedStatement statement = conn.prepareStatement("INSERT INTO drivers (id,name,license_type,available) VALUES (?,?,?,?)");
            statement.setInt(1, driver.getId());
            statement.setString(2, driver.getDriverName());
            statement.setString(3, driver.getLicenseType());
            statement.setBoolean(4, driver.isAvailable());
            statement.executeUpdate();  // Execute the update
            driverCache.put(driver.getId(), driver);
            conn.commit();  // Commit the transaction
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();  // Rollback in case of error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            DataSource.closeConnection();  // Ensure the connection is closed in finally block
        }
    }

    public boolean updateDriverLicenseType(int driverId, String newLicenseType) {
        try {
            conn = DataSource.openConnection();
            PreparedStatement statement = conn.prepareStatement("UPDATE drivers SET license_type = ? WHERE id = ?");
            statement.setString(1, newLicenseType);
            statement.setInt(2, driverId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                // Update the cache if the driver exists
                Driver driver = driverCache.get(driverId);
                if (driver != null) {
                    driver.setLicenseType(newLicenseType);
                }
                DataSource.closeConnection();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataSource.closeConnection();
        return false;
    }


    public boolean removeDriver(int driverId) {
        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;

        try {
            conn = DataSource.openConnection();
            conn.setAutoCommit(false);


            checkStmt = conn.prepareStatement("SELECT count(*) FROM transports WHERE driver_id = ?");
            checkStmt.setInt(1, driverId);
            rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }
            updateStmt = conn.prepareStatement("DELETE FROM drivers WHERE id = ?");
            updateStmt.setInt(1, driverId);
            int affectedRows = updateStmt.executeUpdate();
            conn.commit();
            driverCache.remove(driverId);
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();  // Rollback in case of error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (checkStmt != null) checkStmt.close();
                if (updateStmt != null) updateStmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean updateDriverAvailability(int driverId,boolean available) {
        try {
            conn = DataSource.openConnection();
            PreparedStatement statement = conn.prepareStatement("UPDATE drivers SET available = ? WHERE id = ?");
            statement.setBoolean(1, available);
            statement.setInt(2, driverId);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0; // Returns true if the update was successful
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
        return false;
    }

}
