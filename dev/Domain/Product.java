package Domain;

public class Product {
    private String name;
    private int id;
    private double weight;
    private int quantity;
    public Product(String name,int id,double weight,int quantity){
        this.name = name;
        this.id = id;
        this.weight = weight;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return name + " (ID: " + id + ", Weight: " + weight + ")";
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }
}
