package BookStore528;

/**
 * Data class to store book information.
 * As per the PDF, each book has a name and a price.
 */
public class Book {
    private final String name; // Name of the book (cannot ever be changed)
    private double price;      // Cost of the book 

    public Book(String name, double price) { // Constructor
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }                  // Setters and getters for book variables (encapsulation)
    public void setPrice(double price) { this.price = price; }  // Not used currently, but for future proofing (we thought there was a way for admin to change the price)
}
