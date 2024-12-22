package Windows.CustomerInterfaceWindows;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import ProductManagement.DBConnection;
import CardManagement.Card;
import CardManagement.CardManagement;
import UserManagement.CustomerAuthentication;
import Windows.AutoCloseMessageDialog;
import Windows.GeneralWindows.FirstWindow;
import com.formdev.flatlaf.FlatDarkLaf;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class CustomerCardPinWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField PINField;
    private int attemptsLeft = 3; // Track the remaining login attempts

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        EventQueue.invokeLater(() -> {
            try {
                CustomerCardPinWindow frame = new CustomerCardPinWindow(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public CustomerCardPinWindow(String cardID) {
        // Start a thread to connect to the database when the window is launched
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Connect to the database in the background
                CardManagement.conn = DBConnection.connectToDB();  // Connect to DB
            }
        }).start();


        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent default close operation
        // Add a window listener to handle the window close event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        CustomerCardPinWindow.this,
                        "Do you want to quit Scan Dash?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (response == JOptionPane.YES_OPTION) {
                    // Exit the program
                    System.exit(0);
                }
                // If the response is NO_OPTION, do nothing and stay in the current window
            }
        });

        setBounds(500, 200, 500, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setTitle("Customer Login");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());

        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Add the blue panel with the customer label
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 0, 100)); // Dark blue color
        topPanel.setBounds(0, 0, 500, 60);
        contentPane.add(topPanel);
        topPanel.setLayout(null);

        JLabel customerLabel = new JLabel("CUSTOMER");
        customerLabel.setForeground(Color.WHITE);
        customerLabel.setFont(new Font("Tahoma", Font.BOLD, 24)); // Bigger and bold font
        customerLabel.setBounds(20, 10, 200, 40); // Position at top left
        topPanel.add(customerLabel);

        JLabel PINLabel = new JLabel("PIN");
        PINLabel.setForeground(Color.white);
        PINLabel.setBounds(130, 150, 80, 13);
        contentPane.add(PINLabel);

        PINField = new JPasswordField();
        PINField.setBounds(200, 150, 180, 19);
        contentPane.add(PINField);
        PINField.setColumns(10);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(200, 230, 85, 21);
        loginButton.setBackground(new Color(0, 180, 0));
        loginButton.setForeground(Color.black);
        contentPane.add(loginButton);
        loginButton.addActionListener(e -> {
            // Get the entered PIN
            String enteredPIN = PINField.getText();

            if (isEmptyFields()) {
                JOptionPane.showMessageDialog(null, "Please Fill All Fields", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            Card card;
            try {
                card = CardManagement.getCardFromDB(cardID); // Fetch card from database
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            if (CustomerAuthentication.authenticateCard(card, cardID, enteredPIN)) {
                setVisible(false);
                new CustomerShoppingWindow().setVisible(true);
            } else {
                attemptsLeft--; // Decrement the attempts
                if (attemptsLeft > 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Invalid PIN. Attempts left: " + attemptsLeft,
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    PINField.setText("");
                } else {
                    // No attempts left
                    AutoCloseMessageDialog.showMessage(this, "Maximum attempts reached. Logging out...", "Error", JOptionPane.ERROR_MESSAGE, 1000);

                    dispose(); // Close the window
                    new FirstWindow().setVisible(true); // Redirect to the first window
                }
            }
        });

        JButton closeButton = new JButton("Close");
        closeButton.setBounds(300, 230, 85, 21);
        closeButton.setBackground(Color.red);
        closeButton.setForeground(Color.black);
        contentPane.add(closeButton);
        closeButton.addActionListener(e -> {
            dispose();
            new FirstWindow().setVisible(true);
        });

        // Center the window relative to the screen
        setLocationRelativeTo(null);
    }

    public Boolean isEmptyFields() {
        String username = PINField.getText();
        username = username.replaceAll("\\s", "");
        return username.equals("");
    }
}
