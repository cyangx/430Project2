
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.awt.*;

import javax.swing.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPanel;

/**
 *
 * @author Eric Dorphy -- Stage 1
 * @author Brandon Theisen -- Stage 3
 */
public class SalesState extends WareState implements ActionListener {

    private static Warehouse warehouse;
    private WareContext context;
    private static SalesState instance;

    private static final int EXIT = IOHelper.EXIT;
    private static final int ADD_PRODUCT = 1;
    private static final int ACCEPT_PAYMENT = 2;
    private static final int GET_OVERDUE_BALANCE = 3;
    private static final int SHOW_WAITLIST = 4;
    private static final int ACCEPT_SHIPMENT = 5;
    private static final int SHOW_PRODUCTS = 6;
    private static final int ADD_SUPPLIER = 7;
    private static final int GET_PRODUCT_SUPPLIERS = 8;
    private static final int BECOME_CLIENT = 9;
    private static final int HELP = IOHelper.HELP;

    private JFrame salesFrame;
    private JPanel cards;

    private JPanel addProduct;

    private JButton addProductButton;
    private JButton acceptPaymentButton;
    private JButton getOverdueBalanceButton;
    private JButton showWaitlistButton;
    private JButton acceptShipmentButton;
    private JButton showProductsButton;
    private JButton addSupplierButton;
    private JButton getProductSuppliersButton;
    private JButton backButton;
    private JButton confirmAddProductButton;
    private JButton switchToClientButton;

    private AddProductPanel addProductPanel;
    private ShowProductsPanel showProductsPanel;

    //private JPanel addProductPanel;
    private JButton logoutButton;
    private JTextField textField, textField2, textField3, textField4;
    private String data1, data2, data3, data4;

    private boolean confirmClick = false;

    private SalesState() {
        super();
        warehouse = Warehouse.instance();

        addProductButton = new JButton("Add Product");
        addProductButton.addActionListener(this);
        acceptPaymentButton = new JButton("Accept Payment");
        acceptPaymentButton.addActionListener(this);
        getOverdueBalanceButton = new JButton("Get Overdue Balance");
        getOverdueBalanceButton.addActionListener(this);
        showWaitlistButton = new JButton("Show Waitlist");
        showWaitlistButton.addActionListener(this);
        acceptShipmentButton = new JButton("Accept Shipment");
        acceptShipmentButton.addActionListener(this);
        showProductsButton = new JButton("Show Product List");
        showProductsButton.addActionListener(this);
        addSupplierButton = new JButton("Add Supplier to Product");
        addSupplierButton.addActionListener(this);
        getProductSuppliersButton = new JButton("Get Product Suppliers");
        getProductSuppliersButton.addActionListener(this);
        confirmAddProductButton = new JButton("Confirm");
        confirmAddProductButton.addActionListener(this);
        switchToClientButton = new JButton("Switch to Client");
        switchToClientButton.addActionListener(this);

        //addProductButton = new JButton("Add Product");
        //addProductButton.addActionListener(this);
 //       backButton = new JButton("Back");
        //       backButton.addActionListener(this);
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);

        addProductPanel = new AddProductPanel();
        showProductsPanel = new ShowProductsPanel();

        cards = new JPanel();
        //cards.add(addProductPanel);
        //cards.add(showProductsPanel);

        /*textField = new JTextField();
         textField.setPreferredSize(new Dimension(100, 30));
         textField.addActionListener(this);
         textField2 = new JTextField();
         textField2.setPreferredSize(new Dimension(100, 30));
         textField2.addActionListener(this);
         textField3 = new JTextField();
         textField3.setPreferredSize(new Dimension(100, 30));
         textField3.addActionListener(this);
         textField4 = new JTextField();
         textField4.setPreferredSize(new Dimension(100, 30));
         textField4.addActionListener(this);*/
        //context = LibContext.instance();
    }

    public static SalesState instance() {
        if (instance == null) {
            instance = new SalesState();
        }
        return instance;
    }

    private int getNumber(String prompt) {
        do {
            try {
                String item = IOHelper.getToken(prompt);
                Integer num = Integer.valueOf(item);
                return num.intValue();
            } catch (NumberFormatException nfe) {
                IOHelper.Println("Please input a number ");
            }
        } while (true);
    }

    private void help() {
        IOHelper.Println("Sales State");
        IOHelper.Println("Enter a number between " + EXIT + " and " + HELP + " as explained below:");
        IOHelper.Println(EXIT + " to Exit\n");
        IOHelper.Println(ADD_PRODUCT + " to add a product");
        IOHelper.Println(ACCEPT_PAYMENT + " to accept client payment");
        IOHelper.Println(GET_OVERDUE_BALANCE + " to get clients with balance");
        IOHelper.Println(SHOW_WAITLIST + " to get waitlist for products");
        IOHelper.Println(ACCEPT_SHIPMENT + " to accept shipment");
        IOHelper.Println(SHOW_PRODUCTS + " to get a list of products");
        IOHelper.Println(ADD_SUPPLIER + " to add a supplier to a product");
        IOHelper.Println(GET_PRODUCT_SUPPLIERS + " to view suppliers for a product");
        IOHelper.Println(BECOME_CLIENT + " to become a client");
        IOHelper.Println(HELP + " for help");
    }

    private void addProducts() {
        Product result = null;

        do {

            String title = JOptionPane.showInputDialog(salesFrame, "Enter  product name: ");
            String productID = JOptionPane.showInputDialog(salesFrame, "Enter id: ");
            double price = new Double(JOptionPane.showInputDialog(salesFrame, "Enter Price: "));
            int quantity = new Integer(JOptionPane.showInputDialog(salesFrame, "Enter Quantity: "));

            if (title.equals("") != true) {
                result = warehouse.addProduct(title, productID, price, quantity);
            }

            if (result != null) {
                JOptionPane.showMessageDialog(salesFrame, result);
            } else {
                JOptionPane.showMessageDialog(salesFrame, "Product could not be added.");
            }
            int reply = JOptionPane.showConfirmDialog(salesFrame, "Add another product?");
            if (reply == JOptionPane.NO_OPTION || reply == JOptionPane.CANCEL_OPTION) {
                break;
            }
        } while (true);//End of do while

        /*//Console Version
         do {
         String title = IOHelper.getToken("Enter  product name: ");
         String productID = IOHelper.getToken("Enter id: ");
         price = Double.parseDouble(IOHelper.getToken("Enter price: "));
         quantity = Integer.parseInt(IOHelper.getToken("Enter quantity: "));

         result = warehouse.addProduct(title, productID, price, quantity);
         if (result != null) {
         System.out.println(result);
         } else {
         IOHelper.Println("Product could not be added.");
         }

         if (!IOHelper.yesOrNo("Add more products?")) {
         break;
         }

         } while (true);*/
    }//End addProducts

    private void acceptPayment() {
        double balance, payment;
        do {
            String clientID = JOptionPane.showInputDialog(salesFrame, "Enter client ID: ");
            if (warehouse.findClient(clientID)) {
                //client is found so we accept a payment from a client

                balance = warehouse.getClientBalance(clientID);
                //IOHelper.Println("This client's balance is: " + balance);

                payment = Double.parseDouble(JOptionPane.showInputDialog("The client's balance is: "
                        + balance + "\nEnter payment amount: "));
                warehouse.updateClientBalance(clientID, -payment);

                balance = warehouse.getClientBalance(clientID);
                //IOHelper.Println("This client's new balance is: " + balance);
                JOptionPane.showMessageDialog(null, "The client's new balance is: " + balance);

            } else {
                JOptionPane.showMessageDialog(salesFrame, "Client not found.");
            }
            int reply = JOptionPane.showConfirmDialog(salesFrame, "Accept a payment from another client?");
            if (reply == JOptionPane.NO_OPTION || reply == JOptionPane.CANCEL_OPTION) {
                break;
            }
        } while (true);//End of do while loop
            /*//Console version
         String clientID = IOHelper.getToken("Enter client ID: ");

         if (warehouse.findClient(clientID)) {
         //client is found so we accept a payment from a client

         balance = warehouse.getClientBalance(clientID);
         IOHelper.Println("This client's balance is: " + balance);
         payment = Double.parseDouble(IOHelper.getToken("Enter payment amount"));
         warehouse.updateClientBalance(clientID, -payment);

         balance = warehouse.getClientBalance(clientID);
         IOHelper.Println("This client's new balance is: " + balance);

         } else {
         IOHelper.Println("Client is not found.");
         }

         } while (IOHelper.yesOrNo("Would you like to accept payment from another client?"));*/
    }//End of acceptPayment

    private void getOverdueBalance() {
        String x = "";
        /*//Console version
         Iterator allMembers = warehouse.getMembers();
         while (allMembers.hasNext()) {
         Client member = (Client) (allMembers.next());
         if (member.getBalance() > 0) {
         IOHelper.Println(member.toStringBalance());
         }
         }*/
        Iterator allMembers = warehouse.getMembers();
        while (allMembers.hasNext()) {
            Client member = (Client) (allMembers.next());
            if (member.getBalance() > 0) {
                x = x + member.toStringBalance() + "\n";
            }
        }
        JOptionPane.showMessageDialog(salesFrame, "The following have overdue balances:\n" + x);
    }

    private void showWaitlist() {
        //IOHelper.Println("Show wait list for product.");
        String wait = "";
        String pId = JOptionPane.showInputDialog("Show waitlist for product\n"
                + "Enter product ID: ");
        if (warehouse.findProduct(pId) != null) {
            Iterator wholeWaitList = warehouse.getWaitList(pId);
            while (wholeWaitList.hasNext()) {
                Wait waitList = (Wait) (wholeWaitList.next());
                //IOHelper.Println(waitList.toString());
                wait = wait + waitList.toString() + "\n";
            }
            JOptionPane.showMessageDialog(salesFrame, "Waitlist for " + pId + ":\n" + wait);
        } else {
            JOptionPane.showMessageDialog(salesFrame, "Product not found.");
        }
        /*//Console version
         IOHelper.Println("Show wait list for product.");
         String pId = IOHelper.getToken("Enter product ID: ");
         if (warehouse.findProduct(pId) != null) {
         Iterator wholeWaitList = warehouse.getWaitList(pId);
         while (wholeWaitList.hasNext()) {
         Wait waitList = (Wait) (wholeWaitList.next());
         IOHelper.Println(waitList.toString());
         }
         } else {
         IOHelper.Println("Product not found.");
         }*/
    }

    private void acceptShipment() {

        do {
            String productId = JOptionPane.showInputDialog("Enter product ID: ");
            Product p = warehouse.findProduct(productId);
            int quantity;

            if (p != null) {
                String quant = JOptionPane.showInputDialog("Enter quantity recieved: ");
                quantity = Integer.parseInt(quant);

                for (Iterator waitList = warehouse.getWaitList(productId); waitList.hasNext();) {
                    Wait wait = (Wait) waitList.next();
                    //IOHelper.Println(wait.toString());
                    JOptionPane.showMessageDialog(salesFrame, wait.toString());
                    int clientQuantity = wait.getQuantity();
                    /*if (IOHelper.yesOrNo("Do you want to fulfill waitlist for this client?")) */
                    int reply = JOptionPane.showConfirmDialog(salesFrame, "Do you want to fulfill waitlist for this client?");
                    if (reply == JOptionPane.YES_OPTION) {
                        if (quantity >= clientQuantity) {
                            if (warehouse.fulfillWaitList(p, wait.getClient(), clientQuantity)) {
                                quantity -= clientQuantity;
                                waitList.remove();
                            }
                        } else {
                            if (warehouse.fulfillWaitList(p, wait.getClient(), quantity)) {
                                quantity -= quantity;
                                break;
                            }
                        }
                    }

                }
                warehouse.updateQuantity(p, quantity);
            }
            int j = JOptionPane.showConfirmDialog(salesFrame, "Add more items?");
            if (j == JOptionPane.NO_OPTION || j == JOptionPane.CANCEL_OPTION) {
                break;
            }
        } while (true);
        //Console version
        /*do {
         String productId = IOHelper.getToken("Enter product ID: ");
         Product p = warehouse.findProduct(productId);
         int quantity;

         if (p != null) {
         quantity = getNumber("Enter quantity recieved:");
         for (Iterator waitList = warehouse.getWaitList(productId); waitList.hasNext();) {
         Wait wait = (Wait) waitList.next();
         IOHelper.Println(wait.toString());
         int clientQuantity = wait.getQuantity();
         if (IOHelper.yesOrNo("Do you want to fulfill waitlist for this client?")) {
         if (quantity >= clientQuantity) {
         if (warehouse.fulfillWaitList(p, wait.getClient(), clientQuantity)) {
         quantity -= clientQuantity;
         waitList.remove();
         }
         } else {
         if (warehouse.fulfillWaitList(p, wait.getClient(), quantity)) {
         quantity -= quantity;
         break;
         }
         }
         }
         }
         warehouse.updateQuantity(p, quantity);
         }
         } while (IOHelper.yesOrNo("Add more items?"));*/
    }//End of acceptOrders

    private void showProducts() {
        Iterator allProducts = warehouse.getProducts();
        String prodList = "";
        while (allProducts.hasNext()) {
            Product product = (Product) (allProducts.next());
            prodList = prodList + product.toString() + "\n";
        }
        JOptionPane.showMessageDialog(salesFrame, "Product list: \n" + prodList);

    }

    private void addSupplier() {
        //GUI version
        do {
            String pId = JOptionPane.showInputDialog("Enter product id: ");

            Product p = warehouse.findProduct(pId);

            if (p != null) {
                String mId = JOptionPane.showInputDialog("Enter manufacturer id: ");
                Manufacturer m;
                m = warehouse.findManufacturer(mId);
                if (m != null) {
                    double price;
                    String pr = JOptionPane.showInputDialog("Enter price: ");
                    price = Double.parseDouble(pr);

                    warehouse.addSupplierToProduct(p, m, price);
                    JOptionPane.showMessageDialog(salesFrame, "Supplier added.");
                    break;
                } else {
                    JOptionPane.showMessageDialog(salesFrame, "Could not find manufacturer.");
                }
            } else {
                JOptionPane.showMessageDialog(salesFrame, "Product not found.");
            }

            int j = JOptionPane.showConfirmDialog(salesFrame, "Try again?");
            if (j == JOptionPane.NO_OPTION || j == JOptionPane.CANCEL_OPTION) {
                break;
            }
        } while (true);

        /*//Console version
         do {
         String pId = IOHelper.getToken("Enter product id");
            
         Product p = warehouse.findProduct(pId);

         if (p != null) {
         String mId = IOHelper.getToken("Enter manufacturer id");
         Manufacturer m;
         m = warehouse.findManufacturer(mId);
         if (m != null) {
         int price = getNumber("Enter price");
         warehouse.addSupplierToProduct(p, m, price);
         System.out.println("Supplier added.");
         break;
         } else {
         System.out.println("Could not find manufacturer.");
         }
         } else {
         System.out.println("Product not found.");
         }

         if (!IOHelper.yesOrNo("Try again?")) {
         break;
         }

         } while (true);
         */
    }

    private void supplierList() {

        String supList = "";

        String pId = JOptionPane.showInputDialog("Show supplier list for product.\n "
                + "Enter product ID: ");

        if (warehouse.findProduct(pId) != null) {
            Iterator supplierList = warehouse.getSupplierList(pId);

            while (supplierList.hasNext()) {
                Supplier sl = (Supplier) (supplierList.next());
                //IOHelper.Println(sl.toString());
                supList = supList + sl.toString() + "\n";
            }
            JOptionPane.showMessageDialog(salesFrame, "Supplier list for " + pId
                    + ":\n" + supList);
        } else {
            JOptionPane.showMessageDialog(salesFrame, "Product not found.");
        }

        /*//Console version
         IOHelper.Println("Show supplier list for product.");
         String pId = IOHelper.getToken("Enter product ID");

         if (warehouse.findProduct(pId) != null) {
         Iterator supplierList = warehouse.getSupplierList(pId);

         while (supplierList.hasNext()) {
         Supplier sl = (Supplier) (supplierList.next());
         IOHelper.Println(sl.toString());
         }
         } else {
         IOHelper.Println("Product not found.");
         }*/
    }

    private void becomeClient() {
        //String userID = IOHelper.getToken("Please input the user id: ");
        String userID = JOptionPane.showInputDialog("Please input the user id: ");

        if (Warehouse.instance().findClient(userID)) {
            (WareContext.instance()).setUser(userID);
            (WareContext.instance()).changeState(WareContext.CLIENT_STATE); //go to sales state
        } else {
            //System.out.println("Invalid user id.");
            JOptionPane.showMessageDialog(salesFrame, "Invalid user id.");
        }
    }

    /**
     * If we are a manager, logout of sales and return to manager else we logout
     * and go to the main login menu
     */
    private void logout() {
        if ((WareContext.instance()).getLogin() == WareContext.IsManager) {
            (WareContext.instance()).changeState(WareContext.MANAGER_STATE);
        } else {
            (WareContext.instance()).changeState(WareContext.LOGIN_STATE);
        }
    }

    private void process() {
        int command;
        help();
        while ((command = IOHelper.GetCmd()) != EXIT) {
            switch (command) {
                case EXIT:
                    break;
                case ADD_PRODUCT:
                    addProducts();
                    break;
                case ACCEPT_PAYMENT:
                    acceptPayment();
                    break;
                case GET_OVERDUE_BALANCE:
                    getOverdueBalance();
                    break;
                case SHOW_WAITLIST:
                    showWaitlist();
                    break;
                case ACCEPT_SHIPMENT:
                    acceptShipment();
                    break;
                case SHOW_PRODUCTS:
                    showProducts();
                    break;
                case ADD_SUPPLIER:
                    addSupplier();
                    break;
                case GET_PRODUCT_SUPPLIERS:
                    supplierList();
                    break;
                case BECOME_CLIENT:
                    becomeClient();
                    break;
                case HELP:
                    help();
                    break;
            }
        }
        logout();
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(this.logoutButton)) {
            logout();
        } else if (event.getSource().equals(this.addProductButton)) {
            cards.removeAll();
            cards.add(addProductPanel);
            salesFrame.validate();
        } else if (event.getSource().equals(this.acceptPaymentButton)) {
            acceptPayment();
        } else if (event.getSource().equals(this.backButton)) {
            run();
        } else if (event.getSource().equals(this.getOverdueBalanceButton)) {
            getOverdueBalance();
        } else if (event.getSource().equals(this.acceptShipmentButton)) {
            acceptShipment();
        } else if (event.getSource().equals(this.switchToClientButton)) {
            becomeClient();
        } else if (event.getSource().equals(this.addSupplierButton)) {
            addSupplier();
        } else if (event.getSource().equals(this.showProductsButton)) {
            cards.removeAll();
            cards.add(showProductsPanel);
            salesFrame.validate();
            //showProducts();
        } else if (event.getSource().equals(this.showWaitlistButton)) {
            showWaitlist();
        } else if (event.getSource().equals(this.getProductSuppliersButton)) {
            supplierList();
        }
        //else if(event.getSource().equals(this.textField)) {
        //    JOptionPane.showMessageDialog(salesFrame,"You entered: " + textField.getText());
        //}
        /*else if(event.getSource().equals(this.confirmAddProductButton)){
         data1 = textField.getText();
         data2 = textField2.getText();
         data3 = textField3.getText();
         data4 = textField4.getText();
         confirmClick = true;
         }*/
    }

    public void run() {
        //process();
        salesFrame = WareContext.instance().getFrame();
        Container pane = salesFrame.getContentPane();
        pane.removeAll();
        pane.setLayout(new FlowLayout());
        pane.add(this.addProductButton);
        //pane.add(this.acceptPaymentButton);
        //pane.add(this.getOverdueBalanceButton);
        //pane.add(this.showWaitlistButton);
        //pane.add(this.acceptShipmentButton);
        pane.add(this.showProductsButton);
        //pane.add(this.addSupplierButton);
        //pane.add(this.getProductSuppliersButton);
        //pane.add(this.switchToClientButton);

        //pane.add(this.textField);
        //pane.add(this.logoutButton);
        // TODO: add other buttons and items here
        pane.add(cards, BorderLayout.CENTER);

        salesFrame.setVisible(true);
        salesFrame.paint(salesFrame.getGraphics());
    }
}
