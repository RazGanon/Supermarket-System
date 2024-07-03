package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

    private static final String DB_URL = "jdbc:sqlite:C:/devop/projects/ADSS-AR/DB/mydatabase.db";
    private static final String DRIVER = "org.sqlite.JDBC";
    private static Connection conn = null;

    public static Connection openConnection() {
        try {
            // Register the JDBC driver
            Class.forName(DRIVER);

            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(DB_URL);
            }
            return conn;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("JDBC Driver not found.");
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection failed.");
            return null;
        }
    }

    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connection closed successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
