//package Tests;
//
//import Domain.*;
//import Service.*;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import static org.junit.Assert.*;
//
//public class TransportServiceTest {
//
//    private TransportService transportService;
//    private TruckService truckService;
//    private DriverService driverService;
//
//    @Before
//    public void setUp() {
//        transportService = new TransportService();
//        truckService = new TruckService();
//        driverService = new DriverService();
//
//        // Initialize data
//        initializeData();
//    }
//
//    private void initializeData() {
//        // Initialize sites and products
//        Site site1 = new Site("123 Main St", "John Doe", "555-1234");
//        Site site2 = new Site("456 Elm St", "Jane Smith", "555-5678");
//        Site site3 = new Site("789 Oak St", "Bob Johnson", "555-9012");
//
//        Product productA = new Product("ProductA", 1, 300);
//        Product productB = new Product("ProductB", 2, 400);
//        Product productC = new Product("ProductC", 3, 100);
//        Product productD = new Product("ProductD", 4, 250);
//        Product productE = new Product("ProductE", 5, 120);
//
//        ArrayList<Product> products1 = new ArrayList<>();
//        products1.add(productA);
//        products1.add(productB);
//        ProductsReport productsReport1 = new ProductsReport(products1);
//
//        ArrayList<Product> products2 = new ArrayList<>();
//        products2.add(productC);
//        products2.add(productD);
//        ProductsReport productsReport2 = new ProductsReport(products2);
//
//        ArrayList<Product> products3 = new ArrayList<>();
//        products3.add(productE);
//        ProductsReport productsReport3 = new ProductsReport(products3);
//
//        // Add products reports to a suitable storage if needed
//        // For this test, assume you have a method to store these reports, e.g., in a map
//
//        // Initialize trucks
//        Truck truck1 = new Truck("ABC123","ModelX" ,2000 , 5000, "B");
//        Truck truck2 = new Truck("DEF456", "ModelY",2500 , 6000, "C");
//        Truck truck3 = new Truck("GHI789", "ModelZ", 3000, 7000,"D");
//        Truck truck4 = new Truck("JKL012", "ModelA",3500.0, 8000,"E");
//
//        truckService.addTruck(truck1);
//        truckService.addTruck(truck2);
//        truckService.addTruck(truck3);
//        truckService.addTruck(truck4);
//
//        // Initialize drivers
//        Driver driver1 = new Driver("A", "Mor", true, 1);
//        Driver driver2 = new Driver("B", "Raziel", true, 2);
//        Driver driver3 = new Driver("C", "Sampson", true, 3);
//        Driver driver4 = new Driver("D", "Shon ha melech", true, 4);
//        Driver driver5 = new Driver("E", "Jesie", true, 5);
//
//        driverService.addDriver(driver1);
//        driverService.addDriver(driver2);
//        driverService.addDriver(driver3);
//        driverService.addDriver(driver4);
//        driverService.addDriver(driver5);
//    }
//
//    private Site getSiteByName(String name) {
//        // Implement a method to retrieve site by name
//        switch (name) {
//            case "123 Main St":
//                return new Site("123 Main St", "John Doe", "555-1234");
//            case "456 Elm St":
//                return new Site("456 Elm St", "Jane Smith", "555-5678");
//            case "789 Oak St":
//                return new Site("789 Oak St", "Bob Johnson", "555-9012");
//            default:
//                return null;
//        }
//    }
//
//    private ProductsReport getProductsReportById(int id) {
//        // Implement a method to retrieve products report by ID
//        switch (id) {
//            case 1:
//                return new ProductsReport(new ArrayList<Product>() {{
//                    add(new Product("ProductA", 1, 3000));
//                    add(new Product("ProductB", 2, 250));
//                }});
//            case 2:
//                return new ProductsReport(new ArrayList<Product>() {{
//                    add(new Product("ProductC", 3, 2000));
//                    add(new Product("ProductD", 4, 25.0));
//                }});
//            case 3:
//                return new ProductsReport(new ArrayList<Product>() {{
//                    add(new Product("ProductE", 5, 30.0));
//                }});
//            default:
//                return null;
//        }
//    }
//
//    @Test(expected = Exception.class)
//    public void testCreateTransportWithNonExistentDriver() throws Exception {
//        // Non-existent driver ID 999
//        TransportRequest request = new TransportRequest(
//                LocalDate.now(),
//                LocalTime.now(),
//                new Site("123 Main St", "", ""),
//                new ArrayList<>(),
//                new HashMap<>(),
//                "ABC123",
//                999
//        );
//        transportService.createTransport(request, truckService, driverService);
//    }
//
//    @Test(expected = Exception.class)
//    public void testCreateTransportWithNoAvailableTruck() throws Exception {
//        // No available truck for the given license number
//        TransportRequest request = new TransportRequest(
//                LocalDate.now(),
//                LocalTime.now(),
//                new Site("123 Main St", "", ""),
//                new ArrayList<>(),
//                new HashMap<>(),
//                "NONEXISTENT_TRUCK",
//                1
//        );
//        transportService.createTransport(request, truckService, driverService);
//    }
//
//    @Test(expected = Exception.class)
//    public void testCreateTransportWithMismatchedDriverLicense() throws Exception {
//        // Driver with a license that cannot handle the weight
//        HashMap<Site, ProductsReport> productReports = new HashMap<>();
//        productReports.put(getSiteByName("123 Main St"), getProductsReportById(1));
//        productReports.put(getSiteByName("456 Elm St"), getProductsReportById(2));
//
//        TransportRequest request = new TransportRequest(
//                LocalDate.now(),
//                LocalTime.now(),
//                new Site("123 Main St", "", ""),
//                new ArrayList<>(),
//                productReports,
//                "ABC123",
//                1
//        );
//        transportService.createTransport(request, truckService, driverService);
//    }
//
//    @Test(expected = Exception.class)
//    public void testCreateTransportWithTruckUnableToHandleWeight() throws Exception {
//        // Truck unable to handle the transport weight
//        HashMap<Site, ProductsReport> productReports = new HashMap<>();
//        productReports.put(getSiteByName("123 Main St"), getProductsReportById(1));
//        productReports.put(getSiteByName("456 Elm St"), getProductsReportById(2));
//
//        TransportRequest request = new TransportRequest(
//                LocalDate.now(),
//                LocalTime.now(),
//                new Site("123 Main St", "", ""),
//                new ArrayList<>(),
//                productReports,
//                "ABC123",
//                5
//        );
//        transportService.createTransport(request, truckService, driverService);
//    }
//
//    @Test
//    public void testSuccessfulTransportCreation() throws Exception {
//        // Successful transport creation
//        HashMap<Site, ProductsReport> productReports = new HashMap<>();
//        productReports.put(getSiteByName("123 Main St"), getProductsReportById(1));
//
//        TransportRequest request = new TransportRequest(
//                LocalDate.now(),
//                LocalTime.now(),
//                new Site("123 Main St", "", ""),
//                new ArrayList<>(),
//                productReports,
//                "ABC123",
//                5
//        );
//        Transport transport = transportService.createTransport(request, truckService, driverService);
//        assertNotNull(transport);
//        assertEquals("ABC123", transport.getTruck().getLicenseNumber());
//        assertEquals(5, transport.getDriver().getId());
//    }
//}
