package Service;

import Domain.*;
import java.util.ArrayList;
import Exceptions.*;

public class TransportService {
    private ArrayList<Transport> transports = new ArrayList<>();

    public ArrayList<Transport> getAllTransports() {
        return transports;
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
    //public int checkSitesValidity()
    public double weighTruck(ArrayList<SiteProductsReport> siteProductsReports){
        double weight = 0; // transport weight
        for (SiteProductsReport siteProductsReport : siteProductsReports) {
            for (Product product : siteProductsReport.getProductsReport().getProducts()) {
                weight += product.getQuantity()*product.getWeight();
            }
        }
        return weight;

    }
    public Transport createTransport(TransportRequest request, TruckService t_service, DriverService d_service, ReportService reportService,ShipmentAreaService shipmentAreaService) throws InvalidDriverException, NoAvailableTruckException, TruckCantHandleWeight, NoAvailableDriver, DestinationsNotInSameAreaException {
        Truck requestedTruck = t_service.getTruckByLicenseNumber(request.getTruckLicenseNumber());
        if (requestedTruck == null) {
            throw new NoAvailableTruckException("No available truck with the specified license number");
        }
        if (!(t_service.isTruckAvailable(requestedTruck.getLicenseNumber()))) {
            throw new NoAvailableTruckException("No available truck with the specified license number");
        }

        Driver requestedDriver = d_service.getDriverById(request.getDriverId());
        if (requestedDriver == null) {
            throw new NoAvailableDriver("No available driver with the specified ID");
        }
        if (!(d_service.isDriverAvailable(requestedDriver.getId()))) {
            throw new NoAvailableDriver("No available driver with the specified ID");
        }
        if (!d_service.isValidLicense(requestedTruck.getRequiredLicense(), requestedDriver.getLicenseType())){
            throw new InvalidDriverException("Driver license cannot fit the transport track required license");
        }
        ArrayList<SiteProductsReport> siteProductsReports = request.getSiteProductsReports();
        double weight = weighTruck(siteProductsReports);
        if (requestedTruck.getMaxWeight() - requestedTruck.getNetWeight() < weight ){
            throw new TruckCantHandleWeight("Truck can't handle transport weight");
        } else {
            // Check if all destination addresses are in the same area
            ArrayList<String> areaNames = new ArrayList<>();
            for (Site site : request.getDestinationAddresses()) {
                for (ShipmentArea area : shipmentAreaService.getAllAreas()) {
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

            TransportReport transportReport = new TransportReport(weight);
            Transport transport = new Transport(requestedTruck, requestedDriver, request.getOriginAddress(), transportReport, request.getRequestedTime(), request.getRequestDay(), request.getSiteProductsReports(), request.getDestinationAddresses());
            transports.add(transport);
            reportService.addTransportReport(transportReport);
            requestedDriver.setAvailable(false);
            requestedTruck.setAvailable(false);
            return transport;
        }
    }


    public boolean changeTransportTruck(DriverService dService, Truck oldTruck, TruckService tService, Transport transport, String changes) throws Exception {
        Truck newTruck = tService.getAvailableTruck(transport.getTsp().getInitialWeight());
        if (!dService.isValidLicense(newTruck.getRequiredLicense(), transport.getDriver().getLicenseType())) {
            throw new InvalidDriverException("Driver license type not fit the truck required license type");
        }
        transport.setTruck(newTruck);
        oldTruck.setAvailable(true);
        newTruck.setAvailable(false);
        transport.getTsp().addChangesMade(changes);
        return true;
    }

    public void removeDestinations(ArrayList<Site> sites, String changes, TransportReport t_report, Transport transport) {
        for (Site site : sites) {
            transport.getDestinations().remove(site);  // Ensure proper removal from destinations
            transport.getSiteAndProducts().removeIf(spr -> spr.getSite().equals(site));  // Remove from the array list as well
        }
        t_report.addChangesMade(changes);
    }

    public void changeDestination(Site site_to_change, Transport transport, Site site_to_add) {
        for (Site s : transport.getDestinations()) {
            if (s.getAddress().equals(site_to_change.getAddress())) {
                transport.getDestinations().remove(site_to_change);
                transport.getDestinations().add(site_to_add);
            }
        }
    }

    public Transport getTransportById(int id) {
        for (Transport transport : transports) {
            if (id == transport.getTsp().getReportId()) {
                return transport;
            }
        }
        return null;
    }
}
