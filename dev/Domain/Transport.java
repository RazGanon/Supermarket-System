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
    private double currWeight;
    private LocalTime localTime;
    private Site source;
    private ArrayList<Site> destinations;
    private TransportReport tsp;
    private HashMap<Site,ProductsReport> h_map;

    Transport(Truck t,Driver d,Site s,TransportReport tsp,double currWeight){
        this.truck = t;
        this.driver = d;
        this.source = s;
        this.localTime = LocalTime.now();
        this.localDate = LocalDate.now();
        this.destinations = new ArrayList<Site>();
        this.tsp = tsp;
        this.h_map = new HashMap<Site,ProductsReport>();
        this.currWeight = currWeight;
    }
    public Truck getTruck(){
        return truck;
    }
    public Driver getDriver(){
        return driver;
    }
    public String getTransportId(){
        return this.t_id;
    }
    public double getCurrWeight() {
        return currWeight;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public void setCurrWeight(double currWeight) {
        this.currWeight = currWeight;
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
