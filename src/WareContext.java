import java.util.*;
import java.text.*;
import java.io.*;
public class WareContext {
  
  private int currentState;
  private static Warehouse warehouse;
  private static WareContext context;
  private int currentUser;
  private String userID;
  private BufferedReader reader = new BufferedReader(new 
                                      InputStreamReader(System.in));
  public static final int IsManager = 0;
  public static final int IsSales = 1;
  public static final int IsClient = 2;
  public static final int MANAGER_STATE = 0;
  public static final int SALES_STATE = 1;
  public static final int CLIENT_STATE = 2;
  public static final int LOGIN_STATE = 3;
  private WareState[] states;
  private int[][] nextState;

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

  private void retrieve() {
    try {
      Warehouse tempWarehouse = Warehouse.retrieve();
      if (tempWarehouse != null) {
        System.out.println(" The warehouse has been successfully retrieved from the file WarehouseData \n" );
        warehouse = tempWarehouse;
      } else {
        System.out.println("File doesnt exist; creating new warehouse" );
        warehouse = Warehouse.instance();
      }
    } catch(Exception cnfe) {
      cnfe.printStackTrace();
    }
  }

  public void setLogin(int code)
  {currentUser = code;}

  public void setUser(String uID)
  { userID = uID;}

  public int getLogin()
  { return currentUser;}

  public String getUser()
  { return userID;}

  private WareContext() { 
//constructorSystem.out.println("In WareContext constructor");
    if (yesOrNo("Look for saved data and  use it?")) {
      retrieve();
    } else {
      warehouse = Warehouse.instance();
    }
    // set up the FSM and transition table;
    states = new WareState[4];
    states[0] = ManagerState.instance();
    states[1] = SalesState.instance();
    states[2] = ClientState.instance(); 
    states[3]=  Loginstate.instance();

    nextState = new int[4][4];
    nextState[MANAGER_STATE][MANAGER_STATE] = -1;
    nextState[MANAGER_STATE][SALES_STATE] = SALES_STATE;
    nextState[MANAGER_STATE][CLIENT_STATE] = -2;
    nextState[MANAGER_STATE][LOGIN_STATE] = LOGIN_STATE;
    
    nextState[SALES_STATE][MANAGER_STATE] = MANAGER_STATE;
    nextState[SALES_STATE][SALES_STATE] = -1;
    nextState[SALES_STATE][CLIENT_STATE] = CLIENT_STATE;
    nextState[SALES_STATE][LOGIN_STATE] = LOGIN_STATE;
    
    nextState[CLIENT_STATE][MANAGER_STATE] = -2;
    nextState[CLIENT_STATE][SALES_STATE] = SALES_STATE;
    nextState[CLIENT_STATE][CLIENT_STATE] = -1;
    nextState[CLIENT_STATE][LOGIN_STATE] = LOGIN_STATE;
    
    nextState[LOGIN_STATE][MANAGER_STATE] = MANAGER_STATE;
    nextState[LOGIN_STATE][SALES_STATE] = SALES_STATE;
    nextState[LOGIN_STATE][CLIENT_STATE] = CLIENT_STATE;
    nextState[LOGIN_STATE][LOGIN_STATE] = -1;
    
    currentState = LOGIN_STATE;
  }

  public void changeState(int transition)
  {
    //System.out.println("current state " + currentState + " \n \n ");
    currentState = nextState[currentState][transition];
    if (currentState == -2) 
      {System.out.println("Error has occurred"); terminate();}
    if (currentState == -1) 
      terminate();
    //System.out.println("current state " + currentState + " \n \n ");
    states[currentState].run();
  }

  private void terminate()
  {
   if (yesOrNo("Save data?")) {
      if (warehouse.save()) {
         System.out.println(" The warehouse has been successfully saved in the file WarehouseData \n" );
       } else {
         System.out.println(" There has been an error in saving \n" );
       }
     }
   System.out.println(" Goodbye \n "); System.exit(0);
  }

  public static WareContext instance() {
    if (context == null) {
       System.out.println("calling constructor");
      context = new WareContext();
    }
    return context;
  }

  public void process(){
    states[currentState].run();
  }
  
  public static void main (String[] args){
    WareContext.instance().process(); 
  }
}
