
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class Loginstate extends WareState implements ActionListener {

    private static final int MANAGER_LOGIN = 0;
    private static final int CLERK_LOGIN = 1;
    private static final int CLIENT_LOGIN = 2;
    private static final int EXIT = 3;
    private Security security = new Security();
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private WareContext context;
    private static Loginstate instance;

    private JButton clientButton, salesButton, managerButton;

    private JFrame loginFrame;

    private Loginstate() {
        super();

        salesButton = new JButton("Sales Clerk");
        salesButton.addActionListener(this);
        clientButton = new JButton("Client");
        clientButton.addActionListener(this);
        managerButton = new JButton("Manager");
        managerButton.addActionListener(this);

//userButton.addActionListener(this);
        //logoutButton.addActionListener(this);
        //ClerkButton.instance().setListener();
        // context = WareContext.instance();
    }

    public void actionPerformed(ActionEvent event) {

        if (event.getSource().equals(this.clientButton)) {

            //WareContext.instance().changeState(WareContext.CLIENT_STATE);
            client();
        } else if (event.getSource().equals(this.salesButton)) {
            //WareContext.instance().changeState(WareContext.SALES_STATE);
            sales();
        } else if (event.getSource().equals(this.managerButton)) {
            //WareContext.instance().changeState(WareContext.MANAGER_STATE);
            manager();
        }
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
        /*String managerID = getToken("Please input the manager id: ");
        String password = getToken("Please input the manager password: ");
        if (security.verifyPassword(managerID, password, 0) == true) {
            (WareContext.instance()).setLogin(WareContext.IsManager);
            (WareContext.instance()).changeState(WareContext.MANAGER_STATE);
        } else {
            System.out.println("not the same");
        }*/

        String userID = JOptionPane.showInputDialog(
                loginFrame, "Please input sales username: ");

        String password = JOptionPane.showInputDialog(loginFrame,
                "Enter Password");

        if (security.verifyPassword(userID, password, WareContext.MANAGER_STATE)) {
            IOHelper.Println("dummy message1");
            JOptionPane.showMessageDialog(loginFrame, "Successful, look at console");
            (WareContext.instance()).setLogin(WareContext.IsManager);
            (WareContext.instance()).changeState(WareContext.MANAGER_STATE);
            IOHelper.Println("dummy message2");

        } else {
            JOptionPane.showMessageDialog(loginFrame, "Incorrect Password");
        }
    }

    private void sales() {
        String userID = JOptionPane.showInputDialog(
                loginFrame, "Please input sales username: ");

        String password = JOptionPane.showInputDialog(loginFrame,
                "Enter Password");

        if (security.verifyPassword(userID, password, WareContext.SALES_STATE)) {
            IOHelper.Println("dummy message1");
            JOptionPane.showMessageDialog(loginFrame, "Successful, look at console");
            (WareContext.instance()).setLogin(WareContext.IsSales);
            (WareContext.instance()).changeState(WareContext.SALES_STATE);
            IOHelper.Println("dummy message2");

        } else {
            JOptionPane.showMessageDialog(loginFrame, "Incorrect Password");
        }
    }

    private void client() {
        String userID = JOptionPane.showInputDialog(
                loginFrame, "Please input the user id: ");
        if (Warehouse.instance().findClient(userID) == true) {
            String password = JOptionPane.showInputDialog(loginFrame,
                    "Enter Password");
            if (security.verifyPassword(userID, password, WareContext.CLIENT_STATE)) {
                JOptionPane.showMessageDialog(loginFrame, "Successful, look at console");
                (WareContext.instance()).setLogin(WareContext.IsClient);
                (WareContext.instance()).setUser(userID);
                (WareContext.instance()).changeState(WareContext.CLIENT_STATE);

                
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Incorrect Password");
            }
        } else {
            JOptionPane.showMessageDialog(loginFrame, "Invalid user id.");
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
                    sales();
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
        //process();
        System.out.println("here");
        loginFrame = WareContext.instance().getFrame();
        loginFrame.getContentPane().removeAll();
        loginFrame.getContentPane().setLayout(new FlowLayout());
        loginFrame.getContentPane().add(this.clientButton);
        loginFrame.getContentPane().add(this.salesButton);
        loginFrame.getContentPane().add(this.managerButton);
        loginFrame.setVisible(true);
        loginFrame.paint(loginFrame.getGraphics());

    }
}
