package Domain;

import java.util.ArrayList;

public class SiteProducts {
    private Site site;
    private ArrayList<Product> items;

    public SiteProducts(Site site, ArrayList<Product> products) {
        this.site = site;
        this.items = products;
    }

    public Site getSite() {
        return site;
    }

    public ArrayList<Product> getProducts() {
        return items;

    }
    public void setSite(Site site){
        site = site;
    }
}
