package Domain;
import java.util.ArrayList;
public class ProductsReport extends Report {
    private ArrayList<Product> products;

    public ProductsReport(ArrayList<Product> p_list){
        super();
        this.products = new ArrayList<>();
        this.products.addAll(p_list);
    }
    // Getters and Setters
    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        double productWeight = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("ProductsReport ID: ").append(getReportId()).append("\n");
        sb.append("Products: ");
        for (Product product : products) {
            sb.append(product.getName()).append(" (ID: ").append(product.getId()).append(", Weight: ").append(product.getWeight()).append("), ");
            productWeight+=product.getWeight();
        }
        sb.setLength(sb.length() - 2);  // Remove the trailing comma and space
        sb.append("\nWeight: ").append(productWeight).append("\n");
        return sb.toString();
    }
}