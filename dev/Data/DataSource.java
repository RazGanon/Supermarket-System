package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DataSource {

    private static final String DB_URL = "jdbc:sqlite:C:\\Users\\user\\Desktop\\github\\ADSS_Group_AR\\resources\\db\\mydatabase.db";
    private static final String DRIVER = "org.sqlite.JDBC";
    private static Connection conn = null;
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }


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

    public static void restoreSuperMarketStartingData() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            //SuperMarkets initial data
            restoreSuperMarketsData(stmt);


            System.out.println("Starting data has been restored.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void restoreSEmpStartingData() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            //Employee initial data
            restoreEmployeeData(stmt);


            System.out.println("Starting data has been restored.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void cleanTables() {
        String[] tables = {"ConstraintsTable", "schedule", "schedule_shift_employee", "Employee", "WeekFlag","SuperMarkets"};
        for (String table : tables) {
            cleanTable(table);
        }
    }


    public static void cleanTable(String table) {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM " + table);
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='" + table + "'"); // Reset auto-increment fields

            if (table.equals("Employee")) {
                // Add the manager employee if the Employee table is cleaned
                addManagerEmployee();
            }

            System.out.println(table + " table has been cleaned.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void addManagerEmployee() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            String sql = "INSERT INTO Employee (ID, Fname, Lname, Salary, Job, Address, Manager_Name, Role, hire_date, days_off, password) " +
                    "VALUES (1234, 'mor', 'barzilay', 10000, 'full-Time job', 'tel-aviv', 'mor', 'Manager', '01-07-2024', 2, 1234)";
            stmt.executeUpdate(sql);
            System.out.println("Manager employee has been added to the Employee table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //buid this two function for self testing- to restore the initial data to db.
    private static void restoreEmployeeData(Statement stmt) throws SQLException {
        stmt.executeUpdate("INSERT INTO Employee (ID, Fname, Lname, Salary, Job, Address, Manager_Name, Role, hire_date, days_off, password) " +
                "VALUES (107643290, 'sara', 'jones', 7000, 10, 'tel-aviv', 'worker', 'Manager', '01-07-2024', 4, 1111)");
        stmt.executeUpdate("INSERT INTO Employee (ID, Fname, Lname, Salary, Job, Address, Manager_Name, Role, hire_date, days_off, password) " +
                "VALUES (156489302, 'mary', 'smith', 6500, 5, 'beersheba', 'worker', 'Employee', '01-07-2024', 6, 1111)");
        stmt.executeUpdate("INSERT INTO Employee (ID, Fname, Lname, Salary, Job, Address, Manager_Name, Role, hire_date, days_off, password) " +
                "VALUES (176345092, 'jane', 'doe', 7500, 12, 'netanya', 'worker', 'Employee', '01-07-2024', 7, 1111)");
        stmt.executeUpdate("INSERT INTO Employee (ID, Fname, Lname, Salary, Job, Address, Manager_Name, Role, hire_date, days_off, password) " +
                "VALUES (198745612, 'john', 'doe', 8000, 12, 'netanya', 'worker', 'Employee', '01-07-2024', 5, 1111)");
        stmt.executeUpdate("INSERT INTO Employee (ID, Fname, Lname, Salary, Job, Address, Manager_Name, Role, hire_date, days_off, password) " +
                "VALUES (198754320, 'paul', 'white', 6800, 6, 'ashdod', 'worker', 'Employee', '01-07-2024', 8, 1111)");
        stmt.executeUpdate("INSERT INTO Employee (ID, Fname, Lname, Salary, Job, Address, Manager_Name, Role, hire_date, days_off, password) " +
                "VALUES (208398511, 'niv', 'sampson', 5000, 13, 'tel-aviv', 'worker', 'Employee', '01-07-2024', 3, 1111)");
        stmt.executeUpdate("INSERT INTO Employee (ID, Fname, Lname, Salary, Job, Address, Manager_Name, Role, hire_date, days_off, password) " +
                "VALUES (313322380, 'mor', 'barzilay', 10000, 13, 'tel-aviv', 'worker', 'Manager', '01-07-2024', 2, 2603)");
        stmt.executeUpdate("INSERT INTO Employee (ID, Fname, Lname, Salary, Job, Address, Manager_Name, Role, hire_date, days_off, password) " +
                "VALUES (208, 'mor', 'moris', 208, 0, 'tel-aviv', 'Full-Time Job', 'Employee', '01-07-2024', 12, 1111)");
    }

    private static void restoreSuperMarketsData(Statement stmt) throws SQLException {
        stmt.executeUpdate("INSERT INTO SuperMarkets (address, manager_name) VALUES ('tel-aviv', 'mor')");
        stmt.executeUpdate("INSERT INTO SuperMarkets (address, manager_name) VALUES ('haifa', 'jane')");
        stmt.executeUpdate("INSERT INTO SuperMarkets (address, manager_name) VALUES ('jerusalem', 'jane')");
        stmt.executeUpdate("INSERT INTO SuperMarkets (address, manager_name) VALUES ('beersheba', 'mark')");
        stmt.executeUpdate("INSERT INTO SuperMarkets (address, manager_name) VALUES ('netanya', 'mark')");
        stmt.executeUpdate("INSERT INTO SuperMarkets (address, manager_name) VALUES ('ashdod', 'anna')");
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
