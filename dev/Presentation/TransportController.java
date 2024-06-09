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
    private TransportController(TruckService truckService, DriverService driverService, ReportService reportService, ShipmentAreaService shipmentAreaService) {
        this.transportService = new TransportService();
        this.truckService = truckService;
        this.driverService = driverService;
        this.reportService = reportService;
        this.shipmentAreaService = shipmentAreaService;
        this.gson = new Gson();
    }

    public static TransportController getInstance(TruckService truckService, DriverService driverService, ReportService reportService,ShipmentAreaService shipmentAreaService) {
        if (instance == null) {
            instance = new TransportController(truckService, driverService, reportService,shipmentAreaService);
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

            System.out.println("Transport Report after change: " + gson.toJson(transport.getTsp())); // Debug statement
            return "Truck changed successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    public String changeDestination(int transportId, String oldSiteGson, String newSiteGson) {
        try {
            Transport transport = transportService.getTransportById(transportId);
            if (transport == null) {
                return "Transport not found.";
            }
            // Get all shipment areas
            ArrayList<ShipmentArea> shipmentAreas = shipmentAreaService.getShipmentAreas();
            Site oldSite = gson.fromJson(oldSiteGson,Site.class);
            Site newSite = gson.fromJson(newSiteGson,Site.class);
            transportService.changeDestination(oldSite,transportService.getTransportById(transportId),newSite);
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

}
