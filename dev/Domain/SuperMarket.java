package Domain;

import java.util.ArrayList;
import java.util.List;

public class SuperMarket {
    private String address;
    private String managerName;
    private static List<SuperMarket> superMarkets = new ArrayList<>();

    public SuperMarket(String address, String managerName) {
        this.address = address;
        this.managerName = managerName;
    }

    @Override
    public String toString() {
        return "Super Market Address: " + address +
                ", The Manager Of This Branch: " + managerName;
    }

    // Method to add a supermarket to the list
    public static void addSuperMarket(SuperMarket superMarket) {
        superMarkets.add(superMarket);
    }

    // Method to get all supermarkets
    public static List<SuperMarket> getAllSuperMarkets() {
        return new ArrayList<>(superMarkets);
    }

    // Method to find a supermarket by address
    public static SuperMarket findSuperMarketByAddress(String address) {
        for (SuperMarket superMarket : superMarkets) {
            if (superMarket.address.equals(address)) {
                return superMarket;
            }
        }
        return null;
    }
    // Method to remove a supermarket by address
    public static boolean removeSuperMarketByAddress(String address) {
        SuperMarket superMarket = findSuperMarketByAddress(address);
        if (superMarket != null) {
            superMarkets.remove(superMarket);
            return true;
        }
        return false;
    }
    // Method to list all supermarkets
    public static void listAllSuperMarkets() {
        for (SuperMarket superMarket : superMarkets) {
            System.out.println(superMarket);
        }
    }

    public String getAddress() {
        return this.address;
    }
}
