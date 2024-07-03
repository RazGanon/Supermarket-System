package Domain;

import Data.ReportsDAO;
import java.util.ArrayList;

public class ReportController {
    private ReportsDAO reportsDAO;

    public ReportController() {
        this.reportsDAO = ReportsDAO.getInstance();
    }

    // Methods for TransportReport
    public void addTransportReport(TransportReport report) {
        reportsDAO.addTransportReport(report);
    }
    public void linkSiteProductsToReport(String siteAddress, int reportId){
        reportsDAO.linkSiteProductsToReport(siteAddress,reportId);
    }
    public TransportReport getTransportReportById(int id) {
        return reportsDAO.getTransportReportById(id);
    }

    public boolean removeProductsFromSite(String siteAddress , int productId){
        return reportsDAO.removeProductsFromSite(siteAddress,productId);
    }
    public void updateTransportReport(int reportId, String changesMade){
        boolean bool = reportsDAO.updateTransportReport(reportId,changesMade);
        if(bool){
            System.out.println("Update successes");
        }
    }
    public void updateProductQuantity(String siteAddress, int productId, int newQuantity){
         reportsDAO.updateProductQuantity(siteAddress,productId,newQuantity);
    }
    public void addSiteProducts(SiteProducts report) {
        reportsDAO.addSiteProducts(report);
    }

    public ArrayList<SiteProducts> getSiteProductsById(int id) {
        return reportsDAO.getSiteProductsByReportId(id);
    }

    public String displaySiteProductsById(int id){
        ArrayList<SiteProducts> siteProducts = getSiteProductsById(id);
        if (siteProducts == null || siteProducts.isEmpty()) {
            return "No sites or products found for the given transport ID.";
        }
        StringBuilder result = new StringBuilder();
        for (SiteProducts sp : siteProducts) {
            result.append("Site: ").append(sp.getSite().getAddress()).append("\n");
            for (Product product : sp.getProducts()) {
                result.append("  Product ID: ").append(product.getId())
                        .append(", Name: ").append(product.getName())
                        .append(", Quantity: ").append(product.getQuantity())
                        .append("\n");
            }
        }
        return result.toString();
    }
    public SiteProducts getSiteProducts(String address) {
        return reportsDAO.getSiteProductsByAddress(address);
    }
    public void resetTransportReportId(String siteAddress){
        reportsDAO.resetTransportReportId(siteAddress);
    }

}
