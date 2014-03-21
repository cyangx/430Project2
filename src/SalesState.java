
import java.util.*;

/**
 *
 * @author Eric Dorphy
 */
public class SalesState extends WareState {

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

    private SalesState() {
        super();
        warehouse = Warehouse.instance();
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
        Product result;
        double price;
        int quantity;

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

        } while (true);
    }//End addProducts

    private void acceptPayment() {
        double balance, payment;
        do {
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

        } while (IOHelper.yesOrNo("Would you like to accept payment from another client?"));
    }//End of acceptPayment

    private void getOverdueBalance() {
        Iterator allMembers = warehouse.getMembers();
        while (allMembers.hasNext()) {
            Client member = (Client) (allMembers.next());
            if (member.getBalance() > 0) {
                IOHelper.Println(member.toStringBalance());
            }
        }
    }

    private void showWaitlist() {
        IOHelper.Println("Show wait list for product.");
        String pId = IOHelper.getToken("Enter product ID");
        if (warehouse.findProduct(pId) != null) {
            Iterator wholeWaitList = warehouse.getWaitList(pId);
            while (wholeWaitList.hasNext()) {
                Wait waitList = (Wait) (wholeWaitList.next());
                IOHelper.Println(waitList.toString());
            }
        } else {
            IOHelper.Println("Product not found.");
        }
    }

    private void acceptShipment() {
        do {
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
        } while (IOHelper.yesOrNo("Add more items?"));
    }//End of acceptOrders

    private void showProducts() {
        Iterator allProducts = warehouse.getProducts();
        while (allProducts.hasNext()) {
            Product product = (Product) (allProducts.next());
            IOHelper.Println(product.toString());
        }
    }

    private void addSupplier() {

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

    }

    private void supplierList() {

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
        }
    }

    private void becomeClient() {
        String userID = IOHelper.getToken("Please input the user id: ");
        if (Warehouse.instance().findClient(userID)) {
            (WareContext.instance()).setUser(userID);
            (WareContext.instance()).changeState(WareContext.CLIENT_STATE); //go to sales state
        } else {
            System.out.println("Invalid user id.");
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

    public void run() {
        process();
    }
}
