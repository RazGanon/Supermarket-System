package Domain;

import Exceptions.*;
import Data.ShipmentAreaDAO;
import java.util.ArrayList;

public class ShipmentAreaController {

    private ShipmentAreaDAO shipmentAreaDAO;

    public ShipmentAreaController() {
        this.shipmentAreaDAO = ShipmentAreaDAO.getInstance();
    }

    public ArrayList<ShipmentArea> getShipmentAreas() {
        return shipmentAreaDAO.getAllAreas();
    }

    public boolean addShipmentArea(ShipmentArea shipmentArea) {
        for (ShipmentArea area : shipmentAreaDAO.getAllAreas()) {
            if (shipmentArea.getAreaName().equals(area.getAreaName())) {
                return false;
            }
        }
        shipmentAreaDAO.addShipmentArea(shipmentArea);
        return true;
    }

    public boolean addSiteToArea(String areaName, Site site) {
        for (ShipmentArea shipmentArea : shipmentAreaDAO.getAllAreas()) {
            if (shipmentArea.getAreaName().equals(areaName)) {
                shipmentArea.addSite(site);
                shipmentAreaDAO.addSiteToArea(site,areaName);
                return true;

            }
        }
        return false;
    }

    public int removeSiteFromArea(String address, String areaName) {
        ArrayList<ShipmentArea> shipmentAreas = shipmentAreaDAO.getAllAreas();
        for (ShipmentArea shipmentArea1 : shipmentAreas){
            if (shipmentArea1.getAreaName().equals(areaName)){
                shipmentArea1.removeSite(address);
            }
        }
        return shipmentAreaDAO.removeSiteFromArea(address,areaName);
    }

    public boolean isSiteInShipmentArea(String address, String areaName) {
        for (ShipmentArea shipmentArea :shipmentAreaDAO.getAllAreas()) {
            if (shipmentArea.getAreaName().equals(areaName)) {
                return shipmentArea.isSiteInShipmentArea(new Site(address, "", ""));
            }
        }
        return false;
    }

    public ArrayList<ShipmentArea> getAllAreas() {
        return shipmentAreaDAO.getAllAreas();
    }

    public Site getSiteByAddress(String address) {
        for (ShipmentArea shipmentArea : shipmentAreaDAO.getAllAreas()) {
            for (Site site : shipmentArea.getSites()) {
                if (site.getAddress().equals(address)) {
                    return site;
                }
            }
        }
        return null;
    }
}
