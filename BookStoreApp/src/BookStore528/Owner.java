package BookStore528;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Manages the store's lists of books and customers.
 * As per the instructions, the owner has the 'admin/admin' credentials.
 */
public class Owner extends User { // Inheritance
    private final FileHandler files = new FileHandler(); // For reading and writing data
    private final ArrayList<Book> books = new ArrayList<>(); // Store inventory
    private final ArrayList<Customer> customers = new ArrayList<>(); // Registered customers
    // These are all final, but elements can still be added and removed
    
    
    public Owner() { // Constructor
        super("admin", "admin"); // Fixed username and password (from parent class User)
    }

    // Loads stored data into the store's lists on start-up
    public void restockArrays() throws IOException { // In case there is an issue with reading the file
        books.clear();
        customers.clear(); // Clears arrays for data integrity (no duplicates)
        books.addAll(files.readBookFile()); 
        customers.addAll(files.readCustomerFile()); // Fetch data from text file
    }

    // Customer list management methods 
    public void addCustomer(Customer c) { customers.add(c); }
    public void deleteCustomer(Customer c) { customers.remove(c); }
    public ArrayList<Customer> getCustomers() { return customers; }

    // Book inventory management methods
    public void addBook(Book b) { books.add(b); }
    public void deleteBook(int index) { books.remove(index); }
    public ArrayList<Book> getBooks() { return books; }
}
