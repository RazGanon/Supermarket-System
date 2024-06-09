package Service;

import Domain.Report;
import Domain.ProductsReport;
import Domain.TransportReport;
import Domain.SiteProductsReport;

import java.util.ArrayList;


public class ReportService {
    private ArrayList<ProductsReport> productsReports;
    private ArrayList<TransportReport> transportReports;
    private ArrayList<SiteProductsReport> siteProductsReports;

    public ReportService() {
        this.productsReports = new ArrayList<>();
        this.transportReports = new ArrayList<>();
        this.siteProductsReports = new ArrayList<>();
    }

    // Methods for ProductsReport
    public void addProductsReport(ProductsReport report) {
        productsReports.add(report);
    }

    public ProductsReport getProductsReportById(int id) {
        for (ProductsReport report : productsReports) {
            if (report.getReportId() == id) {
                return report;
            }
        }
        return null;
    }

    public ArrayList<ProductsReport> getProductsReports() {
        return productsReports;
    }

    // Methods for TransportReport
    public void addTransportReport(TransportReport report) {
        transportReports.add(report);
    }

    public TransportReport getTransportReportById(int id) {
        for (TransportReport report : transportReports) {
            if (report.getReportId() == id) {
                return report;
            }
        }
        return null;
    }

    public ArrayList<TransportReport> getTransportReports() {
        return transportReports;
    }

    // Methods for SiteProductsReport
    public void addSiteProductsReport(SiteProductsReport report) {
        siteProductsReports.add(report);
    }

    public SiteProductsReport getSiteProductsReportById(int id) {
        for (SiteProductsReport report : siteProductsReports) {
            if (report.getProductsReport().getReportId() == id) {
                return report;
            }
        }
        return null;
    }

    public ArrayList<SiteProductsReport> getSiteProductsReports() {
        return siteProductsReports;
    }
}
