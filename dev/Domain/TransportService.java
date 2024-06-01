package Domain;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransportService {
    private List<Transport> transports = new ArrayList<>();


//    public Transport createTransport(TransportRequest request,TruckService t_service,DriverService d_service) throws Exception {
//
//        if (t_service.isTruckAvailable(request.getTruckNumber()))
//        {
//            Truck truck = t_service.getTruckByLicenseNumber(request.getTruckNumber());
//            truck.setAvailable(false);
//
//        }else {
//            throw new Exception("Truck not available.");
//        }
//        if (d_service.assignDriverToTransport(d_service.getDriverByName(request.getDriverName()),this)) {
//            throw new Exception("Driver not available.");
//        }
//        for(Site site:request.getDestinationAddresses()){
//            if(site.)
//        }
//        Site source = new Site(request.getOriginAddress().getAddress(),request.getOriginAddress().getContactName(),request.getOriginAddress().getContactPhoneNumber());
//        TransportReport report = new TransportReport(0, "", "");
//        Transport transport = new Transport(truck, driver, source, report, 0.0);
//
//        transport.setLocalDate(request.getRequestDay());
//        transport.setLocalTime(request.getRequestedTime());
//        transport.setDestinations(convertAddressesToSites(request.getDestinationAddresses()));
//
//        transports.add(transport);
//        return transport;
//    }



    private List<Site> convertAddressesToSites(List<String> addresses) {
        List<Site> sites = new ArrayList<>();
        for (String address : addresses) {
            sites.add(new Site(address));
        }
        return sites;
    }



    public List<Transport> getAllTransports() {
        return new ArrayList<>(transports);
    }

    public boolean updateTransport(String transportId, Transport updatedTransport) {
        for (int i = 0; i < transports.size(); i++) {
            if (transports.get(i).getTransportId().equals(transportId)) {
                transports.set(i, updatedTransport);
                return true;
            }
        }
        return false;
    }

    public boolean deleteTransport(String transportId) {
        return transports.removeIf(transport -> transport.getTransportId().equals(transportId));
    }

}