package Windows.CustomerInterfaceWindows;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import ProductManagement.ProductScanner;
import com.formdev.flatlaf.FlatDarkLaf;

import java.util.Timer;
import java.util.TimerTask;

public class ScanProductWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private static String productUID;
    private String action;

    public ScanProductWindow() {
        setUndecorated(true);

        this.action = action;
        setBounds(500, 200, 500, 450);
        setTitle("Scan Product");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());

        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 0, 100)); // Dark blue color
        topPanel.setBounds(0, 0, 500, 60);
        contentPane.add(topPanel);
        topPanel.setLayout(null);

        JLabel scanLabel = new JLabel("SCAN PRODUCT");
        scanLabel.setForeground(Color.WHITE);
        scanLabel.setFont(new Font("Tahoma", Font.BOLD, 24)); // Bigger and bold font
        scanLabel.setBounds(20, 10, 200, 40); // Position at top left
        topPanel.add(scanLabel);

        JLabel ScanProductIcon = new JLabel("");
        ScanProductIcon.setHorizontalAlignment(SwingConstants.CENTER);
        ScanProductIcon.setBackground(Color.GREEN);
        ImageIcon icon1 = new ImageIcon(this.getClass().getResource("/Images/ScanProduct.gif"));
        ScanProductIcon.setIcon(icon1);
        ScanProductIcon.setBounds(116, 116, 268, 200);
        contentPane.add(ScanProductIcon);

        // Red close button
        JButton closeButton = new JButton("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.RED);
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setBounds(460, 10, 30, 30); // Adjust size and position
        closeButton.addActionListener(e -> {
            dispose(); // Close the current window
            SwingUtilities.invokeLater(() -> {
                new CustomerShoppingWindow().setVisible(true);
                dispose();
            });
        });topPanel.add(closeButton);


        setLocationRelativeTo(null);
        simulateProductScan();

    }

    private void simulateProductScan() {
        try {
            // Change the theme to Flat Dark
            UIManager.setLookAndFeel(new FlatDarkLaf());
            // Or change to other LaF themes accordingly
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        System.out.println("Hi From Simulate Scan");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    scanProduct();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, 1000); // Simulate scanning process after 2 seconds
    }

    /**
     * Method to handle the scanning process. This should be replaced with actual
     * scanning logic.
     */
    private void scanProduct() throws Exception {
        while (true) {
            productUID = ProductScanner.getProductID();
            if (!isValidUID(productUID)) {
                JOptionPane.showMessageDialog(null, "Product not Scanned Properly", "Scan Again", JOptionPane.INFORMATION_MESSAGE);

            }else {
                break;
            }
        }

        System.out.println("Product scanned: " + productUID);
        dispose(); // Close the ScanProductWindow after scanning
        //handleAction();

    }

    public static String getUID() {
        return productUID;
    }

    /*private void handleAction() {
        // Based on the action, open the respective window
        String prodID = ScanProductWindow.getUID();
        //Product scannedProduct = ProductManagement.findProductinDB(prodID);
        dispose();
        switch (action) {
            case "Add":
                if (scannedProduct != null) {
                    JOptionPane.showMessageDialog(null, "Product Already Exists", "Info", JOptionPane.INFORMATION_MESSAGE);
                    new ManageInventoryWindow().setVisible(true);
                    return;
                }
                new AddProductWindow().setVisible(true);
                break;
            case "Remove":
                if (scannedProduct == null) {
                    JOptionPane.showMessageDialog(null, "Product Does not Exist", "Info", JOptionPane.INFORMATION_MESSAGE);
                    new ManageInventoryWindow().setVisible(true);
                    return;
                }
                new RemoveProductWindow().setVisible(true);
                break;
            case "Update":
                if (scannedProduct == null) {
                    JOptionPane.showMessageDialog(null, "Product Does not Exist", "Info", JOptionPane.INFORMATION_MESSAGE);
                    new ManageInventoryWindow().setVisible(true);
                    return;
                }
                new UpdateDetailsWindow().setVisible(true);
                break;
            case "Customer":
                if (scannedProduct == null) {
                    JOptionPane.showMessageDialog(null, "Product Does not Exist", "Info", JOptionPane.INFORMATION_MESSAGE);
                    new CustomerShoppingWindow().setVisible(true);
                    return;
                }
                new ShopProductsWindow().setVisible(true);


        }
    }*/

    public Boolean isValidUID(String UID) {
        if (UID.length() < 8) {
            return false;
        }
        return true;
    }
}