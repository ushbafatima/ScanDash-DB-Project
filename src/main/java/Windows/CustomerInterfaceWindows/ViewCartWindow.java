package Windows.CustomerInterfaceWindows;

import ProductManagement.Product;
import com.formdev.flatlaf.FlatDarkLaf;
import CartManagement.CustomerCart;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

public class ViewCartWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        EventQueue.invokeLater(() -> {
            try {
                ViewCartWindow frame = new ViewCartWindow();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ViewCartWindow() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent default close operation
        // Add a window listener to handle the window close event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(ViewCartWindow.this,
                        "Do you want to quit Scan Dash?", "Confirm Exit", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    // Exit the program
                    System.exit(0);
                }
                // If the response is NO_OPTION, do nothing and stay in the current window
            }
        });
        setBounds(500, 200, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setTitle("View Cart");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());

        setContentPane(contentPane);
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(0, 0));

        // Add the blue panel with the title label
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 0, 100));
        topPanel.setPreferredSize(new Dimension(800, 60));
        contentPane.add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(null);

        JLabel titleLabel = new JLabel("YOUR CART");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLabel.setBounds(20, 10, 200, 40);
        topPanel.add(titleLabel);

        // Create the table model with column names
        String[] columnNames = {"Product ID", "Product Name", "Price", "Quantity", "Discount", "Total"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);

        // Create the scroll pane and add the table to it
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Create the button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnClearCart = new JButton("Clear Cart");
        btnClearCart.setBackground(Color.RED);
        btnClearCart.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(btnClearCart, gbc);
        btnClearCart.addActionListener(e -> handleClearCart());

        JButton btnCheckout = new JButton("Checkout");
        btnCheckout.setBackground(new Color(0, 180, 0));
        btnCheckout.setForeground(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridy = 0;
        buttonPanel.add(btnCheckout, gbc);
        btnCheckout.addActionListener(e -> handleCheckout());

        JButton btnBackToShopping = new JButton("Back to Shopping");
        btnBackToShopping.addActionListener(e -> handleBackToShopping());
        btnBackToShopping.setPreferredSize(new Dimension(160, btnBackToShopping.getPreferredSize().height));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        buttonPanel.add(btnBackToShopping, gbc);

        setLocation(450, 100);
        loadCartData();

        if (tableModel.getRowCount()==0) {
            JOptionPane.showMessageDialog(null, "Your Cart is Empty", "Oops",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        setLocationRelativeTo(null);
    }

    private void loadCartData() {
        tableModel.setRowCount(0);
        Map<Product, Integer> cartItems = CustomerCart.getCart();

        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            Object[] rowData = getObjects(entry);
            tableModel.addRow(rowData);
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Your Cart is Empty", "Oops",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static Object[] getObjects(Map.Entry<Product, Integer> entry) {
        Product product = entry.getKey();
        int quantity = entry.getValue();
        double itemPrice = product.getPrice() * (1 - product.getDiscount()/100);
        double total = itemPrice * quantity;

        Object[] rowData = {
                product.getProductID(),
                product.getProductName(),
                String.format("%.2f", product.getPrice()),
                quantity,
                String.format("%.0f%%", product.getDiscount()),
                String.format("%.2f", total)
        };
        return rowData;
    }

    private void handleClearCart() {
        int response = JOptionPane.showConfirmDialog(
                ViewCartWindow.this,
                "Are you sure you wish to clear the cart?",
                "Clear Cart Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (response == JOptionPane.YES_OPTION) {
            CustomerCart.clearCart();
            clearCart();
        }
    }

    private void handleCheckout() {
        if (CustomerCart.checkout()) {
            JOptionPane.showMessageDialog(ViewCartWindow.this,
                    "Checkout successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            CustomerBillWindow billWindow = new CustomerBillWindow();
            billWindow.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(ViewCartWindow.this,
                    "Checkout failed. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleBackToShopping() {
        new CustomerShoppingWindow().setVisible(true);
        dispose();
    }


    private void clearCart() {
        tableModel.setRowCount(0); // Clear all rows in the table
        // Additional logic for clearing the cart if needed
    }
}