package BookStore528;

public interface CustomerStatus { // This is an interface because polymorphism (can easily switch between gold and silver with the state design pattern)
    String getStatusName(); // Returns "Gold" or "Silver"
    double calcCost(double cost); // Calculates the cost depending on the customer's status
    void updateStatus(Customer c); // Changes the status depending on the points held by the customer
}

// These methods are common among states (statusus), Silver and Gold. This class does not need any logic by itself.