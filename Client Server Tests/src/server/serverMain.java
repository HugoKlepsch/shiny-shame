/*
		 Title: serverMain.java
		 Programmer: hugo
		 Date of creation: Apr 14, 2015
		 Description: 
 */

package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Vector;

/**
 * @author hugo
 *
 */
public class serverMain {
	public static int outPort = 6969;
	
	public static Vector<ServerThread> connectionArray;

	/**
		 * @author hugo
		 * Date of creation: Apr 10, 2015 
		 * @param: None
		 * @return: None
	 * @throws IOException 
	 * @throws UnknownHostException 
		 * @Description: ( ͡° ͜ʖ ͡°)
		 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		String selfIP = getSelfIP();
		System.out.println("Self IP: " + selfIP);
		
		ServerSocket serverSocket = new ServerSocket(outPort);
		connectionArray = new Vector<ServerThread>();
		int numConnections = 0;
		while (numConnections < 100) {
			Socket outSocket = serverSocket.accept();
			numConnections++;
			connectionArray.add(new ServerThread(outSocket));
			connectionArray.get(connectionArray.size() - 1).start();
			for (int i = 0; i < connectionArray.size(); i++) {
				if(!connectionArray.get(i).isAlive()){
					connectionArray.remove(i);
					break;
				}
			}
		}
		serverSocket.close();
	}
	
	private static String getSelfIP() throws IOException{
		Socket ipTest = new Socket("192.168.1.1", 80);
		String selfIP = ipTest.getLocalAddress().getHostAddress();
		ipTest.close();
		return selfIP;
	}

}

class ServerThread extends Thread {
	private Socket outSocket;
	InetAddress address;
	private Socket inSocket; 
	public static int inPort = 6970;
	static ObjectOutputStream outStream;
	static ObjectInputStream inStream;

	public ServerThread(Socket sock) {
		this.outSocket = sock;
	}

	private void spam() throws IOException {
		outStream.flush();
		int counter = 0;
		while (counter < 1000000) {
			counter++;
			outStream.writeObject("Counter: " + counter);
			outStream.flush();
		}
		outStream.writeObject("End");
		outStream.flush();
		outSocket.close();
		System.out.println("Socket closed");

	}
	
	private void ping() throws IOException, NumberFormatException, ClassNotFoundException, InterruptedException{
		System.out.println("server@ping()");
		long startTime = System.currentTimeMillis();
		long timeOut = 2000;
		int recieved = 0;
		int message;
		do{
			message = inStream.readInt();
			System.out.println("Server@ping@message: " + message);
			Thread.sleep(1337);
			recieved = recieved + ((message >= 0 && message < 4) ? 1 : 0);
			outStream.writeInt(message);
			outStream.flush();
		} while(recieved < 3 && (System.currentTimeMillis() - startTime) < timeOut);
	}

	public void run() { // this runs when you hit execute
		try {
			outStream = new ObjectOutputStream(outSocket.getOutputStream());
			address = outSocket.getInetAddress();
			inSocket = new Socket(address, inPort);
			inStream = new ObjectInputStream(inSocket.getInputStream());
			String message;
			message = (String) inStream.readObject();
			if (message.equalsIgnoreCase("ping")) {
				ping();
				spam();
			}
		} catch (IOException e) {
			// TODO: handle exception
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
