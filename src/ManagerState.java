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
    private static final int EXIT = IOHelper.EXIT;
    private static final int ADD_CLIENT = 1;
    private static final int ADD_MANUFACTURER = 2;
    private static final int DELETE_SUPPLIER = 3;
    private static final int SHOW_MANUFACTURERS = 4;
    private static final int SHOW_CLIENTS = 5;
    private static final int SALES_MENU = 6;
    private static final int HELP = IOHelper.HELP;
    
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
    
    private void salesMenu()
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
    
    public void logout()
    {
        (WareContext.instance()).changeState(WareContext.LOGIN_STATE); // exit
    }
    
    public void process() {
        int command;
        help();
        while ((command = IOHelper.GetCmd()) != EXIT) {
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
