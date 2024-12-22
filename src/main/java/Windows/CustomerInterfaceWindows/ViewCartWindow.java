package Windows.CustomerInterfaceWindows;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.sql.*;

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
        String[] columnNames = {"Product ID", "Product Name", "Product Cost", "Product Quantity", "Product Discount"};
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
        btnClearCart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        ViewCartWindow.this,
                        "Are you sure you wish to clear the cart?",
                        "Clear Cart Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (response == JOptionPane.YES_OPTION) {
                    //CartManagement.clearCart();
                    clearCart();
                }
            }
        });

        JButton btnCheckout = new JButton("Checkout");
        btnCheckout.setBackground(new Color(0, 180, 0));
        btnCheckout.setForeground(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridy = 0;
        buttonPanel.add(btnCheckout, gbc);
        btnCheckout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //CustomerBillWindow billWindow = new CustomerBillWindow();
                //billWindow.setVisible(true);
                dispose();            }
        });

        JButton btnBackToShopping = new JButton("Back to Shopping");
        btnBackToShopping.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                new CustomerShoppingWindow().setVisible(true);
                dispose();
            }
        });
        btnBackToShopping.setPreferredSize(new Dimension(160, btnBackToShopping.getPreferredSize().height));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        buttonPanel.add(btnBackToShopping, gbc);

        setLocation(450, 100);
        // Fetch and display the data from the database
        //fetchDataFromDatabase();
        if (tableModel.getRowCount()==0) {
            JOptionPane.showMessageDialog(null, "Your Cart is Empty", "Oops",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        setLocationRelativeTo(null);
    }
    /*private void fetchDataFromDatabase() {
        final String DB_NAME = "user";
        final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
        final String USERNAME = "root";
        final String PASSWORD = "MySQL@2773778";
        String sql = "SELECT prod_id, prod_name, price, quantity, discount FROM cart.cart_items"; // Corrected SQL query

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            try {
                ResultSetMetaData metaData = rs.getMetaData();

                while (rs.next()) {
                    Object[] rowData = new Object[metaData.getColumnCount()];
                    for (int i = 0; i < metaData.getColumnCount(); i++) {
                        rowData[i] = rs.getObject(i + 1);
                    }
                    tableModel.addRow(rowData);
                }

            } catch (SQLException e1) {
                e1.printStackTrace();
            } finally {
                statement.close();
                rs.close();
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }*/

    private void clearCart() {
        tableModel.setRowCount(0); // Clear all rows in the table
        // Additional logic for clearing the cart if needed
    }
}