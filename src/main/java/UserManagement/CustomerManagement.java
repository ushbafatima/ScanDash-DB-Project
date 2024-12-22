package UserManagement;

import ProductManagement.DBConnection;

import java.sql.*;

public class CustomerManagement {
    // Connection object to interact with the database
    public static Connection conn;
    // Method to get customer details by phone number
    public static Customer getCustomerFromDB(String phoneNumber) {
        Customer customer = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = conn.prepareStatement("SELECT * FROM CUSTOMER WHERE phone=?");
            pst.setString(1, phoneNumber);  // Set the phone number parameter
            rs = pst.executeQuery();

            if (rs.next()) {
                // Fetching Customer details
                String customerID = rs.getString("customerID");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String phone = rs.getString("phone");
                String cardID = rs.getString("cardID");

                // Create Card object for the customer
                Card card = CardManagement.getCardFromDB(cardID);  // Assume we have a method to get Card details by cardID

                // Creating Customer object
                customer = new Customer(customerID, firstName, lastName, phone, card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources to prevent memory leaks
            closeResources(pst, rs);
        }

        return customer;
    }

    public static boolean addCustomerToDB(Customer customer) {
        // Check if the customer already exists using the getCustomerFromDB method
        Customer existingCustomer = getCustomerFromDB(customer.getPhoneNumber());
        if (existingCustomer != null) {
            // Customer with the same phone number already exists
            return false;
        }

        PreparedStatement pst = null;
        try {
            // Insert customer into the database
            pst = conn.prepareStatement("INSERT INTO CUSTOMER (firstName, lastName, phone, cardID) VALUES (?, ?, ?, ?)");
            pst.setString(1, customer.getFirstName()); // firstName
            pst.setString(2, customer.getLastName()); // lastName
            pst.setString(3, customer.getPhoneNumber()); // phone
            pst.setString(4, customer.getCard().getCardID()); // cardID
            pst.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            // Close resources
            try {
                if (pst != null) pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean removeCustomerFromDB(Customer customer) {
        // Get the customer from the database using the phone number
        Customer existingCustomer = getCustomerFromDB(customer.getPhoneNumber());

        if (existingCustomer == null) {
            return false; // Customer does not exist
        }

        PreparedStatement pst = null;
        try {
            // Delete customer from database
            pst = conn.prepareStatement("DELETE FROM CUSTOMER WHERE phone = ?");
            pst.setString(1, customer.getPhoneNumber());
            pst.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(pst, null);
        }
    }

    // Method to close database resources (PreparedStatement, ResultSet)
    private static void closeResources(PreparedStatement pst, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
