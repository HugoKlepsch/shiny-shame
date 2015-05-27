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
	private int localIndex = 0;
	private boolean stayAlive = true;
	
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
	}

	/**
	 * @return the localIndex
	 */
	public int getLocalIndex() {
		return localIndex;
	}

	/**
	 * @param localIndex the localIndex to set
	 */
	public void setLocalIndex(int localIndex) {
		this.localIndex = localIndex;
	}

	/**
	 * @return the stayAlive
	 */
	public boolean StayAlive() {
		return stayAlive;
	}

	/**
	 * @param stayAlive the stayAlive to set
	 */
	public void setAlive(boolean stayAlive) {
		this.stayAlive = stayAlive;
	}

}
