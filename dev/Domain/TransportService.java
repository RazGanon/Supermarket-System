package Domain;
import java.util.ArrayList;
import java.util.List;


public class TransportService {
    private List<Transport> transports = new ArrayList<>();


    public Transport createTransport(TransportRequest request,TruckService t_service,DriverService d_service) throws Exception {
        try {
            Driver driver = d_service.getValidDriver(request);
            Truck truck = t_service.getAvailableTruck();
            TransportReport transportReport = new TransportReport(request.getTransportWeight());
            Transport transport = new Transport(truck, driver, request.getOriginAddress(), transportReport, request.getRequestedTime(), request.getRequestDay());
            transports.add(transport);
            return transport;
        } catch (Exception e) {
            throw new Exception("Could not approve request");
        }
    }
    public boolean changTransportTruck(Truck truck,TruckService tService,Transport transport,String changes)throws Exception{
        try {
            Truck newtruck = tService.getAvailableTruck();
            if(!(newtruck.getRequiredLicense().equals(transport.getDriver().getLicenseType()))){
                throw new  Exception("Driver cannot drive this truck");
            }
            transport.setTruck(newtruck);
            truck.setAvailable(true);
            newtruck.setAvailable(false);
            transport.getTsp().addChangesMade(changes);
            return true;
        }catch(Exception e) {
            return false;
        }
    }
//    public boolean removeDestinations(ArrayList<Site> sites,String changes){
//
//    }


}