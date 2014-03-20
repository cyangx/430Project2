/**
 * @(#)Security.java
 *
 *
 * @author
 * @version 1.00 2014/3/20
 */


public class Security {

	public boolean verifyPassword(String userID, String password, int state){
		boolean verified = verifyPasswordHidden(userID,password,state);
		return verified;
	}

    private boolean verifyPasswordHidden(String userID, String password, int state){
  	if(state == 0){
  		if(userID.equals("manager") && password.equals("manager")){
  			System.out.println("Return true.");
  			return true;
  		}
  	}//End of if statement
  	else if (state == 1){
  		if(userID.equals("salesclerk") && password.equals("salesclerk"))
  			return true;
  	}//End of else if statement
    else if(state == 2){
    	if(userID.equals(password))
    			return true;
    }//End of else statement

	return false;
  }//End of verifyPassword


}