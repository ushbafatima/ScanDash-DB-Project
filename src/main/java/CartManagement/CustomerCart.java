package CartManagement;

import ProductManagement.Product;
import DatabaseConfig.DBConnection;
import ProductManagement.ProductManagement;
import UserManagement.CurrentCustomerSession;

import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerCart {
    private static Map<Product, Integer> cart;

    public CustomerCart() {
        cart = new HashMap<>();
    }

    public static boolean addToCart(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return false;
        }

        // Check if product exists in inventory and is available
        Product inventoryProduct = ProductManagement.findProductInInventory(product.getProdID());
        if (inventoryProduct == null || quantity > inventoryProduct.getQuantity()) {
            return false;
        }

        // Update cart and reduce inventory quantity
        int currentQuantity = getCurrentQuantityInCart(product);
        cart.put(product, currentQuantity + quantity);

        inventoryProduct.setQuantity(inventoryProduct.getQuantity() - quantity);
        return ProductManagement.updateProductInDB(inventoryProduct);
    }

    public static boolean removeFromCart(Product product, int quantity) {
        if (product == null || !cart.containsKey(product)) {
            return false;
        }

        int currentQuantity = cart.get(product);
        if (quantity > currentQuantity) {
            return false;
        }

        // Update cart and return the quantity back to inventory
        if (quantity == currentQuantity) {
            cart.remove(product);
        } else {
            cart.put(product, currentQuantity - quantity);
        }

        Product inventoryProduct = ProductManagement.findProductInInventory(product.getProdID());
        if (inventoryProduct != null) {
            inventoryProduct.setQuantity(inventoryProduct.getQuantity() + quantity);
            return ProductManagement.updateProductInDB(inventoryProduct);
        }
        return false;
    }

    public static void clearCart() {
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            // Return all items in cart to inventory
            Product inventoryProduct = ProductManagement.findProductInInventory(product.getProdID());
            if (inventoryProduct != null) {
                inventoryProduct.setQuantity(inventoryProduct.getQuantity() + quantity);
                ProductManagement.updateProductInDB(inventoryProduct);
            }
        }
        cart.clear();
    }

    public static Map<Product, Integer> getCart() {
        return new HashMap<>(cart);
    }

    public static double calculateBill() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double itemPrice = product.getPrice() * (1 - product.getDiscount() / 100);
            total += itemPrice * quantity;
        }
        return total;
    }

    public static boolean checkout() {
        if (cart.isEmpty()) {
            return false;
        }

        // Get current card ID from session
        String cardId = CurrentCustomerSession.getInstance().getCurrentCardId();
        if (cardId == null) {
            return false;
        }

        try (Connection conn = DBConnection.connectToDB()) {
            if (conn == null) {
                return false;
            }

            conn.setAutoCommit(false);

            try {
                // Calculate total bill
                double totalAmount = calculateBill();

                // Insert sale record into salesPerCard
                String insertSaleQuery = "INSERT INTO \"salesPerCard\" (\"cardid\", \"date\", \"totalAmount\") VALUES (?, CURRENT_DATE, ?)";
                try (PreparedStatement pst = conn.prepareStatement(insertSaleQuery)) {
                    pst.setString(1, cardId);
                    pst.setDouble(2, totalAmount);
                    pst.executeUpdate();
                }

                // Deduct final quantities from inventory
                for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
                    Product product = entry.getKey();
                    int quantity = entry.getValue();

                    Product inventoryProduct = ProductManagement.findProductInInventory(product.getProdID());
                    if (inventoryProduct != null) {
                        int remainingQuantity = inventoryProduct.getQuantity() - quantity;
                        inventoryProduct.setQuantity(remainingQuantity);
                        ProductManagement.updateProductInDB(inventoryProduct);
                    }
                }

                conn.commit();
                clearCart(); // Clear the cart after successful checkout
                return true;

            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getCurrentQuantityInCart(Product product) {
        return cart.getOrDefault(product, 0);
    }

    public static boolean isProductInCart(Product product) {
        return cart.containsKey(product);
    }
}
