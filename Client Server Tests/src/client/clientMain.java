/*
		 Title: clientMain.java
		 Programmer: hugo
		 Date of creation: Apr 14, 2015
		 Description: 
 */

package client;

import java.net.*;
import java.io.*;
import java.io.ObjectInputStream.GetField;

import javax.naming.InitialContext;

/**
 * @author hugo
 *
 */
public class clientMain {
	private static int inPort = 6969;
	private static int outPort = 6970;
	/**
	 * @author hugo
	 * Date of creation: Apr 10, 2015 
	 * @param: None
	 * @return: None
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @Description: ( ͡° ͜ʖ ͡°)
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		initNetwork();
	}
	
	private static String getSelfIP() throws IOException{
		Socket ipTest = new Socket("192.168.1.1", 80);
		String selfIP = ipTest.getLocalAddress().getHostAddress();
		ipTest.close();
		return selfIP;
	}
	
	
	private static void initNetwork() throws IOException, ClassNotFoundException{
		String selfIP = getSelfIP();
		System.out.println("Self IP is: " + selfIP);
		System.out.println("Enter target IP address");
		String serverIP = (new BufferedReader(new InputStreamReader(System.in))).readLine();
		
		ServerSocket serverSocket = new ServerSocket(outPort);
		Socket inSocket = new Socket(serverIP, inPort);
		ObjectInputStream inputStream = new ObjectInputStream(inSocket.getInputStream());

		String message;
		do {
			message = (String) inputStream.readObject();
			System.out.println(message);
		} while (!message.equals("End"));

		inSocket.close();
		System.out.println("Socket closed");
	}

}
