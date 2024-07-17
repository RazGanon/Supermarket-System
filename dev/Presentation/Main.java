package Presentation;

import com.google.gson.Gson;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import Domain.*;
import DomainEmployees.*;
public class Main {
    private static Gson gson = new Gson();
    private static MainController mainController;
    private static EmployeeController employee_controller;
    public Main(){
        mainController = MainController.getInstance();
        employee_controller = new EmployeeController();
    }
    public static void main(String[] args) {
        new Main();
        Scanner scanner = new Scanner(System.in);
        employee_controller.addAllTAbleEmp();
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
                    listAllTransports();
                    break;
                case 11:
                    getTransportReportById(scanner);
                    break;
                case 12:
                    addSiteToShipmentArea(scanner);
                    break;
                case 13:
                    removeSiteFromShipmentArea(scanner);
                    break;
                case 14:
                    addShipmentArea(scanner);
                    break;
                case 15:
                    listSiteProductsByTransportId(scanner);
                    break;
                case 16:
                    System.out.println("Exiting...");
                    return;
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
        System.out.println("10. List All Transports");
        System.out.println("11. Get Transport Report By Id");
        System.out.println("12. Add Site To Selected Shipment Area");
        System.out.println("13. Remove Site From Selected Shipment Area");
        System.out.println("14. Add Shipment Area");
        System.out.println("15. List Site Products By Transport ID");
        System.out.println("16. Exit");
        System.out.println("Enter your choice: ");
    }
    public static void addShipmentArea(Scanner scanner){
        System.out.println("Enter Requested Shipment Area Name:");
        String shipmentAreaName = scanner.nextLine();
        ShipmentArea shipmentArea = new ShipmentArea(new ArrayList<>(),shipmentAreaName);
        String areaJson = gson.toJson(shipmentArea);
        boolean bool = mainController.AddShipmentArea(areaJson);
        if (bool){
            System.out.println("Shipment Area added");
        }else{
            System.out.println("Shipment Area already exists");
        }
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
        ArrayList<SiteProducts> siteAndProducts = new ArrayList<>();
        while (true) {
            System.out.print("Enter Site Name (or 'done' to finish): ");
            String siteName = scanner.nextLine();
            if (siteName.equalsIgnoreCase("done")) {
                break;
            }

            Site site = mainController.getShipmentAreaController().getSiteByAddress(siteName);
            if (site != null) {
                destinationAddresses.add(site);
                SiteProducts sp = mainController.getReportController().getSiteProducts(siteName);
                if (sp != null) {
                    siteAndProducts.add(sp);
                }
            } else {
                System.out.println("Site not found in area.");
            }
        }
        TransportRequest transportRequest = new TransportRequest(
                requestDay,
                requestedTime,
                new Site(originAddress, "", ""),
                siteAndProducts,
                truckLicenseNumber,
                driverId
        );

        String json = gson.toJson(transportRequest);
        String result = mainController.createTransport(json);
        // Check for errors using if-else statements
        if (result.startsWith("Error:")) {
            switch (result) {
                case "Error: Invalid driver for the transport weight.":
                    System.out.println("Invalid driver for the transport weight.");
                    break;
                case "Error: No available truck for the specified license number.":
                    System.out.println("No available truck for the specified license number.");
                    break;
                case "Error: Truck can't handle the transport weight.":
                    System.out.println("Truck can't handle the transport weight.");
                    break;
                case "Error: No available driver with the specified ID.":
                    System.out.println("No available driver with the specified ID.");
                    break;
                case "Error: not all sites are in the same area.":
                    System.out.println("Not all sites are in the same area.");
                    break;
                default:
                    System.out.println(result);
                    break;
            }
            return;
        }

        System.out.println(result);
        try {
            Truck truck = mainController.getTruckController().getTruckByLicenseNumber(truckLicenseNumber);
            double weight = mainController.getTransportService().weighTruck(siteAndProducts);
            if (truck.getMaxWeight() - truck.getNetWeight() < weight) {
                int transportId = parseTransportId(result);
                System.out.println("Total weight exceeds the truck's maximum capacity. Choose an action:");
                System.out.println("1. Remove Products");
                System.out.println("2. Remove/Change Site");
                System.out.println("3. Change Truck");
                int action = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (action) {
                    case 1:
                        removeProducts(scanner, siteAndProducts,transportId);
                        break;
                    case 2:
                        removeOrChangeSite(scanner, siteAndProducts,transportId);
                        break;
                    case 3:
                        changeToAvailableTransportTruck(scanner);
                }

            }
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("No available truck")) {
                System.out.println(e.getMessage());
            } else if (e.getMessage().contains("No available driver")) {
                System.out.println(e.getMessage());
            } else if (e.getMessage().contains("Driver license cannot fit the transport truck required license")) {
                System.out.println(e.getMessage());
            } else if (e.getMessage().contains("All destinations must be in the same area")) {
                System.out.println(e.getMessage());
            } else {
                System.out.println("Error: " + e.getMessage());
            }


        }
    }
    public static void listSiteProductsByTransportId(Scanner scanner) {
        System.out.print("Enter Transport ID: ");
        int transportId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        mainController.displaySiteProductsById(transportId);
    }

    private static void removeProducts(Scanner scanner, ArrayList<SiteProducts> siteAndProducts,int transportId) {
        while (true) {
            System.out.print("Enter Site Address to remove products from (or 'done' to finish): ");
            String siteAddress = scanner.nextLine();
            if (siteAddress.equalsIgnoreCase("done")) {
                break;
            }
            for (SiteProducts sp : siteAndProducts) {
                if (sp.getSite().getAddress().equals(siteAddress)) {
                    System.out.print("Enter Product ID to remove: ");
                    int productId = scanner.nextInt();
                    System.out.print("Enter quantity to remove: ");
                    int quantityToRemove = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    boolean productFound = false;
                    for (Product product : sp.getProducts()) {
                        if (product.getId() == productId) {
                            productFound = true;
                            if (product.getQuantity() > quantityToRemove) {
                                product.setQuantity(product.getQuantity() - quantityToRemove);
                                mainController.getReportController().updateProductQuantity(siteAddress, productId, product.getQuantity());
                                mainController.getReportController().updateTransportReport(transportId,"Product quantity update for "+
                                        product.getId() + "current quantity: " + product.getQuantity());
                                System.out.println("Product quantity of product id:" + productId + " updated.");
                            } else {
                                sp.getProducts().remove(product);
                                boolean success = mainController.getReportController().removeProductsFromSite(siteAddress, productId);
                                if (success) {
                                    mainController.getReportController().updateTransportReport(transportId,"Product removed from transport");
                                    System.out.println("Product removed from the site and database successfully.");
                                } else {
                                    System.out.println("Failed to remove product from the database.");
                                }
                            }
                            break;
                        }
                    }

                    if (!productFound) {
                        System.out.println("Product ID not found at the specified site.");
                    }
                    break;
                }
            }
        }
    }
    public static void changeToAvailableTransportTruck(Scanner scanner){
        System.out.print("Enter transport id: ");
        int transportId = scanner.nextInt();
        scanner.nextLine();
        String changes = "Changed transport truck";
        Transport transport = mainController.getTransportById(transportId);
        String json = gson.toJson(transport.getTruck());
        mainController.changeTransportTruck(transportId,changes,json);

    }
    public static void removeOrChangeSite(Scanner scanner, ArrayList<SiteProducts> siteAndProducts, int transportId) {
        while (true) {
            System.out.print("Enter Site Address to remove/change (or 'done' to finish): ");
            String siteAddress = scanner.nextLine();
            if (siteAddress.equalsIgnoreCase("done")) {
                break;
            }

            SiteProducts siteToRemove = null;
            ArrayList<Site> sites = new ArrayList<>();
            for (SiteProducts sp : siteAndProducts) {
                if (sp.getSite().getAddress().equals(siteAddress)) {
                    siteToRemove = sp;
                    sites.add(siteToRemove.getSite());
                    break;
                }
            }

            if (siteToRemove != null) {
                System.out.println("1. Remove Site");
                System.out.println("2. Change Site");
                int action = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (action == 1) {
                    // Removing site
                    mainController.getTransportService().removeTransportDestinations(transportId, sites);
                    mainController.getReportController().resetTransportReportId(siteAddress);
                    mainController.getReportController().updateTransportReport(transportId, "Removed site: " + siteAddress);
                } else if (action == 2) {
                    // Changing site
                    System.out.print("Enter new Site Address: ");
                    String newSiteAddress = scanner.nextLine();
                    Site newSite = mainController.getShipmentAreaController().getSiteByAddress(newSiteAddress);
                    if (newSite != null) {
                        String oldSiteJson = gson.toJson(siteToRemove.getSite());
                        String newSiteJson = gson.toJson(newSite);
                        mainController.changeDestination(transportId, oldSiteJson, newSiteJson);
                        mainController.getReportController().updateTransportReport(transportId, "Changed site: " + siteAddress + ", to new site: " + newSiteAddress);
                    } else {
                        System.out.println("Site not found.");
                    }
                } else {
                    System.out.println("Invalid choice.");
                }
            } else {
                System.out.println("Site not found.");
            }
        }
    }

    public static void addDriver(Scanner scanner) {
        System.out.print("Enter Driver license Type: ");
        String licenseType = scanner.nextLine();
        System.out.print("Enter Driver Name: ");
        String driverName = scanner.nextLine();
        System.out.print("Enter Driver Last Name: ");
        String driverLastName = scanner.nextLine();
        System.out.print("Enter Driver ID: ");
        int driverId = scanner.nextInt();
        System.out.print("Is Driver Currently Available (Please Enter - (true/false)) : ");
        String availabilityStr = scanner.nextLine();
        boolean availability = Boolean.parseBoolean(availabilityStr);
        scanner.nextLine();//Consume new line
        System.out.print("Enter employee salary: ");
        int salary = Integer.parseInt(scanner.nextLine());
        System.out.println("Ask the employee what password they want: ");
        String newEmpPassword = scanner.nextLine();
        String dayOff = "0"; // start with 0 days off
        String jobType = "";
        while (true) {
            System.out.print("Choose job type:\n1. Full-Time Job\n2. Part-Time Job\nEnter your choice: ");
            int jobTypeChoice = Integer.parseInt(scanner.nextLine());
            if (jobTypeChoice == 1) {
                jobType = "Full-Time Job";
                break;
            } else if (jobTypeChoice == 2) {
                jobType = "Part-Time Job";
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        LocalDate date = LocalDate.now();
        String sdate = date.toString();
        terms newTerms = new terms(sdate, jobType, dayOff);
        SuperMarket superMarket = new SuperMarket("Driver's Site","shon");
        Employee e = employee_controller.registerEmployeeManually(driverName,driverLastName,Integer.toString(driverId),salary,newTerms,superMarket,newEmpPassword);
        if (e != null){
            employee_controller.changeEmpRole(e,Role.Driver);
        }
        else{
           Employee employee = EmployeeController.getEmployeeById(Integer.toString(driverId)) ;
           employee_controller.changeEmpRole(employee,Role.Driver);
        }

        Driver newDriver = new Driver(licenseType, driverName + " " + driverLastName, availability, driverId);
        String json = gson.toJson(newDriver);
        String result = mainController.addDriver(json);
        System.out.println(result);
    }

    public static void removeDriver(Scanner scanner) {
        System.out.print("Enter Driver ID To Remove: ");
        int driverId = scanner.nextInt();
        scanner.nextLine(); // Consume new line
        String d_Id = Integer.toString(driverId);
        EmployeeController.deleteEmployee(d_Id);
        mainController.removeDriver(driverId);


    }

    public static void updateDriverAvailability(Scanner scanner) {
        System.out.print("Enter Driver ID: ");
        int driverId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Is Driver Currently Available (Please Enter - (true/false)) : ");
        boolean available = scanner.nextBoolean();
        scanner.nextLine(); // Consume newline

        String result = mainController.setDriverAvailability(driverId, available);
        System.out.println(result);
    }

    public static void listAllDrivers() {
        ArrayList<Driver> allDrivers = mainController.getAllDrivers();
        for (Driver driver : allDrivers) {
            System.out.println(driver);
        }
    }

    public static void listAllTrucks() {
        String allTrucksJson = mainController.getAllTrucks();
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
        Truck newTruck = new Truck(licenseNumber, truckModel, truckNetWeight, truckMaxWeight, requiredLicenseType,true);
        String json = gson.toJson(newTruck);
        String result = mainController.addTruck(json);
        System.out.println(result);
    }

    public static void removeTruck(Scanner scanner) {
        System.out.print("Enter Truck To Remove license Number: ");
        String licenseNumber = scanner.nextLine();
        String result = mainController.removeTruck(licenseNumber);
        System.out.println(result);
    }

    public static void updateTruckAvailability(Scanner scanner) {
        System.out.print("Enter Truck License Number: ");
        String licenseNumber = scanner.nextLine();
        System.out.print("Is Truck Currently Available (Please Enter - (true/false)) : ");
        boolean available = scanner.nextBoolean();
        scanner.nextLine(); // Consume newline

        String result = mainController.setTruckAvailability(licenseNumber, available);
        System.out.println(result);
    }

    public static void listAllTransports() {
        ArrayList<Transport> allTransports = mainController.getAllTransports();
        for (Transport transport : allTransports) {
            System.out.println("Transport ID: " + transport.getTransportId());
            System.out.println("Transport Report: " + transport.getTransportReport());

        }
    }



    public static void getTransportReportById(Scanner scanner) {
        System.out.print("Enter Transport Report ID: ");
        int transportId = scanner.nextInt();
        scanner.nextLine();
        TransportReport transportReport = mainController.getTransportReportById(transportId);
        if (transportReport == null) {
            System.out.print("No Transport Report Matches this Id\n");
        }else{
            System.out.println(transportReport.toString());
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

            String response = mainController.addSiteToArea(areaName, newSiteGson);
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Error adding site: " + e.getMessage());
        }
    }

    public static void removeSiteFromShipmentArea(Scanner scanner) {
        System.out.print("Enter Area Name: ");
        String areaName = scanner.nextLine();
        System.out.print("Enter Site Address: ");
        String address = scanner.nextLine();
        String response = mainController.removeSiteFromArea(address,areaName);
        System.out.println(response);
    }
    public static int parseTransportId(String response) {
        if (response.startsWith("ERROR: ")) {
            throw new IllegalArgumentException("");
        }
        String prefix = "Transport created successfully. Transport ID: ";
        try {
            return Integer.parseInt(response.substring(prefix.length()));
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("");
        }
    }

}