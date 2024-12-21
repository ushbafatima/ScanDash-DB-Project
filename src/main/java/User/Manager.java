package User;

public class Manager {
    private int managerID;
    private String firstName;
    private String lastName;
    private String phone;
    private String username;
    private String password;

    // Constructor to initialize the Manager object
    public Manager(int managerID, String firstName, String lastName, String phone, String username, String password) {
        this.managerID = managerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.username = username;
        this.password = password;
    }

    // Constructor to initialize the Manager object
    public Manager(int managerID, String username, String password) {
        this.managerID = managerID;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public int getManagerID() {
        return managerID;
    }

    public void setManagerID(int managerID) {
        this.managerID = managerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Method to authenticate the manager
    public boolean authenticate(String enteredUsername, String enteredPassword) {
        return this.username.equals(enteredUsername) && this.password.equals(enteredPassword);
    }

    // Method to return manager details as a string
    @Override
    public String toString() {
        return "Manager [ID=" + managerID + ", FirstName=" + firstName + ", LastName=" + lastName + ", Phone=" + phone + ", Username=" + username + "]";
    }
}
