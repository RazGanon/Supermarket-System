package Presentation;

import com.google.gson.Gson;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Scanner;
import Domain.*;
import com.google.gson.reflect.TypeToken;

public class Main {
    private static Gson gson = new Gson();
    private static TransportController transportController = TransportController.getInstance();

    public static void main(String[] args) {
        if (args.length > 0) {
            String filePath = args[0];
            try {
                initializeData(filePath);
            } catch (Exception e) {
                System.out.println("Error initializing data: " + e.getMessage());
            }
        } else {
            String filePath = "data.csv";
            try {
                initializeData(filePath);
            } catch (Exception e) {
                System.out.println("Error initializing data: " + e.getMessage());
            }
        }
        Scanner scanner = new Scanner(System.in);
        while (true) {
            showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1:
                    createTransport(scanner);
                    break;
                case 2:
                    listAllDrivers();
                    break;
                case 3:
                    addDriver(scanner);
                    break;
                case 4:
                    removeDriver(scanner);
                    break;
                case 5:
                    updateDriverAvailability(scanner);
                    break;
                case 6:
                    listAllTrucks();
                    break;
                case 7:
                    addTruck(scanner);
                    break;
                case 8:
                    removeTruck(scanner);
                    break;
                case 9:
                    updateTruckAvailability(scanner);
                    break;
                case 10:
                    listAllProductsReports();
                    break;
                case 11:
                    listAllTransports();
                    break;
                case 12:
                    changeTransportTruck(scanner);
                    break;
                case 13:
                    removeDestinations(scanner);
                    break;
                case 14:
                    changeDestination(scanner);
                    break;
                case 15:
                    getProductsReportById(scanner);
                    break;
                case 16:
                    getTransportReportById(scanner);
                    break;
                case 17:
                    getProductReportBySite(scanner);
                    break;
                case 18:
                    addSiteToShipmentArea(scanner);
                    break;
                case 19:
                    removeSiteFromShipmentArea(scanner);
                    break;
                case 20:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void showMenu() {
        System.out.println("Menu:");
        System.out.println("1. Create Transport");
        System.out.println("2. List All Drivers");
        System.out.println("3. Add Driver");
        System.out.println("4. Remove Driver");
        System.out.println("5. Update Driver Availability");
        System.out.println("6. List All Trucks");
        System.out.println("7. Add Truck");
        System.out.println("8. Remove Truck");
        System.out.println("9. Update Truck Availability");
        System.out.println("10. List All Products Reports");
        System.out.println("11. List All Transports");
        System.out.println("12. Change Transport Truck");
        System.out.println("13. Remove Destinations");
        System.out.println("14. Change Destination");
        System.out.println("15. Get Products Report By Id");
        System.out.println("16. Get Transport Report By Id");
        System.out.println("17. Get Product Report By Site");
        System.out.println("18. Add Site To Selected Shipment Area");
        System.out.println("19. Remove Site From Selected Shipment Area");
        System.out.println("20. Exit");
        System.out.print("Enter your choice: ");
    }

    public static void createTransport(Scanner scanner) {
        System.out.println("Enter Transport Request details:");
        System.out.print("Enter Driver ID: ");
        int driverId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Truck License Number: ");
        String truckLicenseNumber = scanner.nextLine();
        System.out.print("Enter Request Day (yyyy-MM-dd): ");
        String requestDay = scanner.nextLine();
        System.out.print("Enter Requested Time (HH:mm): ");
        String requestedTime = scanner.nextLine();
        System.out.print("Enter Origin Address: ");
        String originAddress = scanner.nextLine();

        ArrayList<Site> destinationAddresses = new ArrayList<>();
        ArrayList<SiteProductsReport> siteAndProducts = new ArrayList<>();

        while (true) {
            System.out.print("Enter Site Name (or 'done' to finish): ");
            String siteName = scanner.nextLine();
            if (siteName.equalsIgnoreCase("done")) {
                break;
            }

            // Get the products report for the site
            String siteAndProductsJson = transportController.getAllSiteProductsReports();
            Type listType = new TypeToken<ArrayList<SiteProductsReport>>() {}.getType();
            ArrayList<SiteProductsReport> siteProductsReports = gson.fromJson(siteAndProductsJson, listType);

            boolean siteFound = false;
            for (SiteProductsReport spr : siteProductsReports) {
                if (spr.getSite().getAddress().equals(siteName)) {
                    destinationAddresses.add(spr.getSite());
                    siteAndProducts.add(spr);
                    siteFound = true;
                    break;
                }
            }

            if (!siteFound) {
                System.out.println("Products report for site " + siteName + " not found.");
            }
        }

        TransportRequest transportRequest = new TransportRequest(
                requestDay,
                requestedTime,
                new Site(originAddress, "", ""),
                siteAndProducts,
                truckLicenseNumber,
                driverId,
                destinationAddresses
        );

        String json = gson.toJson(transportRequest);
        String result = transportController.createTransport(json);
        System.out.println(result);
    }

    public static void addDriver(Scanner scanner) {
        System.out.print("Enter Driver license Type: ");
        String licenseType = scanner.nextLine();
        System.out.print("Enter Driver Name: ");
        String driverName = scanner.nextLine();
        System.out.print("Is Driver Currently Available (Please Enter - (true/false)) : ");
        String availabilityStr = scanner.nextLine();
        boolean availability = Boolean.parseBoolean(availabilityStr); // Convert from String to boolean
        System.out.print("Enter Driver ID: ");
        long driverId = scanner.nextLong();
        scanner.nextLine(); // Consume new line
        Driver newDriver = new Driver(licenseType, driverName, availability, driverId);
        String json = gson.toJson(newDriver);
        String result = transportController.addDriver(json);
        System.out.println(result);
    }

    public static void removeDriver(Scanner scanner) {
        System.out.print("Enter Driver ID To Remove: ");
        long driverId = scanner.nextLong();
        scanner.nextLine(); // Consume new line
        String result = transportController.removeDriver(driverId);
        if (result != null) {
            System.out.println(result);
        } else {
            System.out.println("No Driver Matching The ID Given");
        }
    }
    public static void addSiteToShipmentArea(Scanner scanner) {
        try {
            System.out.print("Enter Area Name: ");
            String areaName = scanner.nextLine();
            System.out.print("Enter Site Address: ");
            String address = scanner.nextLine();
            System.out.print("Enter Contact Number: ");
            String contactNumber = scanner.nextLine();
            System.out.print("Enter Contact Name: ");
            String contactName = scanner.nextLine();
            Site newSite = new Site(address, contactName, contactNumber);
            String newSiteGson = gson.toJson(newSite);

            String response = transportController.addSiteToArea(areaName, newSiteGson);
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Error adding site: " + e.getMessage());
        }
    }
    public static void updateDriverAvailability(Scanner scanner) {
        System.out.print("Enter Driver ID: ");
        long driverId = scanner.nextLong();
        scanner.nextLine(); // Consume newline
        System.out.print("Is Driver Currently Available (Please Enter - (true/false)) : ");
        boolean available = scanner.nextBoolean();
        scanner.nextLine(); // Consume newline

        String result = transportController.setDriverAvailability(driverId, available);
        System.out.println(result);
    }

    public static void listAllDrivers() {
        String allDriversJson = transportController.getAllDrivers();
        ArrayList<Driver> allDrivers = gson.fromJson(allDriversJson, new com.google.gson.reflect.TypeToken<ArrayList<Driver>>() {}.getType());
        for (Driver driver : allDrivers) {
            System.out.println(driver);
        }
    }

    public static void listAllTrucks() {
        String allTrucksJson = transportController.getAllTrucks();
        ArrayList<Truck> allTrucks = gson.fromJson(allTrucksJson, new com.google.gson.reflect.TypeToken<ArrayList<Truck>>() {}.getType());
        for (Truck truck : allTrucks) {
            System.out.println(truck);
        }
    }

    public static void addTruck(Scanner scanner) {
        System.out.print("Enter Truck license Number: ");
        String licenseNumber = scanner.nextLine();
        System.out.print("Enter Required License For Truck: ");
        String requiredLicenseType = scanner.nextLine();
        System.out.print("Enter Truck Model: ");
        String truckModel = scanner.nextLine();
        System.out.print("Enter Truck Net Weight: ");
        double truckNetWeight = scanner.nextDouble();
        scanner.nextLine(); // Consume new line
        System.out.print("Enter Truck Max Weight: ");
        double truckMaxWeight = scanner.nextDouble();
        scanner.nextLine();
        Truck newTruck = new Truck(licenseNumber, truckModel, truckNetWeight, truckMaxWeight, requiredLicenseType);
        String json = gson.toJson(newTruck);
        String result = transportController.addTruck(json);
        System.out.println(result);
    }

    public static void removeTruck(Scanner scanner) {
        System.out.print("Enter Truck To Remove license Number: ");
        String licenseNumber = scanner.nextLine();
        String result = transportController.removeTruck(licenseNumber);
        System.out.println(result);
    }

    public static void updateTruckAvailability(Scanner scanner) {
        System.out.print("Enter Truck License Number: ");
        String licenseNumber = scanner.nextLine();
        System.out.print("Is Truck Currently Available (Please Enter - (true/false)) : ");
        boolean available = scanner.nextBoolean();
        scanner.nextLine(); // Consume newline

        String result =transportController.setTruckAvailability(licenseNumber, available);
        System.out.println(result);
    }

    public static void listAllProductsReports() {
        String allProductsReportsJson = transportController.getAllProductsReports();
        ArrayList<ProductsReport> allProductsReports = gson.fromJson(allProductsReportsJson, new com.google.gson.reflect.TypeToken<ArrayList<ProductsReport>>() {}.getType());
        for (ProductsReport report : allProductsReports) {
            System.out.println(report.toString());
        }
    }

    public static void listAllTransports() {
        String allTransportsJson = transportController.getAllTransports();
        ArrayList<Transport> allTransports = gson.fromJson(allTransportsJson, new com.google.gson.reflect.TypeToken<ArrayList<Transport>>() {}.getType());
        for (Transport transport : allTransports) {
            System.out.println("Transport ID: " + transport.getTransportId());
            System.out.println("Transport Report: " + transport.getTsp());
            System.out.println("-------------------------");
        }
    }

    public static void changeTransportTruck(Scanner scanner) {
        try {
            System.out.print("Enter Transport ID: ");
            int transportId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter changes made: ");
            String changes = scanner.nextLine();
            String transportGson = transportController.getTransportById(transportId);
            Transport transport = gson.fromJson(transportGson,Transport.class);
            String result = transportController.changeTransportTruck(transport.getTransportId(), changes);

            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Error changing transport truck: " + e.getMessage());
        }
    }

    public static void removeDestinations(Scanner scanner) {
        try {
            System.out.print("Enter Transport ID: ");
            int transportId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            ArrayList<Site> sitesToRemove = new ArrayList<>();
            while (true) {
                System.out.print("Enter Site Name to Remove (or 'done' to finish): ");
                String siteName = scanner.nextLine();
                if (siteName.equalsIgnoreCase("done")) {
                    break;
                }
                ArrayList<ShipmentArea> shipmentAreas =  transportController.getShipmentAreaService().getShipmentAreas();
                for(ShipmentArea shipmentArea : shipmentAreas){
                    for (Site site : shipmentArea.getSites()){
                        if (siteName.equals(site.getAddress())){
                            sitesToRemove.add(site);
                        }
                    }
                }

            }

            String sitesJson = gson.toJson(sitesToRemove);
            System.out.print("Enter changes made: ");
            String changes = scanner.nextLine();
            String result = transportController.removeDestinationsFromTransport(transportId, sitesJson, changes);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Error removing destinations: " + e.getMessage());
        }
    }

    public static void changeDestination(Scanner scanner) {
        Site newSite = null;
        Site oldSite = null;
        try {
            System.out.print("Enter Transport ID: ");
            int transportId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter old Site Name: ");
            String oldSiteName = scanner.nextLine();
            System.out.print("Enter new Site Name: ");
            String newSiteName = scanner.nextLine();
            // Get all sites in the area
            String areas = transportController.getAllAreas();
            Type listType = new TypeToken<ArrayList<ShipmentArea>>() {}.getType();
            ArrayList<ShipmentArea> shipmentAreas = gson.fromJson(areas, listType);

            // Find oldSite and newSite
            for (ShipmentArea area : shipmentAreas) {
                for (Site site : area.getSites()) {
                    if (site.getAddress().equals(oldSiteName)) {
                        oldSite = site;
                    }
                    if (site.getAddress().equals(newSiteName)) {
                        newSite = site;
                    }
                }
            }
            String oldSiteGson = gson.toJson(oldSite);
            String newSiteGson = gson.toJson(newSite);
            transportController.changeDestination(transportId,oldSiteGson,newSiteGson);
            System.out.println("Destination changed successfully.");
        } catch (Exception e) {
            System.out.println("Error changing destination: " + e.getMessage());
        }
    }

    private static void initializeData(String filePath) throws Exception {
        try {
            CSVReader.initializeData(filePath, transportController.getTruckService(), transportController.getDriverService(), transportController.getTransportService(), transportController.getReportService(),transportController.getShipmentAreaService());
        } catch (IOException e) {
            System.out.println("Error initializing data: " + e.getMessage());
        }
    }

    public static void getProductsReportById(Scanner scanner) {
        System.out.print("Enter Products Report ID: ");
        int productsId = scanner.nextInt();
        scanner.nextLine();
        String result = transportController.getProductsReportById(productsId);
        ProductsReport productsReport = gson.fromJson(result,ProductsReport.class);
        if (result == null) {
            System.out.print("No Products Report Matches this Id\n");
        } else {
            System.out.println(productsReport.toString());
        }
    }

    public static void getTransportReportById(Scanner scanner) {
        System.out.print("Enter Transport Report ID: ");
        int transportId = scanner.nextInt();
        scanner.nextLine();
        String result = transportController.getTransportReportById(transportId);
        if (result == null) {
            System.out.print("No Transport Report Matches this Id\n");
        } else {
            TransportReport transportReport = gson.fromJson(result,TransportReport.class);
            System.out.println(transportReport.toString());
        }
    }
    public static void getProductReportBySite(Scanner scanner){
        System.out.print("Enter Site Address: ");
        String siteName = scanner.nextLine();
        String sitesAndProducts = transportController.getAllSiteProductsReports();
        Type listType = new TypeToken<ArrayList<SiteProductsReport>>() {}.getType();
        ArrayList<SiteProductsReport> sitesAndProductsReports = gson.fromJson(sitesAndProducts, listType);
        for (SiteProductsReport siteProductsReport : sitesAndProductsReports){
            if (siteName.equals(siteProductsReport.getSite().getAddress())){
                System.out.println(siteProductsReport.getProductsReport().toString());
            }
        }
    }
    public static void removeSiteFromShipmentArea(Scanner scanner) {
        System.out.print("Enter Area Name: ");
        String areaName = scanner.nextLine();
        System.out.print("Enter Site Address: ");
        String address = scanner.nextLine();
        String response = transportController.removeSiteFromArea(areaName, address);
        System.out.println(response);
    }


}
