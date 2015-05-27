/*
		 Title: mainThread.java
		 Programmer: hugo
		 Date of creation: May 26, 2015
		 Description: The class/thread that starts and manages the squad server
 */

package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import sharedPackages.Message;

/**
 * @author hugo
 *
 */
public class mainThread {
	public static int outPort = 6969;
	private static ServerSocket serverSocket;
	private static Vector<Connection> connections;

	private static Vector<Message> messages;

	/**
		 * @author hugo
		 * Date of creation: May 26, 2015 
		 * @param: None
		 * @return: None
	 * @throws IOException 
		 * @Description: ( ͡° ͜ʖ ͡°)
		 */
	public static void main(String[] args) throws IOException {
		String selfIP = getSelfIP();
		System.out.println("Self IP: " + selfIP);

		// TODO this is where we would load the database, if we implemented that.

		serverSocket = new ServerSocket(outPort);
		connections = new Vector<Connection>();
		messages = new Vector<Message>();
		while (true) {
			Socket scSocket = serverSocket.accept();
			connections.add(new Connection(scSocket));
			connections.lastElement().start();
			for (int i = 0; i < connections.size(); i++) {
				if (!connections.get(i).isAlive()) { // if the connection closes
					connections.remove(i); // remove it from our list
					break; // to avoid index errors after removing an index
				}
			}
		}
	}

	/**
	 * 
		 * @author hugo
		 * Date of creation: May 26, 2015 
		 * @param: None
		 * @return: The IP address of the device
		 * @Description: will not work on networks who's default gateway is not 192.168.1.1
	 */
	private static String getSelfIP() throws IOException {
		Socket ipTest = new Socket("192.168.1.1", 80);
		String selfIP = ipTest.getLocalAddress().getHostAddress();
		ipTest.close();
		return selfIP;
	}

	/**
	 * 
		 * @author hugo
		 * Date of creation: May 26, 2015 
		 * @param: The message to add to the running database
		 * @return: None
		 * @Description: ( ͡° ͜ʖ ͡°)
	 */
	public static void addMessage(Message message) {
		if (!messages.isEmpty()) {
			int prevInd = messages.lastElement().getIndex();
			message.setIndex(prevInd + 1);
		} else{
			message.setIndex(0);
		}
		messages.insertElementAt(message, message.getIndex());
	}

	public static int getCurrentMessageIndex() {
		return messages.lastElement().getIndex();
	}

	public static Message getMessage(int index) {
		return messages.get(index);
	}

}
