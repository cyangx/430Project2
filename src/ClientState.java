
import java.awt.Container;
import java.awt.FlowLayout;
import java.io.*;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Cha Yang
 *
 */
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

    private JFrame clientFrame;

    private final JButton logoutButton;
    private final JButton createOrderButton;
    private final JButton showBalanceButton;
    private final JButton showTransactionsButton;
    private final JButton showProductsButton;

    private JPanel clientMenuPanel;
    private ShowClientTransactionsPanel transactionPanel;
    private ShowClientBalancePanel balancePanel;
    private AcceptOrdersPanel acceptOrderPanel;
    private ShowProductsPanel productsPanel;

    private ClientState() {
        super();
        warehouse = Warehouse.instance();

        clientMenuPanel = new JPanel();

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout();
            }
        });

        createOrderButton = new JButton("Create Order");
        createOrderButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (acceptOrderPanel == null) {
                    acceptOrderPanel = new AcceptOrdersPanel();
                }
                refreshGUI(acceptOrderPanel);
            }
        });

        showBalanceButton = new JButton("View Balance");
        showBalanceButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (balancePanel == null) {
                    balancePanel = new ShowClientBalancePanel();
                }
                refreshGUI(balancePanel);
                getClientBalance();
            }
        });

        showTransactionsButton = new JButton("View Transactions");
        showTransactionsButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (transactionPanel == null) {
                    transactionPanel = new ShowClientTransactionsPanel();
                }
                refreshGUI(transactionPanel);
                getTransactions();
            }
        });

        showProductsButton = new JButton("View Products");
        showProductsButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (productsPanel == null) {
                    productsPanel = new ShowProductsPanel();
                }
                refreshGUI(productsPanel);
                showProducts();
            }
        });
    }

    private void refreshGUI(JPanel showPanel) {
        clientMenuPanel.removeAll();
        clientMenuPanel.add(showPanel);
        clientFrame.validate();
    }

    public static ClientState instance() {
        if (instance == null) {
            instance = new ClientState();
        }
        return instance;
    }

    private void acceptOrdersOLD() {
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

    public void processOrder(double amount) {
        String clientId = WareContext.instance().getUser();
        
        warehouse.addToOrderList(); // adds a order into an order list
        warehouse.addToTransactions(clientId, amount);
        warehouse.processOrder(clientId);
    }

    public double acceptOrders(String productId, int quantity) {
        String clientId = WareContext.instance().getUser();
        double amount = 0;
        warehouse.createOrder(clientId);
        Product tempProduct = warehouse.findProduct(productId);

        if (tempProduct != null) {
            warehouse.addToOrder(clientId, productId, quantity);
            warehouse.updateClientBalance(clientId, tempProduct.getPrice() * quantity);
            amount += (tempProduct.getPrice() * quantity);
            acceptOrderPanel.jTextArea2.append(productId + " " + quantity + "\n");

        } else {

            JOptionPane.showMessageDialog(clientFrame, "Product ID not found.");
            return 0;
        }
        return amount;
    }//End of acceptOrders

    private void showProducts() {
        Iterator allProducts = warehouse.getProducts();
        String prodList = "";

        while (allProducts.hasNext()) {
            Product product = (Product) (allProducts.next());
            prodList = prodList + product.toString() + "\n";
        }
        productsPanel.jTextArea1.setText("Product list: \n" + prodList);
    }

    private void getClientBalance() {
        String clientId = WareContext.instance().getUser();
        Client client = warehouse.getClient(clientId);
        double total = client.getBalance();
        balancePanel.nameTextBox.setText(client.getName());
        balancePanel.balanceTextBox.setText(Double.toString(total));

    }

    private void getTransactions() {
        String clientId = WareContext.instance().getUser();
        Client client = warehouse.getClient(clientId);
        Iterator result = warehouse.getTransactions(clientId);
        String transList = "";

        while (result.hasNext()) {
            Transaction transaction = (Transaction) (result.next());
            transList = transList + transaction.toString() + "\n";
        }
        transactionPanel.nameTextBox.setText(client.getName());
        transactionPanel.transactionTextView.setText("Transaction list: \n" + transList);
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

    @Override
    public void run() {
        //process();
        clientFrame = WareContext.instance().getFrame();
        Container pane = clientFrame.getContentPane();
        pane.removeAll();
        pane.setLayout(new FlowLayout());
        pane.add(this.logoutButton);
        pane.add(this.createOrderButton);
        pane.add(this.showBalanceButton);
        pane.add(this.showTransactionsButton);
        pane.add(this.showProductsButton);
        pane.add(this.clientMenuPanel);

        clientFrame.setVisible(true);
        clientFrame.paint(clientFrame.getGraphics());
    }
}
