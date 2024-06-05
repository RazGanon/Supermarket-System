import Domain.*;
import Service.ProductsReportService;
import Service.TransportService;
import Service.TruckService;
import Service.DriverService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CSVReader {
    public static void initializeData(String filePath, TruckService truckService, DriverService driverService, TransportService transportService, ProductsReportService productsReportService) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String section = "";
            ArrayList<Site> sites = new ArrayList<>();
            HashMap<Site, ProductsReport> productReports = new HashMap<>();
            while ((line = br.readLine()) != null) {
                if (line.equalsIgnoreCase("Sites:")) {
                    section = "Sites";
                    continue;
                } else if (line.equalsIgnoreCase("Trucks:")) {
                    section = "Trucks";
                    continue;
                } else if (line.equalsIgnoreCase("Drivers:")) {
                    section = "Drivers";
                    continue;
                }

                String[] values = line.split(",");
                switch (section) {
                    case "Sites":
                        String address = values[0];
                        String contactNumber = values[1];
                        String contactName = values[2];
                        ArrayList<Product> products = new ArrayList<>();
                        for (int i = 3; i < values.length - 1; i += 3) {
                            products.add(new Product(values[i], Integer.parseInt(values[i + 1]), Double.parseDouble(values[i + 2])));
                        }
                        double totalWeight = products.stream().mapToDouble(Product::getWeight).sum();
                        String areaName = values[values.length - 1];
                        Site site = new Site(address, contactName, contactNumber);
                        sites.add(site);
                        ProductsReport productsReport = new ProductsReport(products, totalWeight);
                        productsReportService.addProductsReport(productsReport);
                        productReports.put(site, productsReport);
                        transportService.addShipmentArea(new ShipmentArea(sites, areaName));
                        break;
                    case "Trucks":
                        String licenseNumber = values[0];
                        String requiredLicense = values[1];
                        String model = values[2];
                        double netWeight = Double.parseDouble(values[3]);
                        double maxWeight = Double.parseDouble(values[4]);
                        boolean available = Boolean.parseBoolean(values[5]);
                        Truck truck = new Truck(licenseNumber, model, netWeight, maxWeight, requiredLicense);
                        truck.setAvailable(available);
                        truckService.addTruck(truck);
                        break;
                    case "Drivers":
                        String licenseType = values[0];
                        String driverName = values[1];
                        boolean isAvailable = Boolean.parseBoolean(values[2]);
                        int id = Integer.parseInt(values[3]);
                        Driver driver = new Driver(licenseType, driverName, isAvailable, id);
                        driverService.addDriver(driver);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
