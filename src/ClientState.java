import java.util.*;
import java.text.*;
import java.io.*;

public class ClientState extends WareState {

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private WareContext context;
    private static ClientState instance;
    private static final int EXIT = 0;
    private static final int ACCEPT_ORDERS = 4;
    private static final int GET_BALANCE = 9;
    private static final int GET_TRANSACTIONS = 14;
    private static final int SHOW_PRODUCTS = 17;
    private static final int HELP = 20;

    private ClientState() {
        super();
        warehouse = Warehouse.instance();
    }

    public static ClientState instance() {
        if (instance == null) {
            instance = new ClientState();
        }
        return instance;
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

    public void acceptOrders() {
        String clientId = WareContext.instance().getUser();
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

    public void showProducts() {
        Iterator allProducts = warehouse.getProducts();
        while (allProducts.hasNext()) {
            Product product = (Product) (allProducts.next());
            System.out.println(product.toString());
        }
    }

    public void getClientBalance() {
        String clientId = WareContext.instance().getUser();
        if (warehouse.findClient(clientId)) {
            //client is found so we create an order for a client
            double total = warehouse.getClientBalance(clientId);
            System.out.println("Total balance: " + total);
        } else {
            System.out.println("Client is not found.");
        }

    }

    public void getTransactions() {
        Iterator result;
        String clientId = WareContext.instance().getUser();
        //Calendar date  = getDate("Please enter the date for which you want records as mm/dd/yy");
        result = warehouse.getTransactions(clientId);
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

    public void help() {
        System.out.println("Enter a number between 0 and 20 as explained below:");
        System.out.println(EXIT + " to Exit\n");
        System.out.println(ACCEPT_ORDERS + " to accept orders from a client");
        System.out.println(GET_BALANCE + " to get a client balance ");
        System.out.println(GET_TRANSACTIONS + " to show transaction list of a client");
        System.out.println(SHOW_PRODUCTS + " to  print products");
        System.out.println(HELP + " for help");
    }

    public void logout() {
        (WareContext.instance()).changeState(0); // exit with a code 0
    }

    public void process() {
        int command;
        help();
        while ((command = getCommand()) != EXIT) {
            switch (command) {
                case ACCEPT_ORDERS:
                    acceptOrders();
                    break;
                case GET_BALANCE:
                    getClientBalance();
                    break;
                case GET_TRANSACTIONS:
                    getTransactions();
                    break;
                case SHOW_PRODUCTS:
                    showProducts();
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
