//package Presentation;
//
//import Domain.*;
//import Domain.ReportController;
//import Service.TransportService;
//import Domain.TruckController;
//import Domain.DriverController;
//import Domain.ShipmentAreaController;
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.util.ArrayList;
//
//public class CSVReader {
//
//    public static void initializeData(String filePath, TruckController truckController, DriverController driverController, TransportService transportService, ReportController reportController, ShipmentAreaController shipmentAreaController) throws Exception {
//        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            String section = "";
//
//            while ((line = br.readLine()) != null) {
//                if (line.equalsIgnoreCase("Areas:")) {
//                    section = "Areas";
//                    continue;
//                } else if (line.equalsIgnoreCase("Sites:")) {
//                    section = "Sites";
//                    continue;
//                } else if (line.equalsIgnoreCase("Trucks:")) {
//                    section = "Trucks";
//                    continue;
//                } else if (line.equalsIgnoreCase("Drivers:")) {
//                    section = "Drivers";
//                    continue;
//                }
//
//                String[] values = line.split(",");
//                for (int i = 0; i < values.length; i++) {
//                    values[i] = values[i].trim();
//                }
//
//                switch (section) {
//                    case "Areas":
//                        String areaName = values[0];
//                        shipmentAreaController.addShipmentArea(new ShipmentArea(new ArrayList<>(), areaName));
//                        break;
//
//                    case "Sites":
//                        String address = values[0];
//                        String contactNumber = values[1];
//                        String contactName = values[2];
//                        ArrayList<Product> products = new ArrayList<>();
//                        for (int i = 3; i < values.length - 1; i += 4) {
//                            products.add(new Product(values[i], Integer.parseInt(values[i + 1]), Double.parseDouble(values[i + 2]),Integer.parseInt(values[i+3])));
//                        }
//                        areaName = values[values.length - 1];
//                        Site site = new Site(address, contactName, contactNumber);
//
//                        shipmentAreaController.addSiteToArea(areaName, site);
//
//                        ProductsReport productsReport = new ProductsReport(products);
//                        reportController.addProductsReport(productsReport);
//                        SiteProducts siteProducts = new SiteProducts(site, productsReport);
//                        reportController.addSiteProductsReport(siteProducts);
//                        break;
//
//                    case "Trucks":
//                        String licenseNumber = values[0];
//                        String requiredLicense = values[1];
//                        String model = values[2];
//                        double netWeight = Double.parseDouble(values[3]);
//                        double maxWeight = Double.parseDouble(values[4]);
//                        boolean available = Boolean.parseBoolean(values[5]);
//                        Truck truck = new Truck(licenseNumber, model, netWeight, maxWeight, requiredLicense);
//                        truck.setAvailable(available);
//                        truckController.addTruck(truck);
//                        break;
//
//                    case "Drivers":
//                        String licenseType = values[0];
//                        String driverName = values[1];
//                        boolean isAvailable = Boolean.parseBoolean(values[2]);
//                        int id = Integer.parseInt(values[3]);
//                        Driver driver = new Driver(licenseType, driverName, isAvailable, id);
//                        driverController.addDriver(driver);
//                        break;
//                }
//            }
//        }
//    }
//}
