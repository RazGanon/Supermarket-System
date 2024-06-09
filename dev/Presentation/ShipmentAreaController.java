package Presentation;

import Domain.ShipmentArea;
import Domain.Site;
import Service.ShipmentAreaService;
import com.google.gson.Gson;
import Exceptions.SiteNotInArea;

import java.util.ArrayList;

public class ShipmentAreaController {
    private static ShipmentAreaController instance;
    private ShipmentAreaService shipmentAreaService;
    private Gson gson;

    public ShipmentAreaService getShipmentAreaService() {
        return shipmentAreaService;
    }

    private ShipmentAreaController() {
        this.shipmentAreaService = new ShipmentAreaService();
        this.gson = new Gson();
    }

    public static ShipmentAreaController getInstance() {
        if (instance == null) {
            instance = new ShipmentAreaController();
        }
        return instance;
    }

    public String addSiteToArea(String json, String areaName) {
        Site site = gson.fromJson(json, Site.class);
        shipmentAreaService.addSiteToArea(areaName, site);
        return "Site added successfully to area " + areaName + ".";
    }

    public String removeSiteFromArea(String address, String areaName) {
        try {
            boolean removed = shipmentAreaService.removeSiteFromArea(address, areaName);
            return removed ? "Site removed successfully from area " + areaName + "." : "Site not found in area " + areaName + ".";
        } catch (SiteNotInArea e) {
            return e.getMessage();
        }
    }

    public String isSiteInShipmentArea(String address, String areaName) {
        boolean exists = shipmentAreaService.isSiteInShipmentArea(address, areaName);
        return exists ? "Site exists in the shipment area " + areaName + "." : "Site does not exist in the shipment area " + areaName + ".";
    }

    public String getAllAreas() {
        ArrayList<ShipmentArea> areas = shipmentAreaService.getAllAreas();
        return gson.toJson(areas);
    }

//    public String getAllShipmentAreas() {
//        ArrayList<ShipmentArea> shipmentAreas = shipmentAreaService.getShipmentAreas();
//        return gson.toJson(shipmentAreas);
//    }
}
