
import java.util.*;
import java.io.*;

public class ClientState extends WareState {

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private WareContext context;
    private static ClientState instance;
    private static final int EXIT = IOHelper.EXIT;
    private static final int ACCEPT_ORDERS = 1;
    private static final int GET_BALANCE = 2;
    private static final int GET_TRANSACTIONS = 3;
    private static final int SHOW_PRODUCTS = 4;
    private static final int HELP = IOHelper.HELP;

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

    public void acceptOrders() {
        String clientId = WareContext.instance().getUser();
        String productId;
        int quantity;
        double amount = 0;

        if (warehouse.findClient(clientId)) {
            //client is found so we create an order for a client
            warehouse.createOrder(clientId);
            do {
                productId = IOHelper.getToken("Enter product Id");
                Product tempProduct = warehouse.findProduct(productId);

                if (tempProduct == null) {
                    System.out.println("Product Not found.");

                } else {
                    quantity = Integer.parseInt(IOHelper.getToken("Enter quantity"));
                    warehouse.addToOrder(clientId, productId, quantity);
                    warehouse.updateClientBalance(clientId, tempProduct.getPrice() * quantity);
                    amount += (tempProduct.getPrice() * quantity);
                }
            } while (IOHelper.yesOrNo("Add more items?"));
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
        IOHelper.Println("Enter a number between " + EXIT + " and " + HELP + " as explained below:");
        IOHelper.Println(EXIT + " to Exit\n");
        IOHelper.Println(ACCEPT_ORDERS + " to accept orders from a client");
        IOHelper.Println(GET_BALANCE + " to get a client balance ");
        IOHelper.Println(GET_TRANSACTIONS + " to show transaction list of a client");
        IOHelper.Println(SHOW_PRODUCTS + " to  print products");
        IOHelper.Println(HELP + " for help");
    }

    public void logout() {
        if ((WareContext.instance()).getLogin() == WareContext.IsManager) {
            System.out.println(" going to sales \n ");
            (WareContext.instance()).changeState(WareContext.SALES_STATE); // exit with a code 1
            
        } else if (WareContext.instance().getLogin() == WareContext.IsSales) {
            System.out.println(" going to sales \n");
            (WareContext.instance()).changeState(WareContext.SALES_STATE); // exit with a code 2
            
        } else if (WareContext.instance().getLogin() == WareContext.IsClient) {
            System.out.println(" going to login \n");
            (WareContext.instance()).changeState(WareContext.LOGIN_STATE); 
            
        } else {
            (WareContext.instance()).changeState(WareContext.CLIENT_STATE); // exit code 2, indicates error
        }
    }

    public void process() {
        int command;
        help();
        while ((command = IOHelper.GetCmd()) != EXIT) {
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
