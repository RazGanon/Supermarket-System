package Service;

import Domain.ShipmentArea;
import Domain.Site;
import Exceptions.SiteNotInArea;

import java.util.ArrayList;

public class ShipmentAreaService {
    private ArrayList<ShipmentArea> shipmentAreas;

    public ShipmentAreaService() {
        this.shipmentAreas = new ArrayList<>();
    }

    public ArrayList<ShipmentArea> getShipmentAreas() {
        return shipmentAreas;
    }

    public void addShipmentArea(ShipmentArea shipmentArea) {
        for (ShipmentArea area : shipmentAreas){
            if(shipmentArea.getAreaName().equals(area.getAreaName())){
                return;
            }
        }
        shipmentAreas.add(shipmentArea);
    }

    public void addSiteToArea(String areaName, Site site) {
        ShipmentArea targetArea = null;
        for (ShipmentArea shipmentArea : shipmentAreas) {
            if (shipmentArea.getAreaName().equals(areaName)) {
                targetArea = shipmentArea;
                break;
            }
        }


        targetArea = new ShipmentArea(new ArrayList<>(), areaName);
        shipmentAreas.add(targetArea);
        targetArea.addSite(site);
    }

    public boolean removeSiteFromArea(String address, String areaName) throws SiteNotInArea {
        for (ShipmentArea shipmentArea : shipmentAreas) {
            if (shipmentArea.getAreaName().equals(areaName)) {
                boolean removed = shipmentArea.removeSite(address);
                if (removed) {
                    return true;
                } else {
                    throw new SiteNotInArea("Site not found in area " + areaName);
                }
            }
        }
        throw new SiteNotInArea("Area " + areaName + " not found");
    }

    public boolean isSiteInShipmentArea(String address, String areaName) {
        for (ShipmentArea shipmentArea : shipmentAreas) {
            if (shipmentArea.getAreaName().equals(areaName)) {
                return shipmentArea.isSiteInShipmentArea(new Site(address, "", ""));
            }
        }
        return false;
    }

    public ArrayList<ShipmentArea> getAllAreas() {
        return shipmentAreas;
    }
}
