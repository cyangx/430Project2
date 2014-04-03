/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Anil
 * @author Eric - Refactor pre GUI
 */
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JFrame;

public class ManagerState extends WareState implements ActionListener {

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private WareContext context;
    private static ManagerState instance;

    //manager specific calls
    private static final int EXIT = IOHelper.EXIT;
    private static final int ADD_CLIENT = 1;
    private static final int ADD_MANUFACTURER = 2;
    private static final int DELETE_SUPPLIER = 3;
    private static final int SHOW_MANUFACTURERS = 4;
    private static final int SHOW_CLIENTS = 5;
    private static final int SALES_MENU = 6;
    private static final int HELP = IOHelper.HELP;

    private JFrame managerFrame;

    private JButton logoutButton;
    private JButton addClientButton;
    private JButton addManufacturerButton;
    private JButton deleteSupplierButton;
    private JButton showManufacturersButton;
    private JButton showClientsButton;

    private ManagerState() {
        super();
        warehouse = Warehouse.instance();

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
            }
        });

        addManufacturerButton = new JButton("Add Manufacturer");
        addManufacturerButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // TODO
                // if JPanel null, instantiate it here
                // set menu pane to new pain
            }
        });

        deleteSupplierButton = new JButton("Remove Supplier");
        deleteSupplierButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // TODO
                // if JPanel null, instantiate it here
                // set menu pane to new pain
            }
        });

        showManufacturersButton = new JButton("Show Manufacturers");
        showManufacturersButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // TODO
                // if JPanel null, instantiate it here
                // set menu pane to new pain
            }
        });

        showClientsButton = new JButton("Show Clients");
        showClientsButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // TODO
                // if JPanel null, instantiate it here
                // set menu pane to new pain
            }
        });
    }

    public static ManagerState instance() {
        if (instance == null) {
            instance = new ManagerState();
        }
        return instance;
    }

    private void addClient() {
        String name = IOHelper.getToken("Enter client name");
        String address = IOHelper.getToken("Enter address");
        String phone = IOHelper.getToken("Enter phone");
        Client result;
        result = warehouse.addMember(name, address, phone);
        if (result == null) {
            System.out.println("Could not add client");
        }
        System.out.println(result);
    }

    private void addManufacturer() {
        String name = IOHelper.getToken("Enter manufacturer name");
        String address = IOHelper.getToken("Enter address");
        String phone = IOHelper.getToken("Enter phone");
        Manufacturer result;
        result = warehouse.addManufacturer(name, address, phone);
        if (result == null) {
            System.out.println("Could not add manufacturer");
        }
        System.out.println(result);
    }

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
        while (allManu.hasNext()) {
            Manufacturer manufacturer = (Manufacturer) (allManu.next());
            System.out.println(manufacturer.toString());
        }
    }

    private void showClients() {
        Iterator allMembers = warehouse.getMembers();
        while (allMembers.hasNext()) {
            Client member = (Client) (allMembers.next());
            System.out.println(member.toString());
        }
    }

    private void salesMenu() {
        (WareContext.instance()).changeState(WareContext.SALES_STATE); //go to sales state
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

    private void help() {
        IOHelper.Println("Manager Menu");
        IOHelper.Println("Enter a number between " + EXIT + " and " + HELP + " as explained below:");
        IOHelper.Println(EXIT + " to Exit\n");
        IOHelper.Println(ADD_CLIENT + " to add a client");
        IOHelper.Println(ADD_MANUFACTURER + " to add manufacturer");
        IOHelper.Println(DELETE_SUPPLIER + " to delete supplier");
        IOHelper.Println(SHOW_MANUFACTURERS + " to  display all manufacturers");
        IOHelper.Println(SHOW_CLIENTS + " to  display all clients");
        IOHelper.Println(SALES_MENU + " to  switch to the Sales Person menu");
        IOHelper.Println(HELP + " for help");
    }

    public void logout() {
        (WareContext.instance()).changeState(WareContext.LOGIN_STATE); // exit
    }

    public void process() {
        int command;
        help();
        while ((command = IOHelper.GetCmd()) != EXIT) {
            switch (command) {
                case ADD_CLIENT:
                    addClient();
                    break;
                case ADD_MANUFACTURER:
                    addManufacturer();
                    break;
                case DELETE_SUPPLIER:
                    deleteSupplier();
                    break;
                case SHOW_MANUFACTURERS:
                    showManufacturers();
                    break;
                case SHOW_CLIENTS:
                    showClients();
                    break;
                case SALES_MENU:
                    salesMenu();
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
        }
    }

    public void run() {
        //process();
        managerFrame = WareContext.instance().getFrame();
        Container pane = managerFrame.getContentPane();
        pane.removeAll();
        pane.setLayout(new FlowLayout());
        pane.add(this.logoutButton);
        // TODO: add other buttons and items here

        managerFrame.setVisible(true);
        managerFrame.paint(managerFrame.getGraphics());
    }
}
