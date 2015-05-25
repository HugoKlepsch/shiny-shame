/*
		 Title: LoginDeets.java
		 Programmer: hugo
		 Date of creation: May 25, 2015
		 Description: 
*/


package sharedPackages;

/**
 * @author hugo
 *
 */
public class LoginDeets {
	String userName;
    String password;
    public LoginDeets(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public String toString(){
        return "userName: " + userName + " password: " + password;
    }
}
