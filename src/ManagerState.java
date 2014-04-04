/**
 *
 * @author Anil
 * @author Eric - Refactor pre GUI
 */
import java.awt.Container;
import java.awt.FlowLayout;
import java.io.*;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ManagerState extends WareState {

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private WareContext context;
    private static ManagerState instance;

    /*//manager specific calls
    private static final int EXIT = IOHelper.EXIT;
    private static final int ADD_CLIENT = 1;
    private static final int ADD_MANUFACTURER = 2;
    private static final int DELETE_SUPPLIER = 3;
    private static final int SHOW_MANUFACTURERS = 4;
    private static final int SHOW_CLIENTS = 5;
    private static final int SALES_MENU = 6; 
    private static final int HELP = IOHelper.HELP;
    */
    private JFrame managerFrame;
    private JPanel managerPanel;
    
    private final JButton logoutButton;
    private final JButton addClientButton;
    private final JButton addManufacturerButton;
    private final JButton deleteSupplierButton;
    private final JButton showManufacturersButton;
    private final JButton showClientsButton;
    private final JButton switchToSalesButton;
    
    private AddClientPanel addClientPanel;
    private AddManufacturerPanel addManufacturerPanel;
    private DeleteSupplierPanel deleteSupplierPanel;
    private ShowManufacturersPanel showManufacturersPanel;
    private ShowClientsPanel showClientsPanel;   

    private ManagerState() {
        super();
        warehouse = Warehouse.instance();
        
        managerPanel = new JPanel();

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout();
            }
        });

        addClientButton = new JButton("Add Client");
        addClientButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // TODO
                // if JPanel null, instantiate it here
                // set menu pane to new pain
                if (addClientPanel == null){
                    addClientPanel = new AddClientPanel();
                }
                refreshGUI(addClientPanel);
                //addClient();
            }
        });

        addManufacturerButton = new JButton("Add Manufacturer");
        addManufacturerButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // TODO
                // if JPanel null, instantiate it here
                // set menu pane to new pain
                if (addManufacturerPanel == null){
                    addManufacturerPanel = new AddManufacturerPanel();
                }
                refreshGUI(addManufacturerPanel);
                //addManufacturer();
            }
        });

        deleteSupplierButton = new JButton("Remove Supplier");
        deleteSupplierButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // TODO
                // if JPanel null, instantiate it here
                // set menu pane to new pain
                if (deleteSupplierPanel == null){
                    deleteSupplierPanel = new DeleteSupplierPanel();
                }
                refreshGUI(deleteSupplierPanel);
            }
        });

        showManufacturersButton = new JButton("Show Manufacturers");
        showManufacturersButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // TODO
                // if JPanel null, instantiate it here
                // set menu pane to new pain
                if (showManufacturersPanel == null){
                    showManufacturersPanel = new ShowManufacturersPanel();
                }
                refreshGUI(showManufacturersPanel);
                showManufacturers();
            }
        });

        showClientsButton = new JButton("Show Clients");
        showClientsButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // TODO
                // if JPanel null, instantiate it here
                // set menu pane to new pain
                if (showClientsPanel == null){
                    showClientsPanel = new ShowClientsPanel();
                }
                refreshGUI(showClientsPanel);
                
                showClients();                
            }
        });
        
        switchToSalesButton = new JButton("Switch to Sales");
        switchToSalesButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salesMenu();
            }
        });
    }
    
    private void refreshGUI(JPanel showPanel) {
        managerPanel.removeAll();
        managerPanel.add(showPanel);
        managerFrame.validate();
    }

    public static ManagerState instance() {
        if (instance == null) {
            instance = new ManagerState();
        }
        return instance;
    }
    
    public boolean addClient(String name, String address, String phone) {
        Client result;

        if (name.equals("") || address.equals("") || phone.equals("")) {
            JOptionPane.showMessageDialog(managerFrame, "Client could not be added.");
            return false;
        }

        result = warehouse.addMember(name, address, phone);

        if (result != null) {
            JOptionPane.showMessageDialog(managerFrame, result);
        } else {
            JOptionPane.showMessageDialog(managerFrame, "Client could not be added.");
            return false;
        }
        return true;
    }//End addClients

    public boolean addManufacturer(String name, String address, String phone) {
        Manufacturer result;

        if (name.equals("") || address.equals("") || phone.equals("")) {
            JOptionPane.showMessageDialog(managerFrame, "Manufacturer could not be added.");
            return false;
        }

        result = warehouse.addManufacturer(name, address, phone);

        if (result != null) {
            JOptionPane.showMessageDialog(managerFrame, result);
        } else {
            JOptionPane.showMessageDialog(managerFrame, "Manufacturer could not be added.");
            return false;
        }
        return true;
    }//End addManufacturer
        
    private void deleteSupplier() {
        do {
            String pId = IOHelper.getToken("Enter product id");
            Product p = warehouse.findProduct(pId);

            if (p != null) {

                String mId = IOHelper.getToken("Enter manufacturer id");
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

            if (!IOHelper.yesOrNo("Try again?")) {
                break;
            }

        } while (true);
    }

    private void showManufacturers() {
        Iterator allManu = warehouse.getManufacturers();
        String manufacturerList = "";
        
        while (allManu.hasNext()) {
            Manufacturer manufacturer = (Manufacturer) (allManu.next());
            manufacturerList = manufacturerList + manufacturer.toString() + "\n";
            //System.out.println(manufacturer.toString());
        }
        showManufacturersPanel.manufacturersTextView.setText(manufacturerList);
        //showManufacturersPanel.jTextArea.setText("Manufacturer List: \n" + manufacturerList);
    }

    private void showClients() {
        Iterator allMembers = warehouse.getMembers();
        String clientList = "";
        
        while (allMembers.hasNext()) {
            Client member = (Client) (allMembers.next());
            clientList = clientList + member.toString() + "\n";           
        }
        
        showClientsPanel.clientsTextView.setText(clientList);
    }

    private void salesMenu() {
        (WareContext.instance()).changeState(WareContext.SALES_STATE); //go to sales state
    }


    public void logout() {
        (WareContext.instance()).changeState(WareContext.LOGIN_STATE); // exit
    }

    @Override
    public void run() {
        //process();
        managerFrame = WareContext.instance().getFrame();
        Container pane = managerFrame.getContentPane();
        pane.removeAll();
        pane.setLayout(new FlowLayout());
        pane.add(this.logoutButton);
        pane.add(this.addClientButton);
        pane.add(this.addManufacturerButton);
        pane.add(this.deleteSupplierButton);
        pane.add(this.showManufacturersButton);
        pane.add(this.showClientsButton);
        pane.add(this.switchToSalesButton);
        
        pane.add(managerPanel);
        
        managerFrame.setVisible(true);
        managerFrame.paint(managerFrame.getGraphics());
    }
}
