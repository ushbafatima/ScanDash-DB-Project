package ProductManagement;

// CosmeticsProduct class (subclass of Product)
public class CosmeticsProduct extends Product {
    private String brand;
    private String ingredients;

    // Constructor
    public CosmeticsProduct(String name, String prodID, double price, int quantity, double discount, String brand, String ingredients) {
        super(name, prodID, price, quantity, discount, "Cosmetics");
        this.brand = brand;
        this.ingredients = ingredients;
    }

    public CosmeticsProduct() {

    }

    // Getters and setters
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}
