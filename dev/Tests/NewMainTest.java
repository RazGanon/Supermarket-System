package Tests;

import Data.DataSource;
import Domain.*;
import Presentation.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import static org.junit.Assert.*;

public class NewMainTest {
    private Main main;
    private Gson gson;
    private MainController mainTransportController;

    @Before
    public void setUp() throws Exception {
        this.main = new Main();
        gson = new Gson();
        DataSource.resetDatabase();
        DataSource.insertInitialData();
        mainTransportController = MainController.getInstance();
    }

    @Test
    public void testUserExperience() {
        // Simulate adding a driver
        String input = "C\nYair Tesla\ntrue\n1122\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        main.addDriver(new Scanner(System.in));
        Driver driver = mainTransportController.getDriverById(1122);
        assertNotNull(driver);
        assertEquals("Yair Tesla", driver.getDriverName());
        assertTrue(out.toString().contains("Driver added successfully."));

        // Simulate listing all drivers
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        main.listAllDrivers();
        ArrayList<Driver> allDrivers = mainTransportController.getAllDrivers();
        assertNotNull(allDrivers);
        assertFalse(allDrivers.isEmpty());

        // Simulate adding a truck
        input = "XYZ789\nB\nModelX\n2000\n5000\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        main.addTruck(new Scanner(System.in));
        Truck truck = mainTransportController.getTruckByLicenseNumber("XYZ789");
        assertNotNull(truck);
        assertEquals("ModelX", truck.getModel());

        // Simulate listing all trucks
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        main.listAllTrucks();
        String allTrucksJson = mainTransportController.getAllTrucks();
        ArrayList<Truck> allTrucks = gson.fromJson(allTrucksJson, new TypeToken<ArrayList<Truck>>() {}.getType());
        assertNotNull(allTrucks);
        assertFalse(allTrucks.isEmpty());
        assertTrue(out.toString().contains("XYZ789"));

        // Simulate creating a transport
        input = "1\nABC123\n2024-07-12\n12:12\n123 Origin St\n789 Oak St\n1\ndone\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        main.createTransport(new Scanner(System.in));
        ArrayList<Transport> transports = mainTransportController.getAllTransports();
        assertFalse(transports.isEmpty());

        // Simulate listing all transports
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        main.listAllTransports();
        ArrayList<Transport> allTransports = mainTransportController.getAllTransports();
        assertNotNull(allTransports);
        assertFalse(allTransports.isEmpty());

        // Simulate updating driver availability
        input = "2\nfalse\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        main.updateDriverAvailability(new Scanner(System.in));
        driver = mainTransportController.getDriverById(2);
        assertNotNull(driver);
        assertFalse(driver.isAvailable());

        // Simulate updating truck availability
        input = "DEF456\nfalse\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        main.updateTruckAvailability(new Scanner(System.in));
        truck = mainTransportController.getTruckByLicenseNumber("DEF456");
        assertNotNull(truck);
        assertFalse(truck.isAvailable());

        // Simulate getting a transport report by ID
        input = "1\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        main.getTransportReportById(new Scanner(System.in));
        TransportReport transportReport = mainTransportController.getTransportReportById(1);
        assertEquals("TransportReport ID: 1, Initial Weight: " + transportReport.getInitialWeight() + ", Changes Made: " + transportReport.getChangesMade(), transportReport.toString());

        // Simulate removing a driver
        input = "3\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        main.removeDriver(new Scanner(System.in));
        driver = mainTransportController.getDriverById(3);
        assertNull(driver);

        input = "VWX234\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        main.removeTruck(new Scanner(System.in));
        truck = mainTransportController.getTruckByLicenseNumber("VWX234");
        assertNull(truck);


    }

    @Test
    public void testAddAndRemoveSiteFromArea() {
        Site site = new Site("987 New St", "Contact Person", "555-6789");
        String siteJson = gson.toJson(site);
        String areaName = "Area1";

        // Add site to area
        String result = mainTransportController.addSiteToArea(areaName, siteJson);
        assertEquals("Site added successfully.", result);

        // Check if the site was added
        String checkResult = mainTransportController.isSiteInShipmentArea("987 New St", areaName);
        assertEquals("Site exists in the shipment area " + areaName + ".", checkResult);

        // Remove site from area
        result = mainTransportController.removeSiteFromArea("987 New St",areaName);
        assertEquals("Site removed successfully from area " + areaName, result);

        // Check if the site was removed
        checkResult = mainTransportController.isSiteInShipmentArea("987 New St", areaName);
        assertEquals("Site does not exist in the shipment area " + areaName + ".", checkResult);
    }

    @Test
    public void testSetAndGetDriverAvailability() {
        // Set driver availability
        String result = mainTransportController.setDriverAvailability(1, false);
        assertEquals("Driver availability updated.", result);

        // Get driver and check availability
        Driver driver = mainTransportController.getDriverById(1);
        assertNotNull(driver);
        assertFalse(driver.isAvailable());

        // Set driver availability back to true
        result = mainTransportController.setDriverAvailability(1, true);
        assertEquals("Driver availability updated.", result);

        // Get driver and check availability
        driver = mainTransportController.getDriverById(1);
        assertNotNull(driver);
        assertTrue(driver.isAvailable());
    }

}
