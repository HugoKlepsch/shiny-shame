/*
		 Title: RoomManager.java
		 Programmer: hugo
		 Date of creation: Jun 15, 2015
		 Description: 
*/


package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * @author hugo
 *
 */
public class RoomManager {
	public static int outPort = 6969; 
	private static ServerSocket serverSocket;
	
	private static Vector<Room> rooms;
	
	public static void main(String[] args) throws IOException {
		String selfIP = getSelfIP(); // this method returns the local IP address of the server
		System.out.println("Self IP: " + selfIP); // we print out the local IP address

		// TODO this is where we would load the database, if we implemented that.

		serverSocket = new ServerSocket(outPort);
		
	}
	
	private static String getSelfIP() throws IOException {
		Socket ipTest = new Socket("192.168.1.1", 80); 
		String selfIP = ipTest.getLocalAddress().getHostAddress(); 
		ipTest.close();
		return selfIP; 
	}

}
