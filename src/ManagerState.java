/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Anil
 */

import java.util.*;
import java.text.*;
import java.io.*;

public class ManagerState extends WareState {
 // TODO:
 // Become sales
    // Print clients
    // Print manufacturers
    // remove supplier from product
    // add supplier to product
    // add client
    
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private WareContext context;
    private static ManagerState instance;
  
    //manager specific calls
    private static final int EXIT = 0;
    private static final int ADD_CLIENT = 1;
    private static final int ADD_MANUFACTURER = 5;
    private static final int DELETE_SUPPLIER = 6;
    private static final int SHOW_MANUFACTURERS = 15;
    private static final int SHOW_CLIENTS = 16;
    private static final int SALES_MENU = 17;
    //common calls
    private static final int SAVE = 18;
    private static final int RETRIEVE = 19;
    private static final int HELP = 20;
    
    private ManagerState() {
        super();
        warehouse = Warehouse.instance();
        //context = WareContext.instance();
    }

    public static ManagerState instance() {
        if (instance == null) {
            instance = new ManagerState();
        }
        return instance;
    }
    
    public String getToken(String prompt) {
        do {
            try {
                System.out.println(prompt);
                String line = reader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
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
    
    public void showManufacturers() {
        Iterator allManu = warehouse.getManufacturers();
        while (allManu.hasNext()) {
            Manufacturer manufacturer = (Manufacturer) (allManu.next());
            System.out.println(manufacturer.toString());
        }
    }
    
    public void showClients() {
        Iterator allMembers = warehouse.getMembers();
        while (allMembers.hasNext()) {
            Client member = (Client) (allMembers.next());
            System.out.println(member.toString());
        }
    }
    
    public void salesMenu()
    {     
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
    
    public void help() {
        System.out.println("Enter a number between 0 and 12 as explained below:");
        System.out.println(EXIT + " to Exit\n");
        System.out.println(ADD_CLIENT + " to add a client");
        System.out.println(ADD_MANUFACTURER + " to add manufacturer");
        System.out.println(DELETE_SUPPLIER + " to delete supplier");
        System.out.println(SHOW_MANUFACTURERS + " to  display all manufacturers");
        System.out.println(SHOW_CLIENTS + " to  display all clients");
        System.out.println(SALES_MENU + " to  switch to the Sales Person menu");
        System.out.println(SAVE + " to  save the data");
        System.out.println(RETRIEVE + " to retreive the stored data");
        System.out.println(HELP + " for help");
    }
    
    public void logout()
    {
        (WareContext.instance()).changeState(WareContext.MANAGER_STATE); // exit
    }
    
    public void process() {
        int command;
        help();
        while ((command = getCommand()) != EXIT) {
            switch (command) {
                case ADD_CLIENT: addClient();
                                break;
                case ADD_MANUFACTURER: addManufacturer();
                                break;
                case DELETE_SUPPLIER: deleteSupplier();
                                break;
                case SHOW_MANUFACTURERS: showManufacturers();
                                break;
                case SHOW_CLIENTS: showClients();
                                break;
                case SALES_MENU: salesMenu();
                                break;
                case SAVE: save();
                                break;
                case RETRIEVE: retrieve();
                                break;
                case HELP: help();
                                break;
            }
        }
        logout();
    }
    
    public void run() {
        process();
    }
}
