package Domain;

import java.util.Date;

public class SuperMarket {
    private String address;
    private String ManagerName;
    public SuperMarket (String address, String ManagerName ) {
        this.address= address;
        this.ManagerName = ManagerName;
    }
    @Override
    public String toString() {
        return "Super Market Address : " +
                address+
                ", The Manager Of This Branch " + ManagerName +
                '}';
    }
}
