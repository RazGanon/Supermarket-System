package Presentation;

import Domain.*;
import Service.ReportService;
import com.google.gson.Gson;

public class ReportController {
    private static ReportController instance;
    private ReportService reportService;
    private Gson gson;

    private ReportController() {
        this.reportService = new ReportService();
        this.gson = new Gson();
    }

    public static ReportController getInstance() {
        if (instance == null) {
            instance = new ReportController();
        }
        return instance;
    }

    // Methods for ProductsReport

    public String addProductsReport(ProductsReport productsReport) {
        if (productsReport.getProducts() == null || productsReport.getProducts().isEmpty()) {
            return "Error: Products report must contain at least one product.";
        }
        reportService.addProductsReport(productsReport);
        return "Products report added successfully.";
    }

    public String getProductsReportById(int id) {
        ProductsReport report = reportService.getProductsReportById(id);
        return report != null ? gson.toJson(report) : null;
    }

    public ReportService getReportService(){
        return reportService;
    }

    public String getAllProductsReports() {
        return gson.toJson(reportService.getProductsReports());
    }

    // Methods for TransportReport
    public String addTransportReport(String json) {
        TransportReport report = gson.fromJson(json, TransportReport.class);
        reportService.addTransportReport(report);
        return "Transport report added successfully.";
    }

    public String getTransportReportById(int id) {
        TransportReport report = reportService.getTransportReportById(id);
        if(report != null){
            return  gson.toJson(report);
        }
        return null;
    }

    public String getAllTransportReports() {
        return gson.toJson(reportService.getTransportReports());
    }

    public String addSiteProductsReport(String json) {
        SiteProductsReport report = gson.fromJson(json, SiteProductsReport.class);
        reportService.addSiteProductsReport(report);
        return "Site products report added successfully.";
    }

    public String getSiteProductsReportById(int id) {
        SiteProductsReport report = reportService.getSiteProductsReportById(id);
        return report != null ? gson.toJson(report) : null;
    }

    public String getAllSiteProductsReports() {
        return gson.toJson(reportService.getSiteProductsReports());
    }

}
