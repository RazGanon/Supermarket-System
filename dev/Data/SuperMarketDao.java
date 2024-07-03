package Data;

import Domain.SuperMarket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperMarketDao {
    private Connection conn;
    private static SuperMarketDao single_instance = null;
    private static Map<String, SuperMarket> superMarketCache = new HashMap<>();

    public SuperMarketDao() {
    }

    public static synchronized SuperMarketDao getInstance() {
        if (single_instance == null) {
            single_instance = new SuperMarketDao();
        }
        return single_instance;
    }

    public List<SuperMarket> getAllSuperMarkets() {
        List<SuperMarket> superMarkets = new ArrayList<>();
        try {
            conn = DataSource.openConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM SuperMarkets");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String address = rs.getString("address");
                String managerName = rs.getString("manager_name");
                SuperMarket superMarket = new SuperMarket(address, managerName);
                superMarkets.add(superMarket);
                superMarketCache.put(address, superMarket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return superMarkets;
    }

    public SuperMarket getSuperMarketByAddress(String address) {
        SuperMarket superMarket = superMarketCache.get(address);
        if (superMarket != null) {
            return superMarket;
        }
        try {
            conn = DataSource.openConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM SuperMarkets WHERE address = ?");
            statement.setString(1, address);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String managerName = rs.getString("manager_name");
                superMarket = new SuperMarket(address, managerName);
                superMarketCache.put(address, superMarket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //DataSource.closeConnection();
        }
        return superMarket;
    }

    public void addSuperMarket(SuperMarket superMarket) {
        try {
            conn = DataSource.openConnection();
            PreparedStatement statement = conn.prepareStatement("INSERT INTO SuperMarkets (address, manager_name) VALUES (?, ?)");
            statement.setString(1, superMarket.getAddress());
            statement.setString(2, superMarket.getManagerName());
            statement.executeUpdate();
            superMarketCache.put(superMarket.getAddress(), superMarket);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //DataSource.closeConnection();
        }
    }

    public boolean updateSuperMarket(String address, String newAddress, String newManagerName) {
        boolean success = false;
        try {
            conn = DataSource.openConnection();
            PreparedStatement statement = conn.prepareStatement("UPDATE SuperMarkets SET address = ?, manager_name = ? WHERE address = ?");
            statement.setString(1, newAddress);
            statement.setString(2, newManagerName);
            statement.setString(3, address);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                SuperMarket superMarket = superMarketCache.get(address);
                if (superMarket != null) {
                    superMarket.setAddress(newAddress);
                    superMarket.setManagerName(newManagerName);
                    superMarketCache.put(newAddress, superMarket);
                    superMarketCache.remove(address);
                }
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
           //DataSource.closeConnection();
        }
        return success;
    }

    public void removeSuperMarket(String address) {
        try {
            conn = DataSource.openConnection();
            PreparedStatement statement = conn.prepareStatement("DELETE FROM SuperMarkets WHERE address = ?");
            statement.setString(1, address);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                superMarketCache.remove(address);
                System.out.println("SuperMarket deleted successfully");
            } else {
                System.out.println("SuperMarket not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //DataSource.closeConnection();
        }
    }
}
