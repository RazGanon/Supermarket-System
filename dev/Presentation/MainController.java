package Presentation;
import Domain.*;
import Domain.DriverController;
import Domain.ShipmentAreaController;
import Domain.TruckController;
import com.google.gson.Gson;
import java.util.ArrayList;
import Exceptions.*;

public class MainController {
    private static MainController instance;
    private TransportController transportController;
    private TruckController truckController;
    private DriverController driverController;
    private ReportController reportController;
    private Gson gson;
    private ShipmentAreaController shipmentAreaController;
    private MainController() {
        this.transportController = new TransportController();
        this.truckController = new TruckController();
        this.driverController = new DriverController();
        this.reportController = new ReportController();
        this.shipmentAreaController = new ShipmentAreaController();
        this.gson = new Gson();
    }

    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    public ArrayList<Transport> getAllTransports() {
        return transportController.getAllTransports();
    }

    public Transport getTransportById(int id) {
        return transportController.getTransportById(id);
    }


    public String createTransport(String json) {
        TransportRequest transportRequest = gson.fromJson(json, TransportRequest.class);

        // Validate request data
        if (transportRequest == null) {
            return "Error: Transport request data is invalid.";
        }
        if (transportRequest.getTruckLicenseNumber() == null || transportRequest.getTruckLicenseNumber().isEmpty()) {
            return "Error: Truck license number is required.";
        }
        if (transportRequest.getDriverId() <= 0) {
            return "Error: Valid driver ID is required.";
        }
        if (transportRequest.getRequestDay() == null || transportRequest.getRequestDay().isEmpty()) {
            return "Error: Request day is required.";
        }
        if (transportRequest.getRequestedTime() == null || transportRequest.getRequestedTime().isEmpty()) {
            return "Error: Requested time is required.";
        }
        if (transportRequest.getOriginAddress() == null || transportRequest.getOriginAddress().getAddress().isEmpty()) {
            return "Error: Origin address is required.";
        }
        if (transportRequest.getDestinations() == null || transportRequest.getDestinations().isEmpty()) {
            return "Error: At least one destination address is required.";
        }

        try {
            Transport transport = transportController.createTransport(transportRequest, truckController, driverController, reportController, shipmentAreaController);
            return "Transport created successfully. Transport ID: " + transport.getTransportId();
        } catch (InvalidDriverException e) {
            return "Error: Invalid driver for the transport weight.";
        } catch (NoAvailableTruckException e) {
            return "Error: No available truck for the specified license number.";
        } catch (TruckCantHandleWeight e) {
            return "Error: Truck can't handle the transport weight.";
        } catch (NoAvailableDriver e) {
            return "Error: No available driver with the specified ID.";

        }catch (DestinationsNotInSameAreaException e){
            return "Error: not all sites are in the same area";

         } catch (Exception e) {
            return "Error creating transport: " + e.getMessage();
        }

    }
    public void displaySiteProductsById(int id){
        String result = reportController.displaySiteProductsById(id);
        System.out.println(result.toString());
    }
    public String changeTransportTruck(int transportId,String change,String  jsonOldTruck) {
        try {
            Truck oldTruck = gson.fromJson(jsonOldTruck,Truck.class);
            transportController.changeTransportTruck(driverController,oldTruck,truckController,transportController.getTransportById(transportId),change);
            reportController.updateTransportReport(transportId,change);
            return "Truck changed successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    public void changeDestination(int transportId, String oldSiteGson, String newSiteGson) {
        try {
            Transport transport = transportController.getTransportById(transportId);
            Site oldSite = gson.fromJson(oldSiteGson, Site.class);
            Site newSite = gson.fromJson(newSiteGson, Site.class);
            transportController.changeDestination(oldSite, transport, newSite);
            reportController.resetTransportReportId(oldSite.getAddress());
            reportController.linkSiteProductsToReport(newSite.getAddress(), transportId);
        } catch (Exception e) {
            System.out.println("Error changing destination: " + e.getMessage());
        }
    }
    public TransportController getTransportService() {
        return transportController;
    }
    public String addTruck(String json) {
        Truck truck = gson.fromJson(json, Truck.class);

        // Validate truck data
        if (truck.getLicenseNumber() == null || truck.getLicenseNumber().isEmpty()) {
            return "Error: Truck license number is required.";
        }
        if (truck.getModel() == null || truck.getModel().isEmpty()) {
            return "Error: Truck model is required.";
        }
        if (truck.getRequiredLicense() == null || truck.getRequiredLicense().isEmpty()) {
            return "Error: Required license is required.";
        }

        truckController.addTruck(truck);
        return "Truck added successfully.";
    }

    public String removeTruck(String licenseNumber) {
        boolean result = truckController.removeTruck(licenseNumber);
        return result ? "Truck removed successfully." : "Truck not found.";
    }

    public String getAllTrucks() {
        return gson.toJson(truckController.getAllTrucks());
    }
    public ShipmentAreaController getShipmentAreaController(){
        return shipmentAreaController;
    }
    public TruckController getTruckController() {
        return truckController;
    }

    public String setTruckAvailability(String licenseNumber, boolean available) {
        if (truckController.setTruckAvailability(licenseNumber, available)) {
            return "Truck availability updated.";
        } else {
            return "Truck not found.";
        }
    }

    public Truck getTruckByLicenseNumber(String id) {
        return truckController.getTruckByLicenseNumber(id);
    }
    public DriverController getDriverController() {
        return driverController;
    }

    public String addDriver(String json) {
        Driver driver = gson.fromJson(json, Driver.class);

        // Validate driver data
        if (driver.getDriverName() == null || driver.getDriverName().isEmpty()) {
            return "Error: Driver name is required.";
        }
        if (driver.getLicenseType() == null || driver.getLicenseType().isEmpty()) {
            return "Error: License type is required.";
        }

        driverController.addDriver(driver);
        return "Driver added successfully.";
    }

    public void removeDriver(int id) {
        Driver driver = driverController.getDriverById(id);
        if (driver != null) {
            boolean bool = driverController.removeDriver(driver);
            if(bool){
                System.out.println("Driver removed successfully.");
            }

        } else {
            System.out.println("Error: Driver not found.");
        }
    }

    public String setDriverAvailability(int id, boolean available) {
        if (driverController.setDriverAvailability(id, available)) {
            return "Driver availability updated.";
        } else {
            return "Error: Driver not found.";
        }
    }

    public ArrayList<Driver> getAllDrivers() {
        return driverController.getAllDrivers();
    }

    public Driver getDriverById(int id) {
        return driverController.getDriverById(id);
    }


    public ReportController getReportController(){
        return reportController;
    }



    public TransportReport getTransportReportById(int id) {
        TransportReport report = reportController.getTransportReportById(id);
        return report;
    }


    public String addSiteToArea(String areaName, String siteJson) {

        Site site = gson.fromJson(siteJson, Site.class);
        if (shipmentAreaController.addSiteToArea(areaName, site)) {
            return "Site added successfully.";
        } else {
            return "No shipment area such as " + areaName;
        }

    }


    public String isSiteInShipmentArea(String address, String areaName) {
        boolean exists = shipmentAreaController.isSiteInShipmentArea(address, areaName);
        return exists ? "Site exists in the shipment area " + areaName + "." : "Site does not exist in the shipment area " + areaName + ".";
    }

    public ArrayList<ShipmentArea> getAllAreas() {
        ArrayList<ShipmentArea> areas = shipmentAreaController.getAllAreas();
        return areas;
    }

    public String removeSiteFromArea(String address, String areaName) {

        int status = shipmentAreaController.removeSiteFromArea(address, areaName);
        switch (status) {
            case 1:
                return "Site removed successfully from area " + areaName;
            case 2:
                return "Site not found in area " + areaName;
            case 3:
                return "Cannot remove site: It is connected to existing transports.";
            case 4:
                return "Site not exists.";
            case 5:
                return "Area not exists.";
            default:
                return "Failed to remove site from area.";
        }
    }

    public boolean AddShipmentArea(String jsonArea){
        ShipmentArea shipmentArea = gson.fromJson(jsonArea,ShipmentArea.class);
        return shipmentAreaController.addShipmentArea(shipmentArea);
    }
    public ShipmentAreaController getShipmentAreaService() {
        return shipmentAreaController;
    }

}
