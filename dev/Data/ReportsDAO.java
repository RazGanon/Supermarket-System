package Data;

import Domain.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ReportsDAO { // Singleton
    private Connection conn;
    private static ReportsDAO single_instance = null;
    private HashMap<Integer, TransportReport> reportCache;

    private ReportsDAO() {
        reportCache = new HashMap<>();
    }

    public static ReportsDAO getInstance() {
        if (single_instance == null) {
            single_instance = new ReportsDAO();
        }
        return single_instance;
    }

    public TransportReport getTransportReportById(int reportId) {
        conn = DataSource.openConnection();
        TransportReport transportReport = reportCache.get(reportId);
        if (transportReport == null) {
            try {
                PreparedStatement statement = conn.prepareStatement("SELECT * FROM transportreports WHERE id = ?");
                statement.setInt(1, reportId);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    double initialWeight = rs.getDouble("initial_weight");
                    String changesMade = rs.getString("changes_made");
                    String sourceSiteAddress = rs.getString("source_site_address");
                    Site sourceSite = getSiteByAddress(sourceSiteAddress);
                    ArrayList<SiteProducts> siteProducts = getSiteProductsByReportId(reportId);
                    transportReport = new TransportReport(sourceSite, initialWeight, siteProducts);
                    transportReport.addChangesMade(changesMade);
                    reportCache.put(reportId, transportReport);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DataSource.closeConnection();
            }
        }
        return transportReport;
    }

    public boolean addTransportReport(TransportReport report) {
        conn = DataSource.openConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO transportreports (initial_weight, changes_made, source_site_address) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setDouble(1, report.getInitialWeight());
            statement.setString(2, report.getChangesMade());
            statement.setString(3, report.getSource().getAddress());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    int reportId = rs.getInt(1);
                    for (SiteProducts sp : report.getSiteAndProducts()) {
                        linkSiteProductsToReport(sp.getSite().getAddress(), reportId);
                    }
                    reportCache.put(reportId, report);
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return false;
    }

    public void linkSiteProductsToReport(String siteAddress, int reportId) {
        conn = DataSource.openConnection();
        try {
            String sql = "UPDATE siteproducts SET transport_report_id = ? WHERE site_address = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, reportId);
            statement.setString(2, siteAddress);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
    }

    public boolean updateTransportReport(int reportId, String changesMade) {
        conn = DataSource.openConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE transportReports SET changes_made = ? WHERE id = ?");
            statement.setString(1, changesMade);
            statement.setInt(2, reportId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                TransportReport report = reportCache.get(reportId);
                if (report != null) {
                    report.addChangesMade(changesMade);
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return false;
    }

    public boolean deleteTransportReport(int reportId) {
        conn = DataSource.openConnection();
        try {
            String sql = "DELETE FROM transportreports WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, reportId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                reportCache.remove(reportId);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return false;
    }

    public Site getSiteByAddress(String address) {
        conn = DataSource.openConnection();
        Site site = null;
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM sites WHERE address = ?");
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

    public ArrayList<SiteProducts> getSiteProductsByReportId(int reportId) {
        conn = DataSource.openConnection();
        ArrayList<SiteProducts> siteProductsList = new ArrayList<>();
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT site_address, products FROM siteproducts WHERE transport_report_id = ?");
            statement.setInt(1, reportId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String siteAddress = rs.getString("site_address");
                Site site = getSiteByAddress(siteAddress);
                String productsJson = rs.getString("products");
                ArrayList<Product> products = parseProductsJson(productsJson);
                SiteProducts siteProducts = new SiteProducts(site, products);
                siteProductsList.add(siteProducts);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return siteProductsList;
    }

    public boolean addSiteProducts(SiteProducts siteProducts) {
        conn = DataSource.openConnection();
        try {
            String sql = "INSERT INTO siteproducts (site_address, products) VALUES (?, ?::json) " +
                    "ON CONFLICT (site_address) DO UPDATE SET products = EXCLUDED.products";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, siteProducts.getSite().getAddress());
            statement.setString(2, convertProductsToJson(siteProducts.getProducts()));
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return false;
    }

    public SiteProducts getSiteProductsByAddress(String address) {
        conn = DataSource.openConnection();
        SiteProducts siteProducts = null;
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT site_address, products FROM siteproducts WHERE site_address = ?");
            statement.setString(1, address);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Site site = getSiteByAddress(address);
                String productsJson = rs.getString("products");
                ArrayList<Product> products = parseProductsJson(productsJson);
                siteProducts = new SiteProducts(site, products);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return siteProducts;
    }

    public ArrayList<SiteProducts> getAllSiteProducts() {
        conn = DataSource.openConnection();
        ArrayList<SiteProducts> siteProductsList = new ArrayList<>();
        try {
            String sql = "SELECT site_address, products FROM siteproducts";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String siteAddress = rs.getString("site_address");
                Site site = getSiteByAddress(siteAddress);
                String productsJson = rs.getString("products");
                ArrayList<Product> products = parseProductsJson(productsJson);
                SiteProducts siteProducts = new SiteProducts(site, products);
                siteProductsList.add(siteProducts);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return siteProductsList;
    }

    public boolean updateProductQuantity(String siteAddress, int productId, int newQuantity) {
        conn = DataSource.openConnection();
        try {
            String selectSql = "SELECT products FROM siteproducts WHERE site_address = ?";
            PreparedStatement selectStatement = conn.prepareStatement(selectSql);
            selectStatement.setString(1, siteAddress);
            ResultSet rs = selectStatement.executeQuery();

            if (rs.next()) {
                String productsJson = rs.getString("products");
                Gson gson = new Gson();
                Type productListType = new TypeToken<ArrayList<Product>>() {}.getType();
                ArrayList<Product> products = gson.fromJson(productsJson, productListType);

                for (Product product : products) {
                    if (product.getId() == productId) {
                        product.setQuantity(newQuantity);
                        break;
                    }
                }
                String updatedProductsJson = gson.toJson(products);
                String updateSql = "UPDATE siteproducts SET products = ?::jsonb WHERE site_address = ?";
                PreparedStatement updateStatement = conn.prepareStatement(updateSql);
                updateStatement.setString(1, updatedProductsJson);
                updateStatement.setString(2, siteAddress);
                int rowsUpdated = updateStatement.executeUpdate();
                return rowsUpdated > 0; // Returns true if the update was successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return false;
    }
    public boolean removeProductsFromSite(String siteAddress, int productId) {
        conn = DataSource.openConnection();
        try {
            // Retrieve the current products JSON
            PreparedStatement selectStmt = conn.prepareStatement(
                    "SELECT products FROM siteproducts WHERE site_address = ?"
            );
            selectStmt.setString(1, siteAddress);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                String productsJson = rs.getString("products");

                Gson gson = new Gson();
                Type productListType = new TypeToken<ArrayList<Product>>() {}.getType();
                ArrayList<Product> products = gson.fromJson(productsJson, productListType);
                products.removeIf(product -> product.getId() == productId);
                String updatedProductsJson = gson.toJson(products);
                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE siteproducts SET products = ?::jsonb WHERE site_address = ?"
                );
                updateStmt.setString(1, updatedProductsJson);
                updateStmt.setString(2, siteAddress);
                int rowsUpdated = updateStmt.executeUpdate();
                return rowsUpdated > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return false;
    }

    public boolean resetTransportReportId(String siteAddress) {
        conn = DataSource.openConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "UPDATE siteproducts SET transport_report_id = NULL WHERE site_address = ?"
            );
            statement.setString(1, siteAddress);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return false;
    }

    private String convertProductsToJson(ArrayList<Product> products) {
        Gson gson = new Gson();
        return gson.toJson(products);
    }


    private ArrayList<Product> parseProductsJson(String productsJson) {
        Gson gson = new Gson();
        Type productListType = new TypeToken<ArrayList<Product>>() {}.getType();
        return gson.fromJson(productsJson, productListType);
    }
}
