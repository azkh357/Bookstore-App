package BookStore528;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Persistence layer for the application.
 * Reads and writes data to 'books.txt' and 'customers.txt'.
 * This class throws IOExceptions in the case that there's an issue reading or writing to the files.
 */
public class FileHandler {

    // Overwrites books.txt with the current list of books
    public void bookFileWrite(ArrayList<Book> books) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter("books.txt", false))) { // TRY attempts to open the file. If successful, closes it when finished.
            for (Book b : books) {
                pw.println(b.getName() + ", " + b.getPrice()); // Stores data separated by ", " in books.txt
            }
        }
    }

    // Overwrites customers.txt with the current list of customers
    public void customerFileWrite(ArrayList<Customer> customers) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter("customers.txt", false))) { // False because it's overwriting data, not appending
            for (Customer c : customers) {
                pw.println(c.getUsername() + ", " + c.getPassword() + ", " + c.getPoints());
            }
        }
    }

    // Reads books from the text file into an ArrayList
    public ArrayList<Book> readBookFile() throws IOException {
        File file = new File("books.txt");
        if (!file.exists()) file.createNewFile(); // Makes sure the file exists
        ArrayList<Book> books = new ArrayList<>(); // Creates book list in the program
        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNextLine()) {
                String[] info = scan.nextLine().split(", "); // Info is the data between commas (turns each entry into an array)
                if (info.length == 2) {
                    books.add(new Book(info[0], Double.parseDouble(info[1]))); // Adds name and price from info to books array
                }
            }
        }
        return books;
    }

    // Reads customers from the text file into an ArrayList
    public ArrayList<Customer> readCustomerFile() throws IOException {
        File file = new File("customers.txt"); // Creates text file
        if (!file.exists()) file.createNewFile();
        ArrayList<Customer> customers = new ArrayList<>(); // Creates customer list in the program
        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNextLine()) {
                String[] info = scan.nextLine().split(", ");
                if (info.length == 3) {
                    Customer c = new Customer(info[0], info[1]); // Username and password
                    c.setPoints(Integer.parseInt(info[2])); // Sets the points value to the third data point of the customer data entry
                    customers.add(c);
                }
            }
        }
        return customers;
    }
    
}
