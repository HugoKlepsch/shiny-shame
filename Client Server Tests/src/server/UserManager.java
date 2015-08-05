/*
		 Title: UserManager.java
		 Programmer: hugo
		 Date of creation: Aug 5, 2015
		 Description: 
*/


package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author hugo
 *
 */
public class UserManager {

	public static int outPort = 6969; 
	private static ServerSocket serverSocket;
	public static int userCount = 0;
	public static final int MAXUSERS = 100;
	
	/**
		 * @author hugo
		 * Date of creation: Aug 5, 2015 
		 * @param: None
		 * @return: None
		 * @Description: ( ͡° ͜ʖ ͡°)
		 */
	public static void main(String[] args){
		String selfIP = getSelfIP(); // this method returns the local IP address of the server
		System.out.println("Self IP: " + selfIP); // we print out the local IP address

		// TODO this is where we would load the database, if we implemented that.

		try {
			serverSocket = new ServerSocket(outPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(userCount < MAXUSERS){
			try {
				Socket newConn = serverSocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	private static String getSelfIP() throws IOException {
		Socket ipTest = new Socket("192.168.1.1", 80); 
		String selfIP = ipTest.getLocalAddress().getHostAddress(); 
		ipTest.close();
		return selfIP; 
	}

}
