package Data;

import java.sql.*;

public class DataSource {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/AR_TransportDB";
    private static final String USER = "shonplatok";
    private static Connection conn;

    public static Connection openConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(DB_URL, USER, null);
            }
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void resetDatabase() {
        conn = openConnection();
        try {
            Statement statement = conn.createStatement();

            // Drop all tables
            statement.executeUpdate("DROP TABLE IF EXISTS TransportDestinations CASCADE");
            statement.executeUpdate("DROP TABLE IF EXISTS Transports CASCADE");
            statement.executeUpdate("DROP TABLE IF EXISTS TransportReports CASCADE");
            statement.executeUpdate("DROP TABLE IF EXISTS SiteProducts CASCADE");
            statement.executeUpdate("DROP TABLE IF EXISTS Products CASCADE");
            statement.executeUpdate("DROP TABLE IF EXISTS Sites CASCADE");
            statement.executeUpdate("DROP TABLE IF EXISTS ShipmentAreas CASCADE");
            statement.executeUpdate("DROP TABLE IF EXISTS Trucks CASCADE");
            statement.executeUpdate("DROP TABLE IF EXISTS Drivers CASCADE");

            // Recreate all tables
            statement.executeUpdate("-- Create the ShipmentAreas table\n" +
                    "CREATE TABLE ShipmentAreas (\n" +
                    "    area_name VARCHAR(255) PRIMARY KEY\n" +
                    ");\n");

            statement.executeUpdate("-- Create the Sites table\n" +
                    "CREATE TABLE Sites (\n" +
                    "    address VARCHAR(255) NOT NULL PRIMARY KEY,\n" +
                    "    contact_number VARCHAR(50),\n" +
                    "    contact_name VARCHAR(255),\n" +
                    "    area_name VARCHAR(255) NOT NULL,\n" +
                    "    FOREIGN KEY (area_name) REFERENCES ShipmentAreas(area_name)\n" +
                    ");\n");

            statement.executeUpdate("-- Create the Drivers table\n" +
                    "CREATE TABLE Drivers (\n" +
                    "    id SERIAL PRIMARY KEY,\n" +
                    "    name VARCHAR(255) NOT NULL,\n" +
                    "    license_type VARCHAR(50),\n" +
                    "    available BOOLEAN DEFAULT TRUE\n" +
                    ");\n");

            statement.executeUpdate("-- Create the Trucks table\n" +
                    "CREATE TABLE Trucks (\n" +
                    "    license_number VARCHAR(255) NOT NULL PRIMARY KEY,\n" +
                    "    model VARCHAR(255) NOT NULL,\n" +
                    "    net_weight DECIMAL(10, 2),\n" +
                    "    max_weight DECIMAL(10, 2),\n" +
                    "    required_license VARCHAR(50),\n" +
                    "    available BOOLEAN DEFAULT TRUE\n" +
                    ");\n");

            statement.executeUpdate("-- Create the Products table\n" +
                    "CREATE TABLE Products (\n" +
                    "    id SERIAL PRIMARY KEY,\n" +
                    "    name VARCHAR(255) NOT NULL,\n" +
                    "    weight DECIMAL(10, 2) NOT NULL,\n" +
                    "    quantity INT NOT NULL\n" +
                    ");\n");

            statement.executeUpdate("-- Create the TransportReports table\n" +
                    "CREATE TABLE TransportReports (\n" +
                    "    id SERIAL PRIMARY KEY,\n" +
                    "    initial_weight DECIMAL(10, 2) NOT NULL,\n" +
                    "    changes_made TEXT,\n" +
                    "    source_site_address VARCHAR(255),\n" +
                    "    FOREIGN KEY (source_site_address) REFERENCES Sites(address)\n" +
                    ");\n");

            statement.executeUpdate("-- Create the SiteProducts table\n" +
                    "CREATE TABLE SiteProducts (\n" +
                    "    site_address VARCHAR(255) NOT NULL,\n" +
                    "    products JSONB NOT NULL,\n" +
                    "    transport_report_id INT,\n" +
                    "    PRIMARY KEY (site_address),\n" +
                    "    FOREIGN KEY (site_address) REFERENCES Sites(address),\n" +
                    "    FOREIGN KEY (transport_report_id) REFERENCES TransportReports(id)\n" +
                    ");\n");

            statement.executeUpdate("-- Create the Transports table\n" +
                    "CREATE TABLE Transports (\n" +
                    "    id SERIAL PRIMARY KEY,\n" +
                    "    truck_license_number VARCHAR(255) NOT NULL,\n" +
                    "    driver_id INT,\n" +
                    "    origin_address VARCHAR(255) NOT NULL,\n" +
                    "    transport_report_id INT,\n" +
                    "    requested_time VARCHAR(255) NOT NULL,\n" +
                    "    request_day VARCHAR(255) NOT NULL,\n" +
                    "    FOREIGN KEY (truck_license_number) REFERENCES Trucks(license_number),\n" +
                    "    FOREIGN KEY (driver_id) REFERENCES Drivers(id),\n" +
                    "    FOREIGN KEY (origin_address) REFERENCES Sites(address),\n" +
                    "    FOREIGN KEY (transport_report_id) REFERENCES TransportReports(id)\n" +
                    ");\n");

            statement.executeUpdate("-- Create the TransportDestinations table to hold transport destinations\n" +
                    "CREATE TABLE TransportDestinations (\n" +
                    "    transport_id INT,\n" +
                    "    site_address VARCHAR(255) NOT NULL,\n" +
                    "    PRIMARY KEY (transport_id, site_address),\n" +
                    "    FOREIGN KEY (transport_id) REFERENCES Transports(id),\n" +
                    "    FOREIGN KEY (site_address) REFERENCES Sites(address)\n" +
                    ");");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }


 

    public static void insertInitialData() {
        conn = openConnection();
        try {
            Statement statement = conn.createStatement();
            // Insert areas
            statement.executeUpdate("INSERT INTO ShipmentAreas (area_name) VALUES ('Area1')");
            statement.executeUpdate("INSERT INTO ShipmentAreas (area_name) VALUES ('Area2')");
            statement.executeUpdate("INSERT INTO ShipmentAreas (area_name) VALUES ('Area3')");
            statement.executeUpdate("INSERT INTO ShipmentAreas (area_name) VALUES ('Area4')");
            // Insert products

            // Insert initial data into Products
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductA', 3000, 1)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductB', 20.0, 3)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductC', 15.0, 2)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductD', 25.0, 4)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductE', 30.0, 1)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductF', 15.0, 2)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductG', 25.0, 5)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductH', 20.0, 1)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductI', 18.5, 3)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductJ', 22.5, 6)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductK', 10.0, 2)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductL', 25.0, 3)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductM', 30.0, 8)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductN', 10.0, 4)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductO', 20.0, 3)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductP', 15.0, 6)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductQ', 25.0, 7)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductR', 10.0, 1)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductS', 20.0, 5)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductT', 25.0, 4)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductU', 10.0, 6)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductV', 30.0, 2)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductW', 25.0, 1)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductX', 15.0, 3)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductY', 20.0, 3)");
            statement.executeUpdate("INSERT INTO Products (name, weight, quantity) VALUES ('ProductZ', 15.0, 2)");

            statement.executeUpdate("INSERT INTO Sites (address, contact_number, contact_name, area_name) VALUES ('123 Main St', '555-1234', 'John Doe', 'Area1')");
            statement.executeUpdate("INSERT INTO SiteProducts (site_address, products) VALUES ('123 Main St', '[{\"id\": 1, \"name\": \"ProductA\", \"weight\": 3000, \"quantity\": 2}, {\"id\": 2, \"name\": \"ProductB\", \"weight\": 20.0, \"quantity\": 3}]')");
            statement.executeUpdate("INSERT INTO Sites (address, contact_number, contact_name, area_name) VALUES ('456 Elm St', '555-5678', 'Jane Smith', 'Area1')");
            statement.executeUpdate("INSERT INTO SiteProducts (site_address, products) VALUES ('456 Elm St', '[{\"id\": 3, \"name\": \"ProductC\", \"weight\": 15.0, \"quantity\": 2}, {\"id\": 4, \"name\": \"ProductD\", \"weight\": 25.0, \"quantity\": 4}]')");
            statement.executeUpdate("INSERT INTO Sites (address, contact_number, contact_name, area_name) VALUES ('789 Oak St', '555-9012', 'Bob Johnson', 'Area1')");
            statement.executeUpdate("INSERT INTO SiteProducts (site_address, products) VALUES ('789 Oak St', '[{\"id\": 5, \"name\": \"ProductE\", \"weight\": 12.0, \"quantity\": 1}]')");
            statement.executeUpdate("INSERT INTO Sites (address, contact_number, contact_name, area_name) VALUES ('123 Origin St', '555-3434', 'Omer Topg', 'Area1')");
            statement.executeUpdate("INSERT INTO SiteProducts (site_address, products) VALUES ('123 Origin St', '[{\"id\": 6, \"name\": \"ProductF\", \"weight\": 10.0, \"quantity\": 2}, {\"id\": 7, \"name\": \"ProductG\", \"weight\": 15.0, \"quantity\": 5}]')");

            // Insert trucks
            statement.executeUpdate("INSERT INTO Trucks (license_number, model, net_weight, max_weight, required_license, available) VALUES ('ABC123', 'ModelX', 2000, 5000, 'B', true)");
            statement.executeUpdate("INSERT INTO Trucks (license_number, model, net_weight, max_weight, required_license, available) VALUES ('DEF456', 'ModelY', 2500, 6000, 'C', true)");
            statement.executeUpdate("INSERT INTO Trucks (license_number, model, net_weight, max_weight, required_license, available) VALUES ('GHI789', 'ModelZ', 3000, 7000, 'D', false)");
            statement.executeUpdate("INSERT INTO Trucks (license_number, model, net_weight, max_weight, required_license, available) VALUES ('JKL012', 'ModelA', 3500, 8000, 'E', true)");
            statement.executeUpdate("INSERT INTO Trucks (license_number, model, net_weight, max_weight, required_license, available) VALUES ('MNO345', 'ModelB', 1500, 4000, 'A', true)");
            statement.executeUpdate("INSERT INTO Trucks (license_number, model, net_weight, max_weight, required_license, available) VALUES ('PQR678', 'ModelC', 2000, 4500, 'B', true)");
            statement.executeUpdate("INSERT INTO Trucks (license_number, model, net_weight, max_weight, required_license, available) VALUES ('STU901', 'ModelD', 2500, 5000, 'C', true)");
            statement.executeUpdate("INSERT INTO Trucks (license_number, model, net_weight, max_weight, required_license, available) VALUES ('VWX234', 'ModelE', 3000, 5500, 'D', false)");
            statement.executeUpdate("INSERT INTO Trucks (license_number, model, net_weight, max_weight, required_license, available) VALUES ('YZA567', 'ModelF', 3500, 6000, 'E', true)");
            statement.executeUpdate("INSERT INTO Trucks (license_number, model, net_weight, max_weight, required_license, available) VALUES ('BCD890', 'ModelG', 1500, 3500, 'A', true)");
            statement.executeUpdate("INSERT INTO Trucks (license_number, model, net_weight, max_weight, required_license, available) VALUES ('EFG123', 'ModelH', 2000, 4000, 'B', true)");
            statement.executeUpdate("INSERT INTO Trucks (license_number, model, net_weight, max_weight, required_license, available) VALUES ('HIJ456', 'ModelI', 2500, 4500, 'C', true)");
            statement.executeUpdate("INSERT INTO Trucks (license_number, model, net_weight, max_weight, required_license, available) VALUES ('KLM789', 'ModelJ', 3000, 5000, 'D', false)");
            statement.executeUpdate("INSERT INTO Trucks (license_number, model, net_weight, max_weight, required_license, available) VALUES ('NOP012', 'ModelK', 3500, 5500, 'E', true)");

            // Insert drivers
            statement.executeUpdate("INSERT INTO Drivers (id, name, license_type, available) VALUES (1, 'Mor', 'C', true)");
            statement.executeUpdate("INSERT INTO Drivers (id, name, license_type, available) VALUES (2, 'Raziel', 'B', true)");
            statement.executeUpdate("INSERT INTO Drivers (id, name, license_type, available) VALUES (3, 'Sampson', 'C', true)");
            statement.executeUpdate("INSERT INTO Drivers (id, name, license_type, available) VALUES (4, 'Shon ha melech', 'D', true)");
            statement.executeUpdate("INSERT INTO Drivers (id, name, license_type, available) VALUES (5, 'Jesie', 'E', true)");
            statement.executeUpdate("INSERT INTO Drivers (id, name, license_type, available) VALUES (6, 'Yossi', 'A', true)");
            statement.executeUpdate("INSERT INTO Drivers (id, name, license_type, available) VALUES (7, 'Niv', 'B', true)");
            statement.executeUpdate("INSERT INTO Drivers (id, name, license_type, available) VALUES (8, 'John', 'C', true)");
            statement.executeUpdate("INSERT INTO Drivers (id, name, license_type, available) VALUES (9, 'Shani', 'D', true)");
            statement.executeUpdate("INSERT INTO Drivers (id, name, license_type, available) VALUES (10, 'Sharon', 'E', true)");
            statement.executeUpdate("INSERT INTO Drivers (id, name, license_type, available) VALUES (11, 'Daniel', 'A', true)");
            statement.executeUpdate("INSERT INTO Drivers (id, name, license_type, available) VALUES (12, 'Robert', 'B', true)");

            System.out.println("Initial data inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }


}
