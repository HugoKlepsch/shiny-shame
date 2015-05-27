/*
		 Title: ClientMain.java
		 Programmer: hugo
		 Date of creation: May 26, 2015
		 Description: A quick and dirty computer client for the squad messenger beta
*/


package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author hugo
 *
 */
public class ClientMain {
	private static BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
	private static int localIndex = 0;
	private static boolean stayAlive = true;
	
	/**
		 * @author hugo
		 * Date of creation: May 26, 2015 
		 * @param: None
		 * @return: None
	 * @throws IOException 
		 * @Description: ( ͡° ͜ʖ ͡°)
		 */
	public static void main(String[] args) throws IOException {
		System.out.println("Enter username to connect as: ");
		String userName = userIn.readLine();
		System.out.println("Enter the IP address to connect to: ");
		String address = userIn.readLine();
		
		//TODO constructor
		
		System.out.println("Type \"Exit\" to close");
		String message =  "";
		while(!(message.equals("Exit"))){
			message = userIn.readLine();
			if (!(message.equals("Exit"))) {
				
			}
		}
	
	}

	/**
	 * @return the localIndex
	 */
	public static int getLocalIndex() {
		return localIndex;
	}

	/**
	 * @param localIndex the localIndex to set
	 */
	public static void setLocalIndex(int localIndex) {
		ClientMain.localIndex = localIndex;
	}

	/**
	 * @return the stayAlive
	 */
	public static boolean StayAlive() {
		return stayAlive;
	}

	/**
	 * @param stayAlive the stayAlive to set
	 */
	public static void setAlive(boolean stayAlive) {
		ClientMain.stayAlive = stayAlive;
	}

}
