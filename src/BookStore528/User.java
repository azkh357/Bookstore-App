package BookStore528;

/**
 * Base class for all users (Owner and Customer).
 * As per the class diagram, this holds the common login info.
 */
public abstract class User { // Abstraction (can't create a "User" object but can extend to other classes)
    protected String username; 
    protected String password; // Protected so that subclasses can access but other classes can't

    public User(String username, String password) { // Constructoro
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; } // Getters for username and password of each user
    public String getPassword() { return password; }
}
