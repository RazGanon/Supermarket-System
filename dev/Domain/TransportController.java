package Domain;

import Exceptions.*;
import Data.TransportDAO;

import java.util.ArrayList;
import java.util.Collection;

public class TransportController {
    private TransportDAO transportDAO;


    public TransportController() {
        this.transportDAO = TransportDAO.getInstance();

    }

    public ArrayList<Transport> getAllTransports() {
        return transportDAO.getAllTransports();
    }

    public String licenseToWeight(double weight) {
        if (weight <= 1000) {
            return "A";
        } else if (1000 < weight && weight <= 2000) {
            return "B";
        } else if (2000 < weight && weight <= 3000) {
            return "C";
        } else if (3000 < weight && weight <= 4000) {
            return "D";
        } else {
            return "E";
        }
    }

    public double weighTruck(ArrayList<SiteProducts> siteProducts) {
        double weight = 0;
        for (SiteProducts siteProduct : siteProducts) {
            for (Product product : siteProduct.getProducts()) {
                weight += product.getQuantity() * product.getWeight();
            }
        }
        return weight;
    }

    public Transport createTransport(TransportRequest request, TruckController truckController, DriverController driverController, ReportController reportController, ShipmentAreaController shipmentAreaController) throws InvalidDriverException, NoAvailableTruckException, TruckCantHandleWeight, NoAvailableDriver, DestinationsNotInSameAreaException {
        Truck requestedTruck = truckController.getTruckByLicenseNumber(request.getTruckLicenseNumber());
        if (requestedTruck == null) {
            throw new NoAvailableTruckException("No available truck with the specified license number");
        }
        if (!(truckController.isTruckAvailable(requestedTruck.getLicenseNumber()))) {
            throw new NoAvailableTruckException("No available truck with the specified license number");
        }

        Driver requestedDriver = driverController.getDriverById(request.getDriverId());
        if (requestedDriver == null) {
            throw new NoAvailableDriver("No available driver with the specified ID");
        }
        if (!(driverController.isDriverAvailable(requestedDriver.getId()))) {
            throw new NoAvailableDriver("No available driver with the specified ID");
        }
        if (!driverController.isValidLicense(requestedTruck.getRequiredLicense(), requestedDriver.getLicenseType())){
            throw new InvalidDriverException("Driver license cannot fit the transport track required license");
        }


        ArrayList<SiteProducts> siteProducts = request.getSiteAndProducts();
        double weight = weighTruck(siteProducts);

        ArrayList<String> areaNames = new ArrayList<>();
        for (SiteProducts sp : siteProducts) {
            Site site = sp.getSite();
            for (ShipmentArea area : shipmentAreaController.getAllAreas()) {
                if (area.isSiteInShipmentArea(site)) {
                    if (!areaNames.contains(area.getAreaName())) {
                        areaNames.add(area.getAreaName());
                    }
                }
            }
        }
        if (areaNames.size() != 1) {
            throw new DestinationsNotInSameAreaException("All destinations must be in the same area.");
        }

            TransportReport transportReport = new TransportReport(request.getOriginAddress(), weight, siteProducts);
            reportController.addTransportReport(transportReport);
            Transport transport = new Transport(requestedTruck, requestedDriver, request.getOriginAddress(), request.getRequestedTime(), request.getRequestDay(), request.getDestinations(), transportReport);
            transportDAO.addTransport(transport);
            for (SiteProducts s_products : siteProducts){
                reportController.linkSiteProductsToReport(s_products.getSite().getAddress(),transportReport.getReportId());
            }
            driverController.setDriverAvailability(requestedDriver.getId(),false);
            truckController.setTruckAvailability(requestedTruck.getLicenseNumber(),false);
            requestedTruck.setAvailable(false);
            requestedDriver.setAvailable(false);
            return transport;
        }

    public boolean changeTransportTruck(DriverController driverController, Truck oldTruck, TruckController truckController, Transport transport, String changes) throws Exception {
        Truck newTruck = truckController.getAvailableTruck(transport.getTransportId());
        if (!driverController.isValidLicense(newTruck.getRequiredLicense(), transport.getDriver().getLicenseType())) {
            throw new InvalidDriverException("Driver license type not fit the truck required license type");
        }
        transport.setTruck(newTruck);
        truckController.setTruckAvailability(oldTruck.getLicenseNumber(),true);
        truckController.setTruckAvailability(newTruck.getLicenseNumber(),false);
        updateTransportTruck(transport, newTruck.getLicenseNumber());
        transport.getTransportReport().addChangesMade(changes);
        transport.setTruck(newTruck);
        return true;
    }
    public void updateTransportTruck(Transport transport,String newTruckLicenseNumber) {
        transportDAO.updateTransportTruck(transport,newTruckLicenseNumber);
    }

    public void removeTransportDestinations(int transportId, ArrayList<Site> destinationsToRemove){
        Transport transport = transportDAO.getTransport(transportId);
        ArrayList<Site> sites = transport.getDestinations();
        transportDAO.removeTransportDestinations(transportId,destinationsToRemove);
        for (Site site : destinationsToRemove){
            sites.remove(site);

        }
        transport.setDestinations(sites);
    }
    public void changeDestination(Site siteToRemove, Transport transport, Site siteToAdd) {
        ArrayList<Site> sitesToRemove = new ArrayList<>();
        sitesToRemove.add(siteToRemove);
        ArrayList<Site> sitesToAdd = new ArrayList<>();
        sitesToAdd.add(siteToAdd);
        boolean removalSuccess = transportDAO.removeTransportDestinations(transport.getTransportId(), sitesToRemove);
        boolean additionSuccess = transportDAO.addTransportDestinations(transport.getTransportId(), sitesToAdd);

        ArrayList<Site> sites = transport.getDestinations();
        sites.remove(siteToRemove);
        sites.add(siteToAdd);
        transport.setDestinations(sites);
        if (removalSuccess && additionSuccess) {
            System.out.println("Destination changed successfully.");
        } else {
            System.out.println("Destination change failed.");

        }
    }


    public Transport getTransportById(int id) {
        return transportDAO.getTransport(id);
    }
}
