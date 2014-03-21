
import java.util.*;
import java.text.*;
import java.io.*;

public class UserInterface {

    private static UserInterface userInterface;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private static final int EXIT = 0;
    private static final int ADD_CLIENT = 1;
    private static final int ADD_PRODUCTS = 2;
    private static final int ADD_MANUFACTURER = 3;
    private static final int ACCEPT_ORDERS = 4;
    private static final int ADD_SUPPLIER = 5;
    private static final int DELETE_SUPPLIER = 6;
    private static final int SUPPLIER_LIST = 7;
    private static final int ACCEPT_PAYMENT = 8;
    private static final int GET_BALANCE = 9;
    private static final int GET_OVERDUE = 10;
    private static final int SHOW_WAITLIST = 11;
    private static final int SHOW_INVOICE = 12;
    private static final int ACCEPT_SHIPMENT = 13;
    private static final int GET_TRANSACTIONS = 14;
    private static final int SHOW_MANUFACTURERS = 15;
    private static final int SHOW_CLIENTS = 16;
    private static final int SHOW_PRODUCTS = 17;
    private static final int SAVE = 18;
    private static final int RETRIEVE = 19;
    private static final int HELP = 20;

    private UserInterface() {
        if (yesOrNo("Look for saved data and  use it?")) {
            retrieve();
        } else {
            warehouse = Warehouse.instance();
        }
    }

    public static UserInterface instance() {
        if (userInterface == null) {
            return userInterface = new UserInterface();
        } else {
            return userInterface;
        }
    }

    public String getToken(String prompt) {
        do {
            try {
                System.out.println(prompt);
                String line = reader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(line, "\n\r\f");
                if (tokenizer.hasMoreTokens()) {
                    return tokenizer.nextToken();
                }
            } catch (IOException ioe) {
                System.exit(0);
            }
        } while (true);
    }

    private boolean yesOrNo(String prompt) {
        String more = getToken(prompt + " (Y|y)[es] or anything else for no");
        if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
            return false;
        }
        return true;
    }

    public int getNumber(String prompt) {
        do {
            try {
                String item = getToken(prompt);
                Integer num = Integer.valueOf(item);
                return num.intValue();
            } catch (NumberFormatException nfe) {
                System.out.println("Please input a number ");
            }
        } while (true);
    }

    public Calendar getDate(String prompt) {
        do {
            try {
                Calendar date = new GregorianCalendar();
                String item = getToken(prompt);
                DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
                date.setTime(df.parse(item));
                return date;
            } catch (Exception fe) {
                System.out.println("Please input a date as mm/dd/yy");
            }
        } while (true);
    }

    public int getCommand() {
        do {
            try {
                int value = Integer.parseInt(getToken("Enter command:" + HELP + " for help"));
                if (value >= EXIT && value <= HELP) {
                    return value;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Enter a number");
            }
        } while (true);
    }

    public void help() {
        System.out.println("Enter a number between 0 and 12 as explained below:");
        System.out.println(EXIT + " to Exit\n");
        System.out.println(ADD_CLIENT + " to add a client");
        System.out.println(ADD_PRODUCTS + " to add product");
        System.out.println(ADD_MANUFACTURER + " to add manufacturer");
        System.out.println(ACCEPT_ORDERS + " to accept orders from a client");
        System.out.println(ADD_SUPPLIER + " to add a supplier to a product");
        System.out.println(DELETE_SUPPLIER + " to delete a supplier to a product");
        System.out.println(SUPPLIER_LIST + " to display a supplier list for a product");
        System.out.println(ACCEPT_PAYMENT + " to accept payment from a client");
        System.out.println(GET_BALANCE + " to get a client balance ");
        System.out.println(GET_OVERDUE + " to get overdue balance of all clients ");
        System.out.println(SHOW_WAITLIST + " to display waitlist for product");
        System.out.println(SHOW_INVOICE + " to get a client invoice ");
        System.out.println(ACCEPT_SHIPMENT + " to accept a shipment ");
        System.out.println(GET_TRANSACTIONS + " to show transaction list of a client");
        System.out.println(SHOW_MANUFACTURERS + " to  print manufacturers");
        System.out.println(SHOW_CLIENTS + " to  print clients");
        System.out.println(SHOW_PRODUCTS + " to  print products");
        System.out.println(SAVE + " to  save data");
        System.out.println(RETRIEVE + " to  retrieve");
        System.out.println(HELP + " for help");

    }

    public void addClient() {
        String name = getToken("Enter client name");
        String address = getToken("Enter address");
        String phone = getToken("Enter phone");
        Client result;
        result = warehouse.addMember(name, address, phone);
        if (result == null) {
            System.out.println("Could not add client");
        }
        System.out.println(result);
    }

    public void addProducts() {
        Product result;
        double price;
        int quantity;

        do {
            String title = getToken("Enter  product name: ");
            String productID = getToken("Enter id: ");
            price = Double.parseDouble(getToken("Enter price: "));
            quantity = Integer.parseInt(getToken("Enter quantity: "));

            result = warehouse.addProduct(title, productID, price, quantity);
            if (result != null) {
                System.out.println(result);
            } else {
                System.out.println("Product could not be added.");
            }

            if (!yesOrNo("Add more products?")) {
                break;
            }

        } while (true);
    }//End addProducts

    public void addManufacturer() {
        String name = getToken("Enter manufacturer name");
        String address = getToken("Enter address");
        String phone = getToken("Enter phone");
        Manufacturer result;
        result = warehouse.addManufacturer(name, address, phone);
        if (result == null) {
            System.out.println("Could not add manufacturer");
        }
        System.out.println(result);
    }

    public void addSupplier() {

        do {
            String pId = getToken("Enter product id");
            Product p = warehouse.findProduct(pId);

            if (p != null) {

                String mId = getToken("Enter manufacturer id");
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

            if (!yesOrNo("Try again?")) {
                break;
            }

        } while (true);

    }

    public void deleteSupplier() {
        do {
            String pId = getToken("Enter product id");
            Product p = warehouse.findProduct(pId);

            if (p != null) {

                String mId = getToken("Enter manufacturer id");
                Manufacturer m;
                m = warehouse.findManufacturer(mId);
                if (m != null) {
                    warehouse.deleteSupplierFromProduct(p, m);
                    System.out.println("Supplier deleted.");
                    break;
                } else {
                    System.out.println("Could not find manufacturer.");

                }
            } else {
                System.out.println("Product not found.");
            }

            if (!yesOrNo("Try again?")) {
                break;
            }

        } while (true);
    }

    public void supplierList() {

        System.out.println("Show supplier list for product.");
        String pId = getToken("Enter product ID");

        if (warehouse.findProduct(pId) != null) {
            Iterator supplierList = warehouse.getSupplierList(pId);

            while (supplierList.hasNext()) {
                Supplier sl = (Supplier) (supplierList.next());
                System.out.println(sl.toString());
            }

        } else {
            System.out.println("Product not found.");

        }

    }

    public void acceptShipment() {
        do {
            String productId = getToken("Enter product ID: ");
            Product p = warehouse.findProduct(productId);
            int quantity;

            if (p != null) {

                quantity = getNumber("Enter quantity recieved:");
                for (Iterator waitList = warehouse.getWaitList(productId); waitList.hasNext();) {
                    Wait wait = (Wait) waitList.next();
                    System.out.println(wait.toString());
                    int clientQuantity = wait.getQuantity();
                    if (yesOrNo("Do you want to fulfill waitlist for this client?")) {
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
        } while (yesOrNo("Add more items?"));
    }//End of acceptOrders

    public void acceptOrders() {
        String clientId = getToken("Enter client ID: ");
        String productId;
        int quantity;
        double amount = 0;

        if (warehouse.findClient(clientId)) {
            //client is found so we create an order for a client
            warehouse.createOrder(clientId);
            do {
                productId = getToken("Enter product Id");

                Product tempProduct = warehouse.findProduct(productId);

                if (tempProduct == null) {
                    System.out.println("Product Not found.");

                } else {
                    quantity = Integer.parseInt(getToken("Enter quantity"));
                    warehouse.addToOrder(clientId, productId, quantity);
                    warehouse.updateClientBalance(clientId, tempProduct.getPrice() * quantity);
                    amount += (tempProduct.getPrice() * quantity);

                }

            } while (yesOrNo("Add more items?"));
            // after order is done we need to add it to the order list

            warehouse.addToOrderList();
            warehouse.addToTransactions(clientId, amount);
            warehouse.processOrder(clientId);
        } else {
            System.out.println("Client is not found.");
        }
    }//End of acceptOrders

    public void getTransactions() {
        Iterator result;
        String clientID = getToken("Enter client id");
        //Calendar date  = getDate("Please enter the date for which you want records as mm/dd/yy");
        result = warehouse.getTransactions(clientID);
        if (result == null) {
            System.out.println("Invalid Client ID");
        } else {
            while (result.hasNext()) {
                Transaction transaction = (Transaction) result.next();
                System.out.println(transaction.toString());
            }
            System.out.println("\n  There are no more transactions \n");
        }
    }//End of getTransactions

    public void acceptPayment() {
        double balance, payment;
        do {
            String clientID = getToken("Enter client ID: ");

            if (warehouse.findClient(clientID)) {
                //client is found so we accept a payment from a client

                balance = warehouse.getClientBalance(clientID);
                System.out.println("This client's balance is: " + balance);
                payment = Double.parseDouble(getToken("Enter payment amount"));
                warehouse.updateClientBalance(clientID, -payment);

                balance = warehouse.getClientBalance(clientID);
                System.out.println("This client's new balance is: " + balance);

            } else {
                System.out.println("Client is not found.");
            }

        } while (yesOrNo("Would you like to accept payment from another client?"));
    }//End of acceptPayment

    public void showProducts() {
        Iterator allProducts = warehouse.getProducts();
        while (allProducts.hasNext()) {
            Product product = (Product) (allProducts.next());
            System.out.println(product.toString());
        }
    }

    public void showClients() {
        Iterator allMembers = warehouse.getMembers();
        while (allMembers.hasNext()) {
            Client member = (Client) (allMembers.next());
            System.out.println(member.toString());
        }
    }

    public void showInvoice() {

        String clientId = getToken("Enter client ID");
        if (warehouse.findClient(clientId)) {
            Order o = warehouse.findOrder(clientId);
            if (o != null) {
                o.printOrder();
            }
        } else {
            System.out.println("Client is not found.");
        }

    }

    public void showWaitlist() {
        System.out.println("Show wait list for product.");
        String pId = getToken("Enter product ID");
        if (warehouse.findProduct(pId) != null) {
            Iterator wholeWaitList = warehouse.getWaitList(pId);
            while (wholeWaitList.hasNext()) {
                Wait waitList = (Wait) (wholeWaitList.next());
                System.out.println(waitList.toString());
            }
        } else {
            System.out.println("Product not found.");
        }
    }

    public void showManufacturers() {
        Iterator allManu = warehouse.getManufacturers();
        while (allManu.hasNext()) {
            Manufacturer manufacturer = (Manufacturer) (allManu.next());
            System.out.println(manufacturer.toString());
        }
    }

    public void getOverdueBalance() {
        Iterator allMembers = warehouse.getMembers();
        while (allMembers.hasNext()) {
            Client member = (Client) (allMembers.next());
            if (member.getBalance() > 0) {
                System.out.println(member.toStringBalance());

            }
        }

    }

    public void getClientBalance() {

        String clientId = getToken("Enter client ID");
        if (warehouse.findClient(clientId)) {
            //client is found so we create an order for a client
            double total = warehouse.getClientBalance(clientId);
            System.out.println("Total balance: " + total);

        } else {
            System.out.println("Client is not found.");
        }

    }

    private void save() {
        if (warehouse.save()) {
            System.out.println(" The warehouse has been successfully saved in the file WarehouseData \n");
        } else {
            System.out.println(" There has been an error in saving \n");
        }
    }

    private void retrieve() {
        try {
            Warehouse tempLibrary = Warehouse.retrieve();
            if (tempLibrary != null) {
                System.out.println(" The warehouse has been successfully retrieved from the file WarehouseData \n");
                warehouse = tempLibrary;
            } else {
                System.out.println("File doesnt exist; creating new warehouse");
                warehouse = Warehouse.instance();

            }
        } catch (Exception cnfe) {
            cnfe.printStackTrace();
        }
    }

    public void process() {
        int command;
        help();
        while ((command = getCommand()) != EXIT) {
            switch (command) {
                case ADD_CLIENT:
                    addClient();
                    break;
                case ADD_PRODUCTS:
                    addProducts();
                    break;
                case ADD_MANUFACTURER:
                    addManufacturer();
                    break;
                case ADD_SUPPLIER:
                    addSupplier();
                    break;
                case DELETE_SUPPLIER:
                    deleteSupplier();
                    break;
                case SUPPLIER_LIST:
                    supplierList();
                    break;

                case ACCEPT_ORDERS:
                    acceptOrders();
                    break;

                case ACCEPT_PAYMENT:
                    acceptPayment();
                    break;

                case GET_BALANCE:
                    getClientBalance();
                    break;
                case GET_OVERDUE:
                    getOverdueBalance();
                    break;

                case SHOW_WAITLIST:
                    showWaitlist();
                    break;
                case SHOW_INVOICE:
                    showInvoice();
                    break;
                case ACCEPT_SHIPMENT:
                    acceptShipment();
                    break;

                case GET_TRANSACTIONS:
                    getTransactions();
                    break;

                case SAVE:
                    save();
                    break;
                case RETRIEVE:
                    retrieve();
                    break;
                case SHOW_CLIENTS:
                    showClients();
                    break;
                case SHOW_PRODUCTS:
                    showProducts();
                    break;
                case SHOW_MANUFACTURERS:
                    showManufacturers();
                    break;
                case HELP:
                    help();
                    break;
            }
        }
    }

//This file is no longer needed.
/* 
    public static void main(String[] s) {
        UserInterface.instance().process();
    }
*/
    }
