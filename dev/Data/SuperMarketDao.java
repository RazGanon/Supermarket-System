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
    private static Map<Integer, SuperMarket> superMarketCache = new HashMap<>();

    private SuperMarketDao() {
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
                int id = rs.getInt("id");
                String address = rs.getString("address");
                String managerName = rs.getString("manager_name");
                SuperMarket superMarket = new SuperMarket(address, managerName);
                superMarkets.add(superMarket);
                superMarketCache.put(id, superMarket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return superMarkets;
    }

    public SuperMarket getSuperMarket(int id) {
        SuperMarket superMarket = superMarketCache.get(id);
        if (superMarket != null) {
            return superMarket;
        }
        try {
            conn = DataSource.openConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM SuperMarkets WHERE id = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String address = rs.getString("address");
                String managerName = rs.getString("manager_name");
                superMarket = new SuperMarket(address, managerName);
                superMarketCache.put(id, superMarket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return superMarket;
    }

    public void addSuperMarket(SuperMarket superMarket) {
        try {
            conn = DataSource.openConnection();
            PreparedStatement statement = conn.prepareStatement("INSERT INTO SuperMarkets (id, address, manager_name) VALUES (?, ?, ?)");
            statement.setInt(1, superMarket.getId());
            statement.setString(2, superMarket.getAddress());
            statement.setString(3, superMarket.getManagerName());
            statement.executeUpdate();
            superMarketCache.put(superMarket.getId(), superMarket);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
    }

    public boolean updateSuperMarket(int id, String newAddress, String newManagerName) {
        boolean success = false;
        try {
            conn = DataSource.openConnection();
            PreparedStatement statement = conn.prepareStatement("UPDATE SuperMarkets SET address = ?, manager_name = ? WHERE id = ?");
            statement.setString(1, newAddress);
            statement.setString(2, newManagerName);
            statement.setInt(3, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                SuperMarket superMarket = superMarketCache.get(id);
                if (superMarket != null) {
                    superMarket.setAddress(newAddress);
                    superMarket.setManagerName(newManagerName);
                }
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return success;
    }

    public void removeSuperMarket(int id) {
        try {
            conn = DataSource.openConnection();
            PreparedStatement statement = conn.prepareStatement("DELETE FROM SuperMarkets WHERE id = ?");
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                superMarketCache.remove(id);
                System.out.println("SuperMarket deleted successfully");
            } else {
                System.out.println("SuperMarket not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
    }
}
