package Domain;
import java.util.ArrayList;

public class ShipmentArea {
    private ArrayList<Site> sites;
    private String areaName;

    public void setSites(ArrayList<Site> sites) {
        this.sites = sites;
    }

    public ArrayList<Site> getSites() {
        return sites;
    }

    public String getAreaName() {
        return areaName;
    }

    public ShipmentArea(ArrayList<Site> sites, String areaName) {
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
    // Method to check if a site is in the ShipmentArea
    public boolean isSiteInShipmentArea(Site site) {
        if (this.sites != null) {
            for (Site s : this.sites) {
                if (s.getAddress().equals(site.getAddress())) {
                    return true; // Site found
                }
            }
        }
        return false;
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
