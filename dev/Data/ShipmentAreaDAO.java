package Data;

import Domain.ShipmentArea;
import Domain.Site;
import java.sql.*;
import java.util.ArrayList;

public class ShipmentAreaDAO {
    private Connection conn;
    private static ShipmentAreaDAO single_instance = null;

    private ShipmentAreaDAO() {
    }

    public static synchronized ShipmentAreaDAO getInstance() {
        if (single_instance == null) {
            single_instance = new ShipmentAreaDAO();
        }
        return single_instance;
    }

    public ArrayList<ShipmentArea> getAllAreas() {
        conn = DataSource.openConnection();
        ArrayList<ShipmentArea> shipmentAreas = new ArrayList<>();
        try {
            String sql = "SELECT * FROM shipmentareas";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String areaName = rs.getString("area_name");
                ShipmentArea shipmentArea = new ShipmentArea(new ArrayList<>(), areaName);
                shipmentArea.setSites(getSitesByArea(areaName));
                shipmentAreas.add(shipmentArea);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return shipmentAreas;
    }

    public ArrayList<Site> getSitesByArea(String areaName) {
        conn = DataSource.openConnection();
        ArrayList<Site> sites = new ArrayList<>();
        try {
            String sql = "SELECT * FROM sites WHERE area_name = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, areaName);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String address = rs.getString("address");
                String contactNumber = rs.getString("contact_number");
                String contactName = rs.getString("contact_name");
                Site site = new Site(address, contactName, contactNumber);
                sites.add(site);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return sites;
    }

    public boolean addShipmentArea(ShipmentArea shipmentArea) {
        conn = DataSource.openConnection();
        try {
            String sql = "INSERT INTO shipmentareas (area_name) VALUES (?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, shipmentArea.getAreaName());
            int rowsInserted = statement.executeUpdate();
            DataSource.closeConnection();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addSiteToArea(Site site, String areaName) {
        conn = DataSource.openConnection();
        try {
            String sql = "INSERT INTO sites (address, contact_number, contact_name, area_name) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, site.getAddress());
            statement.setString(2, site.getContactPhoneNumber());
            statement.setString(3, site.getContactName());
            statement.setString(4, areaName);
            int rowsInserted = statement.executeUpdate();
            DataSource.closeConnection();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int removeSiteFromArea(String address, String areaName) {
        conn = DataSource.openConnection();
        try {
            PreparedStatement checkAreaStatement = conn.prepareStatement("SELECT COUNT(*) FROM shipmentareas WHERE area_name = ?");
            checkAreaStatement.setString(1, areaName);
            ResultSet rs = checkAreaStatement.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                return 5;
            }
            PreparedStatement checkSiteStatement = conn.prepareStatement("SELECT COUNT(*) FROM sites WHERE address = ?");
            checkSiteStatement.setString(1, address);
            rs = checkSiteStatement.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                return 4;
            }
            PreparedStatement checkSiteInAreaStatement = conn.prepareStatement("SELECT COUNT(*) FROM sites WHERE address = ? AND area_name = ?");
            checkSiteInAreaStatement.setString(1, address);
            checkSiteInAreaStatement.setString(2, areaName);
            rs = checkSiteInAreaStatement.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                return 2;
            }
            PreparedStatement checkTransportReportsStatement = conn.prepareStatement( "SELECT COUNT(*) FROM transportreports WHERE source_site_address = ?");
            checkTransportReportsStatement.setString(1, address);
            rs = checkTransportReportsStatement.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return 3;
            }
            PreparedStatement checkTransportDestinationsStatement = conn.prepareStatement("SELECT COUNT(*) FROM transportdestinations WHERE site_address = ?");
            checkTransportDestinationsStatement.setString(1, address);
            rs = checkTransportDestinationsStatement.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return 3;
            }
            PreparedStatement checkSiteProductsStatement = conn.prepareStatement("SELECT COUNT(*) FROM siteproducts WHERE site_address = ?");
            checkSiteProductsStatement.setString(1, address);
            rs = checkSiteProductsStatement.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return 3;
            }
            PreparedStatement statement = conn.prepareStatement("DELETE FROM sites WHERE address = ?");
            statement.setString(1, address);
            int rowsDeleted = statement.executeUpdate();
            DataSource.closeConnection();
            return rowsDeleted > 0 ? 1 : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return 0;
    }


    public Site getSiteByAddress(String address) {
        conn = DataSource.openConnection();
        Site site = null;
        try {
            String sql = "SELECT * FROM sites WHERE address = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, address);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String contactNumber = rs.getString("contact_number");
                String contactName = rs.getString("contact_name");
                site = new Site(address, contactName, contactNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return site;
    }
    public boolean isSiteInShipmentArea(String address, String areaName) {
        conn = DataSource.openConnection();
        try {
            String sql = "SELECT * FROM sites WHERE address = ? AND area_name = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, address);
            statement.setString(2, areaName);
            ResultSet rs = statement.executeQuery();
            boolean exists = rs.next();
            DataSource.closeConnection();
            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

