package Windows;

import com.formdev.flatlaf.FlatDarkLaf;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class CustomerShoppingWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CustomerShoppingWindow frame = new CustomerShoppingWindow();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public CustomerShoppingWindow() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent default close operation
        // Add a window listener to handle the window close event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(CustomerShoppingWindow.this,
                        "Do you want to quit Scan Dash?", "Confirm Exit", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    // Exit the program
                    System.exit(0);
                }
                // If the response is NO_OPTION, do nothing and stay in the current window
            }
        });
        setBounds(500, 200, 500, 400);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        setLocation(x,y);
        setResizable(false); // Disable resizing
        setTitle("Shopping");

        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());

        // Center the window
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 0, 100)); // Dark blue color
        topPanel.setBounds(0, 0, 500, 60);
        contentPane.add(topPanel);
        topPanel.setLayout(null);

        JLabel managerLabel = new JLabel("CUSTOMER");
        managerLabel.setForeground(Color.WHITE);
        managerLabel.setFont(new Font("Tahoma", Font.BOLD, 24)); // Bigger and bold font
        managerLabel.setBounds(20, 10, 200, 40); // Position at top left
        topPanel.add(managerLabel);

        JButton displayCartbtn = new JButton("Display Cart");
        displayCartbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //new ViewCartWindow().setVisible(true);
                dispose();
            }

        });
        displayCartbtn.setForeground(Color.white);
        displayCartbtn.setBackground(new Color(0, 0, 100));
        displayCartbtn.setBounds(180, 150, 150, 50);
        displayCartbtn.setFocusPainted(false);
        contentPane.add(displayCartbtn);

        JButton scanProductbtn = new JButton("Scan Product");
        scanProductbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //new ScanProductWindow("Customer").setVisible(true);
                setVisible(false);
            }
        });
        scanProductbtn.setForeground(Color.white);
        scanProductbtn.setBounds(180, 220, 150, 50);
        scanProductbtn.setBackground(new Color(0, 0, 100));
        scanProductbtn.setFocusPainted(false);
        contentPane.add(scanProductbtn);

        RoundedButton logoutButton = new RoundedButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new FirstWindow().setVisible(true);
            }
        });
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(Color.RED);
        logoutButton.setBounds(408, 70, 68, 30);
        contentPane.add(logoutButton);
    }

}