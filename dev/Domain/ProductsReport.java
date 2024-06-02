package Domain;
import java.util.List;
import java.util.ArrayList;
public class ProductsReport extends Report {
    private List<String> products;
    ProductsReport(List<String> p_list){
        super();
        this.products = new ArrayList<>();
        this.products.addAll(p_list);

    }
    // Getters and Setters
    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    // Optional: method to add a single product
    public void addProduct(String product) {
        this.products.add(product);
    }
    public void deleteProduct(String product){
        this.products.remove(product);
    }
}