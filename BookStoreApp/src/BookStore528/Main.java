package BookStore528;

import javax.swing.*; // Imports all elements for swing
import javax.swing.table.DefaultTableCellRenderer; // Allow table cell functionality (for books, customers, etc)
import javax.swing.table.DefaultTableModel; // Allows tables to exist in swing GUI
import javax.swing.table.JTableHeader; // Allows us to add headers to tables
import java.awt.*; // Abstract Window Toolkit (basically GUI window physics)
import java.awt.event.*; // This allows the window to respond to user input
import java.io.IOException; // Allows IOExceptions (Throws errors)
import java.util.ArrayList; 

/**
 * Bookstore Application Entry Point.
 * Controls the Swing GUI, screen transitions, and business logic.
 */
public class Main {
    private static JFrame frame;          // The main window
    private static CardLayout cardLayout; // Swaps screens (changes the current screen to the next screen)
    private static JPanel container;      // Panel containing all screens
    
    private static final Owner owner = new Owner();      // Creates the intial owner (admin) 
    private static final FileHandler files = new FileHandler(); // File handling logic
    private static Customer currentCustomer;              // Tracks the current customer (no value until a user logs in with username and pass)

    // GUI Customization (changing the colours here can globally change the theme of the entire app)
    private static final Color BG_COLOR = new Color(219, 234, 254);     // Sky Blue
    private static final Color CARD_COLOR = new Color(255, 251, 235);   // Warm Cream
    private static final Color TEXT_COLOR = new Color(30, 41, 59);      // Slate Navy
    private static final Color BLUE_ACCENT = new Color(30, 64, 175);    // Strong Blue
    private static final Color ORANGE_ACCENT = new Color(249, 115, 22); // Vibrant Orange
    private static final Color RED_ACCENT = new Color(220, 38, 38);     // Bold Red
    
    // These are all static because only one instance of the window can exist at a time

    public static void main(String[] args) {
        // Startup: Load stored data into the store lists
        try {
            owner.restockArrays(); // Attempts to load data into arrays from txt files
        } catch (IOException e) {
            e.printStackTrace(); // Prints the details of the error to the console (makes it easy to track what's going wrong)
        }

        SwingUtilities.invokeLater(() -> { // Stops it from crashing unexpectedly (we were running into issues)
            // Set some UI defaults to match the theme
            UIManager.put("Label.foreground", TEXT_COLOR);
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("PasswordField.background", Color.WHITE);

            // Configure the main application frame
            frame = new JFrame("Bookstore App"); // Creates the main JFrame with the heading "Bookstore App"
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Do nothing so that the files can be written and the app can be closed while protecting data
            frame.setSize(750, 650); // Set default (initial) size of window
            frame.setLocationRelativeTo(null); // Centers the window

            // Auto-save data when the application is closed
            frame.addWindowListener(new WindowAdapter() { // Allows the window to be adaptable to session events
                @Override
                public void windowClosing(WindowEvent e) { // Overrides the built-in method for detecting if the window is being closed
                    try {
                        files.bookFileWrite(owner.getBooks()); // Writees the books and customers to the txt files before closing
                        files.customerFileWrite(owner.getCustomers());
                    } catch (IOException ex) {
                        ex.printStackTrace(); // Prints details of error to the console (if there is one)
                    }
                    System.exit(0); // Exits the program
                }
            });

            // CardLayout allows us to "swap" screens in a single window
            cardLayout = new CardLayout(); // Allows program to swap to a different screen
            container = new JPanel(cardLayout);
            container.add(createLoginScreen(), "LoginScreen"); // Adds Login Screen to the container of screens (method below)
            container.add(createOwnerStartScreen(), "OwnerStartScreen"); // Adds the owner start screen to the container of screens

            frame.add(container); // Adds the screens to the frame (window) and makes it visible
            frame.setVisible(true);
        });
    }

    // UI Element Methods

    // Styles buttons with the theme colors and hover effects globally (all buttons)
    private static void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            @Override public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
    }

    // Standard styling for all JTables used in the app
    private static void styleTable(JTable table) {
        table.setBackground(CARD_COLOR);
        table.setRowHeight(40);
        table.setFont(new Font("SansSerif", Font.PLAIN, 15));
        table.setSelectionBackground(BLUE_ACCENT);
        table.setSelectionForeground(Color.WHITE);
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 15));
        header.setForeground(BLUE_ACCENT);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

    // Screen Creation Methods

    // SCREEN: The initial login portal
    private static JPanel createLoginScreen() {
        JPanel outer = new JPanel(new GridBagLayout()); // Creates a grid layout
        outer.setBackground(BG_COLOR);
        JPanel card = new JPanel(new GridBagLayout()); //  The login box is a card
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder( // Creates the border of the main UI
            BorderFactory.createLineBorder(ORANGE_ACCENT, 2),
            BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12); gbc.fill = GridBagConstraints.HORIZONTAL; // Padding/spacing for every element

        JLabel titleLabel = new JLabel("BOOKSTORE", SwingConstants.CENTER); // Creates bookstore label
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36)); titleLabel.setForeground(BLUE_ACCENT);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; card.add(titleLabel, gbc); // Posisitons the label

        gbc.gridwidth = 1; // Changes width of the next element
        gbc.gridy = 1; card.add(new JLabel("Username:"), gbc);
        JTextField userField = new JTextField(15); gbc.gridx = 1; card.add(userField, gbc); // Creates text field for username/password
        gbc.gridx = 0; gbc.gridy = 2; card.add(new JLabel("Password:"), gbc);
        JPasswordField passField = new JPasswordField(15); gbc.gridx = 1; card.add(passField, gbc); // Same as username but specifically for passwords (added security)

        JButton loginButton = new JButton("LOGIN"); styleButton(loginButton, BLUE_ACCENT);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.insets = new Insets(25, 12, 0, 12); card.add(loginButton, gbc); // Login button details

        // Handle Login logic for Owner vs Customer
        loginButton.addActionListener(e -> { // Detects a certain action (press of the login button)
            String u = userField.getText(), p = new String(passField.getPassword()); // Gets user data from text fields
            if (u.equals(owner.getUsername()) && p.equals(owner.getPassword())) {   // If they match the owner credentials (admin/admin), then it switches to the owner start screen (method below)
                cardLayout.show(container, "OwnerStartScreen");
            } else {
                for (Customer c : owner.getCustomers()) if (u.equals(c.getUsername()) && p.equals(c.getPassword())) { // Compares each customer's username and password to the text fields
                    currentCustomer = c;
                    container.add(createCustomerStartScreen(), "CustomerStartScreen"); // Creates and goes to the customer start screen
                    cardLayout.show(container, "CustomerStartScreen");
                    return;
                }
                JOptionPane.showMessageDialog(frame, "Invalid credentials! Try again."); // If it doesn't match the owner or any customer credentials, user must try again.
            }
        });
        outer.add(card); return outer; // returns the screen
    }

    // SCREEN: The starting dashboard for the admin/owner
    private static JPanel createOwnerStartScreen() {
        JPanel panel = new JPanel(new GridBagLayout()); panel.setBackground(BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints(); gbc.insets = new Insets(20, 20, 20, 20); gbc.fill = GridBagConstraints.BOTH; // Creates grid
        
        // Button detailing
        JButton bBtn = new JButton("MANAGE BOOKS"), cBtn = new JButton("MANAGE CUSTOMERS"), loBtn = new JButton("LOGOUT");
        styleButton(bBtn, ORANGE_ACCENT); styleButton(cBtn, ORANGE_ACCENT); styleButton(loBtn, RED_ACCENT);
        bBtn.setPreferredSize(new Dimension(300, 120)); cBtn.setPreferredSize(new Dimension(300, 120));
        
        // Detects if buttons are pressed and goes to the according screen
        bBtn.addActionListener(e -> { container.add(createOwnerBooksScreen(), "OwnerBooksScreen"); cardLayout.show(container, "OwnerBooksScreen"); });
        cBtn.addActionListener(e -> { container.add(createOwnerCustomersScreen(), "OwnerCustomersScreen"); cardLayout.show(container, "OwnerCustomersScreen"); });
        loBtn.addActionListener(e -> cardLayout.show(container, "LoginScreen"));
        
        gbc.gridy = 0; panel.add(bBtn, gbc); gbc.gridy = 1; panel.add(cBtn, gbc); gbc.gridy = 2; panel.add(loBtn, gbc); // Creates the column of buttons in the middle of the screen
        return panel; 
    }

    // SCREEN: Allows owner to add or delete books
    private static JPanel createOwnerBooksScreen() {
        JPanel panel = new JPanel(new BorderLayout(25, 25)); panel.setBackground(BG_COLOR); panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Sets up borders of screen
        
        String[] cols = {"Book Name", "Book Price"}; // Columns of book table
        // Make table non-editable (prevents errors)
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Book b : owner.getBooks()) model.addRow(new Object[]{b.getName(), String.format("%.2f", b.getPrice())}); // Fills the table with info of books
        
        JTable table = new JTable(model); styleTable(table); panel.add(new JScrollPane(table), BorderLayout.CENTER); // Turns the model into a table
        
        JPanel input = new JPanel(new GridLayout(2, 3, 20, 20)); input.setBackground(BG_COLOR);
        JTextField nf = new JTextField(), pf = new JTextField();
        JButton aBtn = new JButton("ADD"), dBtn = new JButton("DELETE"), bkBtn = new JButton("BACK"); // Adds functionality for adding and deleting books
        styleButton(aBtn, BLUE_ACCENT); styleButton(dBtn, RED_ACCENT); styleButton(bkBtn, TEXT_COLOR);
        
        aBtn.addActionListener(e -> { try { // Detects if button is pressed
            double p = Double.parseDouble(pf.getText());  // Parses for double
            if (p < 0) throw new Exception(); // Price cannot be negative
            Book b = new Book(nf.getText(), p); owner.addBook(b); // Creates a new book with the inputted name and price
            model.addRow(new Object[]{b.getName(), String.format("%.2f", p)}); nf.setText(""); pf.setText("");
        } catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Please enter a valid, positive price!"); } });
        
        dBtn.addActionListener(e -> { int r = table.getSelectedRow(); if (r != -1) { owner.deleteBook(r); model.removeRow(r); } }); // Deletes the book associated with the selected row (and also removes the row)
        
        bkBtn.addActionListener(e -> cardLayout.show(container, "OwnerStartScreen"));
        input.add(new JLabel("Book Name:")); input.add(nf); input.add(aBtn); input.add(new JLabel("Book Price:")); input.add(pf); input.add(dBtn); // Adds the established elements to the screen
        panel.add(input, BorderLayout.SOUTH); panel.add(bkBtn, BorderLayout.NORTH); return panel;
    }

    // SCREEN: Allows owner to manage customer accounts
    private static JPanel createOwnerCustomersScreen() { 
        JPanel panel = new JPanel(new BorderLayout(25, 25)); panel.setBackground(BG_COLOR); panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        String[] cols = {"Username", "Password", "Points"}; // Creates columns of customer table
        // Make table non-editable 
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Customer c : owner.getCustomers()) model.addRow(new Object[]{c.getUsername(), c.getPassword(), c.getPoints()}); // Fills the customer data into the table model
        
        JTable table = new JTable(model); styleTable(table); panel.add(new JScrollPane(table), BorderLayout.CENTER); // Creates table
        
        JPanel input = new JPanel(new GridLayout(2, 3, 20, 20)); input.setBackground(BG_COLOR);
        JTextField uf = new JTextField(), paf = new JTextField();
        JButton aBtn = new JButton("ADD"), dBtn = new JButton("DELETE"), bkBtn = new JButton("BACK"); // Creates buttons (same as book table)
        styleButton(aBtn, BLUE_ACCENT); styleButton(dBtn, RED_ACCENT); styleButton(bkBtn, TEXT_COLOR);
        
        aBtn.addActionListener(e -> { String u = uf.getText(), p = paf.getText(); if (!u.isEmpty() && !p.isEmpty()) { // Detects if button is pressed
            owner.addCustomer(new Customer(u, p)); model.addRow(new Object[]{u, p, 0}); uf.setText(""); paf.setText(""); // Adds customers with inputted username, password, and 0 points (uf clears the field)
        } });
        
        dBtn.addActionListener(e -> { int r = table.getSelectedRow(); if (r != -1) { owner.deleteCustomer(owner.getCustomers().get(r)); model.removeRow(r); } }); // Deletes customer (same as book table) 
        
        bkBtn.addActionListener(e -> cardLayout.show(container, "OwnerStartScreen"));
        input.add(new JLabel("Username:")); input.add(uf); input.add(aBtn); input.add(new JLabel("Password:")); input.add(paf); input.add(dBtn); // Adds all the elements to the screen
        panel.add(input, BorderLayout.SOUTH); panel.add(bkBtn, BorderLayout.NORTH); return panel;
    }

    // SCREEN: Main screen for logged-in customers
    private static JPanel createCustomerStartScreen() {
        JPanel panel = new JPanel(new BorderLayout(25, 25)); panel.setBackground(BG_COLOR); panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Status header panel with Gold/Silver coloring
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); headerPanel.setBackground(BG_COLOR);  // Creates a header
        JLabel wl = new JLabel("Welcome " + currentCustomer.getUsername() + " | Points: " + currentCustomer.getPoints() + " | Status: "); // Adds all header content
        JLabel sl = new JLabel(currentCustomer.getStatus()); // Gold or Silver
        wl.setFont(new Font("SansSerif", Font.BOLD, 20)); sl.setFont(new Font("SansSerif", Font.BOLD, 20)); // Format header
        sl.setForeground(currentCustomer.getStatus().equalsIgnoreCase("Gold") ? new Color(212, 175, 55) : new Color(113, 113, 122)); // Changes font colour of status accordingly (could have been implemented using state design pattern?)
        headerPanel.add(wl); headerPanel.add(sl); panel.add(headerPanel, BorderLayout.NORTH); // Adds headers to screen

        // Table with Checkbox support for book selection
        String[] cols = {"Book Name", "Book Price", "Select"}; // Columns of customer book buy table
        DefaultTableModel m = new DefaultTableModel(cols, 0) { 
            @Override public Class<?> getColumnClass(int c) { return c == 2 ? Boolean.class : super.getColumnClass(c); } // Adds checkboxes
            @Override public boolean isCellEditable(int r, int c) { return c == 2; } // Not editable
        };
        for (Book b : owner.getBooks()) m.addRow(new Object[]{b.getName(), b.getPrice(), false}); //Fills in table contents
        
        JTable table = new JTable(m); styleTable(table); panel.add(new JScrollPane(table), BorderLayout.CENTER); /// Creates the table
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0)); btnPanel.setBackground(BG_COLOR);
        JButton buyBtn = new JButton("BUY"), rBtn = new JButton("REDEEM & BUY"), loBtn = new JButton("LOGOUT");
        styleButton(buyBtn, BLUE_ACCENT); styleButton(rBtn, BLUE_ACCENT); styleButton(loBtn, RED_ACCENT); // Creates buttons
        
        buyBtn.addActionListener(e -> processPurchase(m, false)); 
        rBtn.addActionListener(e -> processPurchase(m, true));
        loBtn.addActionListener(e -> cardLayout.show(container, "LoginScreen")); // Checks the buttons for actions (click)
        
        btnPanel.add(buyBtn); btnPanel.add(rBtn); btnPanel.add(loBtn); panel.add(btnPanel, BorderLayout.SOUTH); // Adds all elements to the screen
        return panel;
    }

    // Purchase logic: Calculates final cost and updates inventory/points
    private static void processPurchase(DefaultTableModel model, boolean redeem) {
        double subtotal = 0; ArrayList<Book> toRemove = new ArrayList<>(); // Creates a list of books to remove from books (because they are being bought)
        for (int i = 0; i < model.getRowCount(); i++) if ((Boolean) model.getValueAt(i, 2)) { // Checks which books in the buy table are selected
            subtotal += (Double) model.getValueAt(i, 1); toRemove.add(owner.getBooks().get(i));
        }
        
        if (toRemove.isEmpty()) { JOptionPane.showMessageDialog(frame, "Select at least one book to purchase!"); return; } // If no books are selected 
        
        double discount = 0;
        if (redeem) {
            int currentPoints = currentCustomer.getPoints(); // Checks the points of the customer and applies the discount accordingly
            discount = currentPoints / 100.0; // 100 pts = $1 CAD
            if (discount > subtotal) discount = subtotal; // Can't discount more than the subtotal
            currentCustomer.setPoints(currentPoints - (int)(discount * 100)); // Remove used points
        }
        
        double total = subtotal - discount; 
        total = currentCustomer.getStatusObj().calcCost(total); // Apply state-specific cost rules (none right now but can be added)
        currentCustomer.setPoints(currentCustomer.getPoints() + (int)(total * 10)); // $1 spent = 10 points earned
        owner.getBooks().removeAll(toRemove); // One copy of each book only!
        
        container.add(createCheckoutScreen(total), "CheckoutScreen"); cardLayout.show(container, "CheckoutScreen"); // Switches to checkout screen
    }

    // SCREEN: Final summary screen shown after a successful buy
    private static JPanel createCheckoutScreen(double total) {
        JPanel panel = new JPanel(new GridBagLayout()); panel.setBackground(BG_COLOR);
        JPanel card = new JPanel(new GridBagLayout()); card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BLUE_ACCENT, 2), BorderFactory.createEmptyBorder(60, 60, 60, 60)));
        GridBagConstraints gbc = new GridBagConstraints(); gbc.insets = new Insets(15, 15, 15, 15); gbc.gridx = 0; gbc.gridy = GridBagConstraints.RELATIVE;
        // Designs the screen basics
        
        JLabel totalLabel = new JLabel("TOTAL: CAD " + String.format("%.2f", total)); totalLabel.setFont(new Font("SansSerif", Font.BOLD, 32)); totalLabel.setForeground(RED_ACCENT); card.add(totalLabel, gbc); // Creates label for total cost for customer
        
        JPanel statusPanel = new JPanel(new FlowLayout()); statusPanel.setBackground(CARD_COLOR);
        JLabel pointsLabel = new JLabel("Points: " + currentCustomer.getPoints() + " | Status: ");
        JLabel statusLabel = new JLabel(currentCustomer.getStatus());
        pointsLabel.setFont(new Font("SansSerif", Font.BOLD, 20)); statusLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        statusLabel.setForeground(currentCustomer.getStatus().equalsIgnoreCase("Gold") ? new Color(212, 175, 55) : new Color(113, 113, 122)); // Changes the colour of status label depending on status after purchase (? instead of if-else block)
        statusPanel.add(pointsLabel); statusPanel.add(statusLabel); card.add(statusPanel, gbc);
        // Adds panel and labels accordingly
        
        JButton loBtn = new JButton("LOGOUT"); styleButton(loBtn, RED_ACCENT); loBtn.setPreferredSize(new Dimension(250, 60));
        loBtn.addActionListener(e -> cardLayout.show(container, "LoginScreen")); // If logout is pressed, it switches to login screen
        gbc.insets = new Insets(40, 15, 0, 15); card.add(loBtn, gbc); // Adds padding and logout button
        
        panel.add(card); return panel; // Returns the panel
    }
}
