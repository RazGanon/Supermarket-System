package Domain;

public class SiteProductsReport {
    private Site site;
    private ProductsReport productsReport;

    public SiteProductsReport(Site site, ProductsReport productsReport) {
        this.site = site;
        this.productsReport = productsReport;
    }

    public Site getSite() {
        return site;
    }

    public ProductsReport getProductsReport() {
        return productsReport;
    }
}
