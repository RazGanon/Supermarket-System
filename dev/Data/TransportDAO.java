package Data;

import Domain.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class TransportDAO { // singleton
    private Connection conn;
    private static TransportDAO single_instance = null;
    private HashMap<Integer, Transport> transportCache;

    private TransportDAO() {
        transportCache = new HashMap<>();
    }

    public static synchronized TransportDAO getInstance() {
        if (single_instance == null) {
            single_instance = new TransportDAO();
        }
        return single_instance;
    }

    public Transport getTransport(int transportId) {
        try {
            conn = DataSource.openConnection();
            Transport transport = transportCache.get(transportId);
            if (transport == null) {
                PreparedStatement statement = conn.prepareStatement("SELECT * FROM transports WHERE id = ?");
                statement.setInt(1, transportId);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    String truckLicenseNumber = rs.getString("truck_license_number");
                    int driverId = rs.getInt("driver_id");
                    String originAddress = rs.getString("origin_address_id");
                    int transportReportId = rs.getInt("transport_report_id");
                    String requestedTime = rs.getString("requested_time");
                    String requestDay = rs.getString("request_day");

                    Truck truck = TruckDAO.getInstance().getTruck(truckLicenseNumber);
                    Driver driver = DriverDAO.getInstance().getDriver(driverId);
                    Site originSite = getSiteByAddress(originAddress);
                    TransportReport transportReport = ReportsDAO.getInstance().getTransportReportById(transportReportId);

                    ArrayList<Site> destinations = getDestinationsByTransportId(transportId);
                    transport = new Transport(truck,driver,originSite,requestedTime,requestDay,destinations,transportReport);
                    transportCache.put(transportId, transport);
                }
            }
            DataSource.closeConnection();
            return transport;
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

    public boolean addTransport(Transport transport) {
        conn = DataSource.openConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO transports (truck_license_number, driver_id, origin_address, transport_report_id, requested_time, request_day) VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, transport.getTruck().getLicenseNumber());
            statement.setInt(2, transport.getDriver().getId());
            statement.setString(3, transport.getOriginAddress().getAddress());
            statement.setInt(4, transport.getTransportId());
            statement.setString(5, transport.getRequestedTime());
            statement.setString(6, transport.getRequestDay());


            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    int transportId = rs.getInt(1);
                    addTransportDestinations(transportId, transport.getDestinations());
                    transportCache.put(transportId, transport);
                    DataSource.closeConnection();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Transport> getAllTransports() {
        ArrayList<Transport> transports = new ArrayList<>();
        try {
            conn = DataSource.openConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM transports");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String licenseNumber = rs.getString("truck_license_number");
                int driverId = rs.getInt("driver_id");
                String originAddress = rs.getString("origin_address");
                String requestedTime = rs.getString("requested_time");
                String requestDay = rs.getString("request_day");


                Truck truck = TruckDAO.getInstance().getTruck(licenseNumber);
                Driver driver = DriverDAO.getInstance().getDriver(driverId);
                Site originSite = ShipmentAreaDAO.getInstance().getSiteByAddress(originAddress);
                TransportReport transportReport = ReportsDAO.getInstance().getTransportReportById(id);
                ArrayList<Site> destinations = getDestinationsByTransportId(id);

                Transport transport = new Transport(truck,driver,originSite,requestedTime,requestDay,destinations,transportReport);
                transports.add(transport);
            }
            DataSource.closeConnection();
            return transports;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean updateTransportTruck(Transport transport,String newTruckLicenseNumber) {
        conn = DataSource.openConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE transports SET truck_license_number = ?, driver_id = ?, origin_address = ?, transport_report_id = ?, requested_time = ?, request_day = ? WHERE id = ?");
            statement.setString(1, newTruckLicenseNumber);
            statement.setInt(2, transport.getDriver().getId());
            statement.setString(3, transport.getOriginAddress().getAddress());
            statement.setInt(4, transport.getTransportId());
            statement.setString(5, transport.getRequestedTime());
            statement.setString(6, transport.getRequestDay());
            statement.setInt(7, transport.getTransportId());

            int rowsUpdated = statement.executeUpdate();
            DataSource.closeConnection();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public ArrayList<Site> getDestinationsByTransportId(int transportId) {
        conn = DataSource.openConnection();
        ArrayList<Site> destinations = new ArrayList<>();
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT site_address FROM transportdestinations WHERE transport_id = ?");
            statement.setInt(1, transportId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String siteAddress = rs.getString("site_address");
                Site site = getSiteByAddress(siteAddress);
                destinations.add(site);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataSource.closeConnection();
        return destinations;
    }

   public boolean removeTransportDestinations(int transportId, ArrayList<Site> destinationsToRemove) {
        conn = DataSource.openConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM transportdestinations WHERE transport_id = ? AND site_address = ?");
            for (Site site : destinationsToRemove) {
                statement.setInt(1, transportId);
                statement.setString(2, site.getAddress());
                statement.addBatch();
            }
            int[] rowsDeleted = statement.executeBatch();
            for (int i : rowsDeleted) {
                if (i == 0) return false;  // Indicates that a deletion did not affect any rows
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return false;
    }

    public boolean addTransportDestinations(int transportId, ArrayList<Site> destinations) {
        conn = DataSource.openConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO transportdestinations (transport_id, site_address) VALUES (?, ?)");
            for (Site site : destinations) {
                statement.setInt(1, transportId);
                statement.setString(2, site.getAddress());
                statement.addBatch();
            }
            int[] rowsInserted = statement.executeBatch();
            for (int i : rowsInserted) {
                if (i == 0) return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return false;
    }

    private Site getSiteByAddress(String address) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM sites WHERE address = ?");
            statement.setString(1, address);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String contactNumber = rs.getString("contact_number");
                String contactName = rs.getString("contact_name");
                return new Site(address, contactName, contactNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
