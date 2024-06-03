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
            Transport transport = new Transport(truck, driver, request.getOriginAddress(), transportReport, request.getRequestedTime(), request.getRequestDay(),request.getProduct_reports());
            transports.add(transport);
            return transport;
        } catch (Exception e) {
            return null;
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
    public void removeDestinations(ArrayList<Site> sites,String changes,TransportReport t_report,Transport transport){
        for(Site site : sites){
            transport.getH_map().remove(site);
        }
        t_report.addChangesMade(changes);
   }
   public void changeDestination(Site site_to_change,Transport transport,Site site_to_add){
        ArrayList<Site> dest = transport.getDestinations();
        for(Site s : dest){
            if(s.equals(site_to_change)){
                dest.remove(site_to_change);
                dest.add(site_to_add);
            }
        }
        transport.setDestinations(dest);
   }


}