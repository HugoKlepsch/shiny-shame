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

import server.serverMain;

/**
 * @author hugo
 *
 */
public class clientMain {
	private static int inPort = 6969;
	private static int outPort = 6970;
	static ServerSocket serverSocket;
	static Socket outSocket;
	static ObjectOutputStream outStream;
	static Socket inSocket;
	static ObjectInputStream inStream;
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
	
	private static double ping() throws IOException{
		outStream.writeObject("ping");
		int numTimes = 4;
		long[] initTime = new long[numTimes];
		long[] endTime = new long[numTimes];
		for (int i = 0; i < 4; i++) {
			initTime[i] = System.currentTimeMillis();
			System.out.println("Client@outind");
			outStream.writeInt(i);
			outStream.flush();
		}
		for (int i = 0; i < 4; i++) {
			int recievedInd = inStream.readInt();
			endTime[recievedInd] = System.currentTimeMillis();
			System.out.println("Client@recieveind: " + recievedInd);
		}
		
		
		return 1.0;
	}
	
	private static void initNetwork() throws IOException, ClassNotFoundException{
		String selfIP = getSelfIP();
		System.out.println("Self IP is: " + selfIP);
		System.out.println("Enter target IP address");
		String serverIP = (new BufferedReader(new InputStreamReader(System.in))).readLine();
		
		serverSocket = new ServerSocket(outPort);
		inSocket = new Socket(serverIP, inPort);
		inStream = new ObjectInputStream(inSocket.getInputStream());
		outSocket = serverSocket.accept();
		outStream = new ObjectOutputStream(outSocket.getOutputStream());
		ping();
		
		String message;
		do {
			message = (String) inStream.readObject();
			System.out.println(message);
		} while (!message.equals("End"));

		inSocket.close();
		System.out.println("Socket closed");
	}

}
