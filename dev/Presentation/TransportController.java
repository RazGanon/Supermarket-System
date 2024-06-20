package Presentation;
import Domain.*;
import Service.*;
import com.google.gson.Gson;
import java.util.ArrayList;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import Exceptions.*;

public class TransportController {
    private static TransportController instance;
    private TransportService transportService;
    private TruckService truckService;
    private DriverService driverService;
    private ReportService reportService;
    private Gson gson;
    private ShipmentAreaService shipmentAreaService;
    private TransportController() {
        this.transportService = new TransportService();
        this.truckService = new TruckService();
        this.driverService = new DriverService();
        this.reportService = new ReportService();
        this.shipmentAreaService = new ShipmentAreaService();
        this.gson = new Gson();
    }

    public static TransportController getInstance() {
        if (instance == null) {
            instance = new TransportController();
        }
        return instance;
    }

    public String getAllTransports() {
        ArrayList<Transport> transports = transportService.getAllTransports();
        return gson.toJson(transports);
    }

    public String getTransportById(int id) {
        Transport transport = transportService.getTransportById(id);
        return transport != null ? gson.toJson(transport) : "No transport with ID:" + id;
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
        if (transportRequest.getDestinationAddresses() == null || transportRequest.getDestinationAddresses().isEmpty()) {
            return "Error: At least one destination address is required.";
        }

        try {
            transportService.createTransport(transportRequest, truckService, driverService, reportService,shipmentAreaService);
            return "Transport created successfully.";
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

    public String changeTransportTruck(int transportId, String change) {
        try {
            Transport transport = transportService.getTransportById(transportId);
            if (transport == null) {
                return "Transport not found.";
            }

            Truck oldTruck = transport.getTruck();
            Truck newTruck = truckService.getAvailableTruck(transport.getTsp().getInitialWeight());
            if (!driverService.isValidLicense(newTruck.getRequiredLicense(), transport.getDriver().getLicenseType())) {
                return "Error: Driver license type does not fit the truck's required license type.";
            }

            transport.setTruck(newTruck);
            oldTruck.setAvailable(true);
            newTruck.setAvailable(false);
            transport.getTsp().addChangesMade(change);

            return "Truck changed successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    public String changeDestination(int transportId, String oldSiteGson, String newSiteGson) {
        try {
            Transport transport = transportService.getTransportById(transportId);
            Site oldSite = gson.fromJson(oldSiteGson,Site.class);
            Site newSite = gson.fromJson(newSiteGson,Site.class);
            transportService.changeDestination(oldSite,transport,newSite);
            return "Destination changed successfully.";
        } catch (Exception e) {
            return "Error changing destination: " + e.getMessage();
        }
}
    public String removeDestinationsFromTransport(int transportId, String sitesJson, String changes) {
        Transport transport = transportService.getTransportById(transportId);
        if (transport == null) {
            return "Transport not found.";
        }

        Type siteListType = new TypeToken<ArrayList<Site>>() {}.getType();
        ArrayList<Site> sites = gson.fromJson(sitesJson, siteListType);

        TransportReport transportReport = transport.getTsp();
        transportService.removeDestinations(sites, changes, transportReport, transport);

        return "Destinations removed successfully.";
    }
    public TransportService getTransportService() {
        return transportService;
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

        truckService.addTruck(truck);
        return "Truck added successfully.";
    }

    public String removeTruck(String licenseNumber) {
        boolean result = truckService.removeTruck(licenseNumber);
        return result ? "Truck removed successfully." : "Truck not found.";
    }

    public String getAllTrucks() {
        return gson.toJson(truckService.getAllTrucks());
    }

    public TruckService getTruckService() {
        return truckService;
    }

    public String setTruckAvailability(String licenseNumber, boolean available) {
        if (truckService.setTruckAvailability(licenseNumber, available)) {
            return "Truck availability updated.";
        } else {
            return "Truck not found.";
        }
    }

    public String getTruckByLicenseNumber(String id) {
        return gson.toJson(truckService.getTruckByLicenseNumber(id));
    }
    public DriverService getDriverService() {
        return driverService;
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

        driverService.addDriver(driver);
        return "Driver added successfully.";
    }

    public String removeDriver(long id) {
        Driver driver = driverService.getDriverById(id);
        if (driver != null) {
            return driverService.removeDriver(driver);
        } else {
            return "Error: Driver not found.";
        }
    }

    public String setDriverAvailability(long id, boolean available) {
        if (driverService.setDriverAvailability(id, available)) {
            return "Driver availability updated.";
        } else {
            return "Error: Driver not found.";
        }
    }

    public String getAllDrivers() {
        return gson.toJson(driverService.getAllDrivers());
    }

    public String getDriverById(long id) {
        Driver driver = driverService.getDriverById(id);
        return driver != null ? gson.toJson(driver) : null;
    }

    public String updateDriverInfo(int id, String newLicenseType) {
        if (newLicenseType == null || newLicenseType.isEmpty()) {
            return "Error: New license type is required.";
        }
        if (driverService.updateDriverInfo(id, newLicenseType)) {
            return "Driver license updated";
        } else {
            return "Error: Driver not found.";
        }
    }
    public String getProductsReportById(int id) {
        ProductsReport report = reportService.getProductsReportById(id);
        return report != null ? gson.toJson(report) : null;
    }

    public ReportService getReportService(){
        return reportService;
    }

    public String getAllProductsReports() {
        return gson.toJson(reportService.getProductsReports());
    }


    public String getTransportReportById(int id) {
        TransportReport report = reportService.getTransportReportById(id);
        if(report != null){
            return  gson.toJson(report);
        }
        return null;
    }


    public String addSiteProductsReport(String json) {
        SiteProductsReport report = gson.fromJson(json, SiteProductsReport.class);
        reportService.addSiteProductsReport(report);
        return "Site products report added successfully.";
    }



    public String getAllSiteProductsReports() {
        return gson.toJson(reportService.getSiteProductsReports());
    }
    public String addSiteToArea(String areaName, String siteJson) {
        try {
            Site site = gson.fromJson(siteJson, Site.class);
            shipmentAreaService.addSiteToArea(areaName, site);
            return "Site added successfully.";
        } catch (Exception e) {
            return "Error adding site: " + e.getMessage();
        }
    }



    public String isSiteInShipmentArea(String address, String areaName) {
        boolean exists = shipmentAreaService.isSiteInShipmentArea(address, areaName);
        return exists ? "Site exists in the shipment area " + areaName + "." : "Site does not exist in the shipment area " + areaName + ".";
    }

    public String getAllAreas() {
        ArrayList<ShipmentArea> areas = shipmentAreaService.getAllAreas();
        return gson.toJson(areas);
    }

    public String removeSiteFromArea(String areaName, String address) {
        try {
            boolean removed = shipmentAreaService.removeSiteFromArea(address, areaName);
            return removed ? "Site removed successfully from area " + areaName : "Site not found in area " + areaName;
        } catch (SiteNotInArea e) {
            return e.getMessage();
        }
    }
    public ShipmentAreaService getShipmentAreaService() {
        return shipmentAreaService;
    }

}
