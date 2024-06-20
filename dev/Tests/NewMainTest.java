package Tests;
import Domain.*;
import Presentation.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

public class NewMainTest {
    private TransportController transportController;
    private Main main;
    private Gson gson;

    @Before
    public void setUp() throws Exception {
        main = new Main();
        // Initialize controllers
        transportController = TransportController.getInstance();
        gson = new Gson();

        // Path to the CSV file
        String filePath = "data.csv";

        // Initialize data from CSV file
        CSVReader.initializeData(filePath, transportController.getTruckService(), transportController.getDriverService(), transportController.getTransportService(), transportController.getReportService(), transportController.getShipmentAreaService());
    }

    @Test
    public void testUserExperience() {
        // Simulate adding a driver
        String input = "E\nYair Tesla\ntrue\n3183342456\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        main.addDriver(new Scanner(System.in));
        Driver driver = gson.fromJson(transportController.getDriverById(3183342456L), Driver.class);
        assertNotNull(driver);
        assertEquals("Yair Tesla", driver.getDriverName());
        assertTrue(out.toString().contains("Driver added successfully."));

        // Simulate listing all drivers
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        main.listAllDrivers();
        String allDriversJson = transportController.getAllDrivers();
        ArrayList<Driver> allDrivers = gson.fromJson(allDriversJson, new TypeToken<ArrayList<Driver>>() {}.getType());
        assertNotNull(allDrivers);
        assertFalse(allDrivers.isEmpty());
        assertTrue(out.toString().contains("Yair Tesla"));

        // Simulate adding a truck
        input = "XYZ789\nB\nModelX\n2000\n5000\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        main.addTruck(new Scanner(System.in));
        Truck truck = gson.fromJson(transportController.getTruckByLicenseNumber("XYZ789"), Truck.class);
        assertNotNull(truck);
        assertEquals("ModelX", truck.getModel());

        // Simulate listing all trucks
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        main.listAllTrucks();
        String allTrucksJson = transportController.getAllTrucks();
        ArrayList<Truck> allTrucks = gson.fromJson(allTrucksJson, new TypeToken<ArrayList<Truck>>() {}.getType());
        assertNotNull(allTrucks);
        assertFalse(allTrucks.isEmpty());
        assertTrue(out.toString().contains("XYZ789"));

        // Simulate creating a transport
        input = "1\nABC123\n2024-07-12\n12:12\n123 Origin St\n123 Main St\n1\ndone\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        main.createTransport(new Scanner(System.in));
        ArrayList<Transport> transports = gson.fromJson(transportController.getAllTransports(), new TypeToken<ArrayList<Transport>>() {}.getType());
        assertFalse(transports.isEmpty());

        // Simulate listing all transports
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        main.listAllTransports();
        String allTransportsJson = transportController.getAllTransports();
        ArrayList<Transport> allTransports = gson.fromJson(allTransportsJson, new TypeToken<ArrayList<Transport>>() {}.getType());
        assertNotNull(allTransports);
        assertFalse(allTransports.isEmpty());

        // Simulate changing the truck for an existing transport
        int transportId = transports.get(0).getTransportId();
        input = transportId + "\nTruck change\n";
        System.out.println("Input for changeTransportTruck: \n" + input); // Print the input for debugging
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        main.changeTransportTruck(new Scanner(System.in));

        Transport updatedTransport = gson.fromJson(transportController.getTransportById(transportId), Transport.class);
        System.out.println("Changes Made after Truck change: " + updatedTransport.getTsp().getChangesMade());
        assertEquals("Truck change", updatedTransport.getTsp().getChangesMade());

        // Simulate changing the destination for an existing transport
        input = transportId + "\n123 Main St\n789 Oak St\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        main.changeDestination(new Scanner(System.in));
        Transport transport = gson.fromJson(transportController.getTransportById(transportId), Transport.class);
        Site site1 = new Site("123 Main St", "John Doe", "555-1234");
        Site site2 = new Site("789 Oak St", "Bob Johnson", "555-9012");
        assertFalse(transport.getDestinations().contains(site1));
        assertTrue(transport.getDestinations().contains(site2));

        // Simulate removing a destination from an existing transport
        input = transportId + "\n789 Oak St\ndone\nRemove destination\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        main.removeDestinations(new Scanner(System.in));
        transport = gson.fromJson(transportController.getTransportById(transportId), Transport.class);
        assertFalse(transport.getDestinations().contains(site2));

        // Simulate updating driver availability
        input = "2\nfalse\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        main.updateDriverAvailability(new Scanner(System.in));
        driver = gson.fromJson(transportController.getDriverById(2L), Driver.class);
        assertNotNull(driver);
        assertFalse(driver.isAvailable());

        // Simulate updating truck availability
        input = "DEF456\nfalse\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        main.updateTruckAvailability(new Scanner(System.in));
        truck = gson.fromJson(transportController.getTruckByLicenseNumber("DEF456"), Truck.class);
        assertNotNull(truck);
        assertFalse(truck.isAvailable());

        // Simulate getting a products report by ID
        input = "1\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        main.getProductsReportById(new Scanner(System.in));
        ProductsReport report = gson.fromJson(transportController.getProductsReportById(1), ProductsReport.class);
        assertEquals(report.toString(),"ProductsReport ID: 1\nProducts: ProductA (ID: 1, Weight: 10.5), ProductB (ID: 2, Weight: 20.0)\nWeight: 30.5\n");

        // Simulate getting a transport report by ID
        input = "15\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        main.getTransportReportById(new Scanner(System.in));
        TransportReport transportReport = gson.fromJson(transportController.getTransportReportById(15), TransportReport.class);
        assertEquals(transportReport.toString(),"TransportReport ID: 15,Initial Weight: " + transportReport.getInitialWeight() + ",Changes Made: " + transportReport.getChangesMade());

        // Simulate removing a driver
        input = "1\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        main.removeDriver(new Scanner(System.in));
        driver = gson.fromJson(transportController.getDriverById(1L), Driver.class);
        assertNull(driver);

        // Simulate removing a truck
        input = "ABC123\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        main.removeTruck(new Scanner(System.in));
        truck = gson.fromJson(transportController.getTruckByLicenseNumber("ABC123"), Truck.class);
        assertNull(truck);

        // Add a site product report for testing
        Site site = new Site("123 Main St", "John Doe", "555-1234");
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("ProductA", 1, 10.5,2));
        ProductsReport productsReport = new ProductsReport(products);
        SiteProductsReport siteProductsReport = new SiteProductsReport(site, productsReport);
        transportController.addSiteProductsReport(gson.toJson(siteProductsReport));

        // Simulate user input for getProductReportBySite
        input = "123 Main St\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Capture system output for getProductReportBySite
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        // Call the method getProductReportBySite
        main.getProductReportBySite(new Scanner(System.in));

        // Verify output
        String output = out.toString();
        assertTrue(output.contains("ProductA"));
    }
}
