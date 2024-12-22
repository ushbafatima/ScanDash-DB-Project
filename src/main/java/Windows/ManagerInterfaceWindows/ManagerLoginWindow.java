package Windows.ManagerInterfaceWindows;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import DatabaseConfig.DBConnection;
import Windows.GeneralWindows.FirstWindow;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
// Import the Manager and ManagerAuthentication classes
import UserManagement.Manager;
import UserManagement.ManagerAuthentication;

import java.sql.SQLException;

import static Windows.AutoCloseMessageDialog.showMessage;

public class ManagerLoginWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        EventQueue.invokeLater(() -> {
            try {
                ManagerLoginWindow frame = new ManagerLoginWindow();
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ManagerLoginWindow() {
        // Start a thread to connect to the database when the window is launched
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Connect to the database in the background
                ManagerAuthentication.conn = DBConnection.connectToDB();  // Connect to DB
            }
        }).start();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        ManagerLoginWindow.this,
                        "Do you want to quit Scan Dash?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        setBounds(500, 200, 500, 400);
        setResizable(true);

        // Set layout and content pane
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new GridBagLayout());
        setContentPane(contentPane);

        setTitle("Manager Login");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());

        // Create a layout to divide the window into two regions:
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Blue banner (top region)
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 0, 100));
        topPanel.setPreferredSize(new java.awt.Dimension(0, 60)); // Make the banner thicker
        topPanel.setLayout(new GridBagLayout());

        JLabel managerLabel = new JLabel("MANAGER");
        managerLabel.setForeground(Color.WHITE);
        managerLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        topPanel.add(managerLabel);

        // Add topPanel to the contentPane
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across the full width
        gbc.weightx = 1.0;
        gbc.weighty = 0.0; // Don't take vertical space
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(topPanel, gbc);

        // Center region for form fields and buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setOpaque(false); // Keep it transparent to show background color

        GridBagConstraints centerGbc = new GridBagConstraints();
        centerGbc.insets = new Insets(10, 10, 10, 10);
        centerGbc.fill = GridBagConstraints.HORIZONTAL;

        // Form components
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setForeground(Color.white);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(Color.white);

        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // Get the entered username and password
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                if (isEmptyFields()) {
                    JOptionPane.showMessageDialog(null, "Please Fill All Fields", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (!username.endsWith(".scandash")) {
                    JOptionPane.showMessageDialog(null, "Username must end with '.scandash'", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Attempt to fetch manager details from the database
                    Manager manager = ManagerAuthentication.getManagerFromDB(username);

                    if (!ManagerAuthentication.managerFound(manager)) {
                        JOptionPane.showMessageDialog(null, "User Not Found", "Error", JOptionPane.ERROR_MESSAGE);
                        clearFields();
                        return;
                    }

                    // Authenticate the manager
                    if (ManagerAuthentication.authenticateManager(manager,username, password)) {
                        showMessage(null, "Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE, 1000);  // 2000ms (2 seconds)
                        setVisible(false);
                        new ManagerTasksWindow().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Password", "Error", JOptionPane.ERROR_MESSAGE);
                        passwordField.setText("");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });


        JButton closeButton = new JButton("Close");
        closeButton.setBackground(Color.red);
        closeButton.setForeground(Color.black);
        closeButton.addActionListener(e -> {
            dispose();
            new FirstWindow().setVisible(true);
        });

        // Add components to the center panel
        centerGbc.gridx = 0;
        centerGbc.gridy = 0;
        centerGbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(usernameLabel, centerGbc);

        centerGbc.gridx = 1;
        centerGbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(usernameField, centerGbc);

        centerGbc.gridx = 0;
        centerGbc.gridy = 1;
        centerGbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(passwordLabel, centerGbc);

        centerGbc.gridx = 1;
        centerGbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(passwordField, centerGbc);

        // Add buttons
        centerGbc.gridx = 0;
        centerGbc.gridy = 2;
        centerGbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(loginButton, centerGbc);

        centerGbc.gridx = 1;
        centerGbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(closeButton, centerGbc);

        // Add center panel to the content pane
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0; // Allow it to take all remaining vertical space
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(centerPanel, gbc);

        setLocationRelativeTo(null);
    }

    public Boolean isEmptyFields() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        return username.isEmpty() || password.isEmpty();
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}