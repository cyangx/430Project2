
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.awt.*;
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
    private AcceptShipmentPanel acceptShipmentPanel;
    private AddSupplierPanel addSupplierPanel;

    //private JPanel addProductPanel;
    private JButton logoutButton;

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
        addSupplierPanel = new AddSupplierPanel();
        acceptShipmentPanel = new AcceptShipmentPanel();

        cards = new JPanel();
        //cards.add(addProductPanel);
        //cards.add(showProductsPanel);
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

    public void addProducts() {
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

    public boolean addProduct(String title, String productID, int quantity, double price) {
       Product result;

       if(title.equals("") || productID.equals("") || quantity == 0 || price == 0){
           JOptionPane.showMessageDialog(salesFrame, "Product could not be added.");
           return false;
       }

       result = warehouse.addProduct(title, productID, price, quantity);


       if (result != null) {
           JOptionPane.showMessageDialog(salesFrame, result);
       } else {
           JOptionPane.showMessageDialog(salesFrame, "Product could not be added.");
           return false;
       }
       return true;
   }//End addProducts
    
    public boolean acceptPayment(String clientID, double payment) {
        double balance;
        
            if (warehouse.findClient(clientID)) {
                //client is found so we accept a payment from a client

                balance = warehouse.getClientBalance(clientID);
                
                warehouse.updateClientBalance(clientID, -payment);

                balance = warehouse.getClientBalance(clientID);
                //IOHelper.Println("This client's new balance is: " + balance);
                JOptionPane.showMessageDialog(null, "The client's new balance is: " + balance);

            } else {
                JOptionPane.showMessageDialog(salesFrame, "Client not found.");
                return false;
            }          

            return true;
    }//End of acceptPayment
    
    public boolean balanceCheck(String clientID){
        double balance;
        if (warehouse.findClient(clientID)){
            balance = warehouse.getClientBalance(clientID);
            JOptionPane.showInputDialog("The client's balance is: " + balance);
            return true;
        }
        return false;
    }

    public void getOverdueBalance() {
        String x = "";

        Iterator allMembers = warehouse.getMembers();
        while (allMembers.hasNext()) {
            Client member = (Client) (allMembers.next());
            if (member.getBalance() > 0) {
                x = x + member.toStringBalance() + "\n";
            }
        }
        //JOptionPane.showMessageDialog(salesFrame, "The following have overdue balances:\n" + x);
        showProductsPanel.outputBox.setText("The following have overdue balances: \n" + x);
    }

    public void showWaitlist(String pId) {
        //IOHelper.Println("Show wait list for product.");
        String wait = "";

        if (warehouse.findProduct(pId) != null) {
            Iterator wholeWaitList = warehouse.getWaitList(pId);
            while (wholeWaitList.hasNext()) {
                Wait waitList = (Wait) (wholeWaitList.next());
                //IOHelper.Println(waitList.toString());
                wait = wait + waitList.toString() + "\n";
            }
            //JOptionPane.showMessageDialog(salesFrame, "Waitlist for " + pId + ":\n" + wait);
            showProductsPanel.outputBox.setText("Waitlist for: " + pId + "\n" + wait);
        } else {
            JOptionPane.showMessageDialog(salesFrame, "Product not found.");
        }

    }

    public boolean acceptShipment(String productId, int quantity) {

            Product p = warehouse.findProduct(productId);

            if (p != null) {

                for (Iterator waitList = warehouse.getWaitList(productId); waitList.hasNext();) {
                    Wait wait = (Wait) waitList.next();
                    JOptionPane.showMessageDialog(salesFrame, wait.toString());
                    int clientQuantity = wait.getQuantity();
                    
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
            else{
                JOptionPane.showMessageDialog(salesFrame, "Product ID not found.");
                return false;
            }
            
            return true;
    }//End of acceptOrders

    private void showProducts() {
        Iterator allProducts = warehouse.getProducts();
        String prodList = "";
        while (allProducts.hasNext()) {
            Product product = (Product) (allProducts.next());
            prodList = prodList + product.toString() + "\n";
        }
        showProductsPanel.outputBox.setText("Product list: \n" + prodList);
        
        //JOptionPane.showMessageDialog(salesFrame, "Product list: \n" + prodList);

    }

    public boolean addSupplier(String pId, String mId, double price) {
        //GUI version

            Product p = warehouse.findProduct(pId);

            if (p != null) {
                
                Manufacturer m;
                m = warehouse.findManufacturer(mId);
                if (m != null) {
                    
                    warehouse.addSupplierToProduct(p, m, price);
                    JOptionPane.showMessageDialog(salesFrame, "Supplier added.");
                } else {
                    JOptionPane.showMessageDialog(salesFrame, "Could not find manufacturer.");
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(salesFrame, "Product not found.");
                return false;
            }
            return true;
    }

    public void supplierList(String pId) {

        String supList = "";

        if (warehouse.findProduct(pId) != null) {
            Iterator supplierList = warehouse.getSupplierList(pId);

            while (supplierList.hasNext()) {
                Supplier sl = (Supplier) (supplierList.next());

                supList = supList + sl.toString() + "\n";
            }

            showProductsPanel.outputBox.setText("Supplier list for : " + pId + "\n");
        } else {
            JOptionPane.showMessageDialog(salesFrame, "Product not found.");
        }
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
 //                   acceptPayment();
                    break;
                case GET_OVERDUE_BALANCE:
                    getOverdueBalance();
                    break;
                case SHOW_WAITLIST:
//                    showWaitlist();
                    break;
                case ACCEPT_SHIPMENT:
//                    acceptShipment();
                    break;
                case SHOW_PRODUCTS:
                    showProducts();
                    break;
                case ADD_SUPPLIER:
 //                   addSupplier();
                    break;
                case GET_PRODUCT_SUPPLIERS:
 //                   supplierList();
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

    private void refreshGUI(JPanel showPanel) {
        cards.removeAll();
        cards.add(showPanel);
        salesFrame.validate();
    }
    
    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(this.logoutButton)) {
            logout();
        } else if (event.getSource().equals(this.addProductButton)) {
            refreshGUI(addProductPanel);
        } else if (event.getSource().equals(this.acceptPaymentButton)) {
//            acceptPayment();
        } else if (event.getSource().equals(this.backButton)) {
            run();
        } else if (event.getSource().equals(this.getOverdueBalanceButton)) {
            getOverdueBalance();
        } else if (event.getSource().equals(this.acceptShipmentButton)) {
            refreshGUI(acceptShipmentPanel);
        } else if (event.getSource().equals(this.switchToClientButton)) {
            becomeClient();
        } else if (event.getSource().equals(this.addSupplierButton)) {
            refreshGUI(addSupplierPanel);
        } else if (event.getSource().equals(this.showProductsButton)) {
            refreshGUI(showProductsPanel);  
            showProducts();
        } else if (event.getSource().equals(this.showWaitlistButton)) {
 //           showWaitlist();
        } else if (event.getSource().equals(this.getProductSuppliersButton)) {
//            supplierList();
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
        pane.add(this.acceptShipmentButton);
        pane.add(this.showProductsButton);
        pane.add(this.addSupplierButton);
        //pane.add(this.getProductSuppliersButton);
        //pane.add(this.switchToClientButton);

        //pane.add(this.textField);
        pane.add(this.logoutButton);
        // TODO: add other buttons and items here
        pane.add(cards, BorderLayout.CENTER);

        salesFrame.setVisible(true);
        salesFrame.paint(salesFrame.getGraphics());
    }
}
