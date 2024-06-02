package Domain;

public class Truck {
    private String licenseNumber;
    private String requiredLicense;
    private String model;
    private double netWeight;
    private double maxWeight;
    private boolean available;

    public Truck(String l_number,String m,double n_weight,double m_weight,String r_license){
        this.licenseNumber = l_number;
        this.model = m;
        this.netWeight = n_weight;
        this.maxWeight = m_weight;
        this.available = false;
        this.requiredLicense = r_license;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getModel() {
        return model;
    }


    public double getNetWeight() {
        return netWeight;
    }

    public String getRequiredLicense() {
        return requiredLicense;
    }

    public double getMaxWeight() {
        return maxWeight;
    }
    public void setNetWeight(double netWeight) {
        this.netWeight = netWeight;
    }
    public boolean isAvailable(){
        return this.available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}