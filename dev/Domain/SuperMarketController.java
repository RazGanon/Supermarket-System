package Domain;

import java.util.ArrayList;
import java.util.List;

import Data.SuperMarketDao;

public class SuperMarketController {

    private static SuperMarketDao smd = SuperMarketDao.getInstance();
    private static List<SuperMarket> superMarkets = new ArrayList<>();

    // Method to add all supermarkets to the system
    public static void addAllSuperMarketsToSystem() {
        List<SuperMarket> newSuperMarkets = smd.getAllSuperMarkets();
        superMarkets = new ArrayList<>(newSuperMarkets);
    }

    // Method to get all supermarkets
    public static List<SuperMarket> getAllSuperMarkets() {
        return new ArrayList<>(superMarkets); // Return a copy of the list to avoid external modifications
    }
    // Method to print all supermarkets
    public static void printAllSuperMarkets() {
        for (SuperMarket sm : superMarkets) {
            System.out.println("Address: " + sm.getAddress() + ", Manager Name: " + sm.getManagerName());
        }
    }
}
