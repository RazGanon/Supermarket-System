package Domain;
import java.util.List;

public class ShipmentArea {
    private List<Site> sites;
    private String areaName;

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    public List<Site> getSites() {
        return sites;
    }

    public String getAreaName() {
        return areaName;
    }

    public ShipmentArea(List<Site> sites, String areaName) {
        this.sites = sites;
        this.areaName = areaName;
    }

    // Method to add a site to the list
    public void addSite(Site site) {
        if (this.sites != null) {
            this.sites.add(site);
        } else {
            throw new IllegalStateException("The sites list has not been initialized.");
        }
    }

    // Method to remove a site from the list by name
    public boolean removeSite(String address) {
        if (this.sites != null) {
            for (Site site : this.sites) {
                if (site.getAddress().equals(address)) {
                    this.sites.remove(site);
                    return true; // Site found and removed
                }
            }
        } else {
            throw new IllegalStateException("The sites list has not been initialized.");
        }
        return false; // Site not found
    }
}
