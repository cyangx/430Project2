
import java.util.*;
import java.io.*;

public class Loginstate extends WareState {
    private static final int MANAGER_LOGIN = 0;
    private static final int CLERK_LOGIN = 1;
    private static final int CLIENT_LOGIN = 2;
    private static final int EXIT = 3;
    private Security security = new Security();
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private WareContext context;
    private static Loginstate instance;
    
    private Loginstate() {
        super();
        // context = WareContext.instance();
    }

    public static Loginstate instance() {
        if (instance == null) {
            instance = new Loginstate();
        }
        return instance;
    }

    public int getCommand() {
        do {
            try {
                int value = Integer.parseInt(getToken("Enter command:"));
                if (value <= EXIT && value >= MANAGER_LOGIN) {
                    return value;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Enter a number");
            }
        } while (true);
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

    private void manager() {
        String managerID = getToken("Please input the manager id: ");
        String password = getToken("Please input the manager password: ");
        if (security.verifyPassword(managerID, password, 0) == true) {
            (WareContext.instance()).setLogin(WareContext.IsManager);
            (WareContext.instance()).changeState(0);
        }

    }

    private void clerk() {
        String clerkID = getToken("Please input the salesclerk id: ");
        String password = getToken("Please input the salesclerk password: ");
        if (security.verifyPassword(clerkID, password, 1) == true) {
            System.out.println("Password verified. Continuing.");
            (WareContext.instance()).setLogin(WareContext.IsSales);
            (WareContext.instance()).changeState(1);
        }
    }

    private void client() {
        String userID = getToken("Please input the client id: ");
        String password = getToken("Please input the client password: ");
        if (Warehouse.instance().findClient(userID) == true) {
            if(security.verifyPassword(userID,password,2) == true){
            (WareContext.instance()).setLogin(WareContext.IsClient);
            (WareContext.instance()).setUser(userID);
            (WareContext.instance()).changeState(2);
            }
        } else {
            System.out.println("Invalid user id.");
        }
    }

    public void process() {
        int command;
        /*System.out.println("Please input 0 to login as Clerk\n"+
         "input 1 to login as user\n" +
         "input 2 to exit the system\n");*/
        System.out.println("Please input 0 to login as Manager\n"
                + "input 1 to login as Salesclerk\n"
                + "input 2 to login as Client\n"
                + "input 3 to exit the system\n");

        while ((command = getCommand()) != EXIT) {

            switch (command) {
                case MANAGER_LOGIN:
                    manager();
                    break;
                case CLERK_LOGIN:
                    clerk();
                    break;
                case CLIENT_LOGIN:
                    client();
                    break;
                default:
                    System.out.println("Invalid choice");

            }
            /*System.out.println("Please input 0 to login as Clerk\n"+
             "input 1 to login as user\n" +
             "input 2 to exit the system\n"); */
            System.out.println("Please input 0 to login as Manager\n"
                    + "input 1 to login as Salesclerk\n"
                    + "input 2 to login as Client\n"
                    + "input 3 to exit the system\n");
        }
        //(WareContext.instance()).changeState(2);
        (WareContext.instance()).changeState(3);
    }

    public void run() {
        process();
    }
}
