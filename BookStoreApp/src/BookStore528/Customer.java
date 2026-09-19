package BookStore528;

/**
 * Handles the customer's account and membership status.
 * Uses the State Design Pattern to delegate logic to Status classes.
 */
public class Customer extends User { // Customer is a type of User
    private int points;            // Tracks cumulative points
    private CustomerStatus status; // Holds either Gold or Silver status (from interface)

    public Customer(String username, String password) { // Constructor
        super(username, password); // Takes from User class
        this.points = 0;           // Everyone starts at 0 points
        this.status = new SilverStatus(); // Initial state
    }

    // Standard getters for name, pass (inherited from User) and points
    public int getPoints() { return points; }

    // Every time points change, delegate the state check to the current state object
    public void setPoints(int points) {
        this.points = points;
        status.updateStatus(this); // Uses state design for customer status
    }

    // Delegation to the State object to get the current status string
    public String getStatus() {
        return status.getStatusName();
    }
    
    // Allows Main.java to access the state object for cost calculations
    public CustomerStatus getStatusObj() {
        return status;
    }
    
    // Allows the State objects to transition the customer's status
    public void setStatus(CustomerStatus status) {
        this.status = status;
    }
}
