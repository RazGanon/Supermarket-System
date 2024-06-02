package Domain;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
public class Transport {
    private Truck truck;
    private String t_id;
    private Driver driver;
    private LocalDate localDate;
    private LocalTime localTime;
    private Site source;
    private ArrayList<Site> destinations;
    private TransportReport tsp;
    private HashMap<Site,ProductsReport> h_map;

    Transport(Truck t,Driver d,Site s,TransportReport tsp,LocalTime time , LocalDate date){
        this.truck = t;
        this.driver = d;
        this.source = s;
        this.localTime = time;
        this.localDate = date;
        this.destinations = new ArrayList<Site>();
        this.tsp = tsp;
        this.h_map = new HashMap<Site,ProductsReport>();
    }
    public Truck getTruck(){
        return truck;
    }

    public TransportReport getTsp() {
        return tsp;
    }

    public Driver getDriver(){
        return driver;
    }
    public String getTransportId(){
        return this.t_id;
    }


    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    public ArrayList<Site> getDestinations() {
        return destinations;
    }

    public HashMap<Site, ProductsReport> getH_map() {
        return h_map;
    }
}
