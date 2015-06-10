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
	public static Vector<Connection> connections;
	@Deprecated
	private static Vector<String> users;
	private static Vector<String> connectedUsers;
	public static Vector<Message> messages;

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
		users = new Vector<String>();
		connectedUsers = new Vector<String>();
		
		
		new ThreadManager().start();
		new ServerCommands().start();
		
		while (true) {
			Socket scSocket = serverSocket.accept();
			connections.add(new Connection(scSocket));
			connections.lastElement().start();
			
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
	@Deprecated
	public static void addUser(String username){
		users.addElement(username);
		System.out.println("Added: " + username + " to the array");
	}
	@Deprecated
	public static void removeUser(String username){
		System.out.println("Attempted to remove " + username + " from list. " + (users.remove(username) ? "Successful": "Failed:"));
	}
	@Deprecated
	public static Vector<String> getUsers(){
		return users;
	}
	@Deprecated
	public static Vector<String> getUsers2(){
		Vector<String> userList = new Vector<String>();
		for(int i = 0; i<connections.size();i++){
			userList.add(connections.elementAt(i).getUserDeets().getUserName());
		}
		return userList;
	}
	
	public static Vector<String> getUsers3(){
		return connectedUsers;
	}
	
	public static void setUsers3(Vector<String> connectedUsers){
		mainThread.connectedUsers = connectedUsers;
	}


	

}

class ThreadManager extends Thread{
	public ThreadManager(){
		
	}
	@Override
	public void run(){
		while (true) {
			Vector<String> tempConnectedUsers = new Vector<String>();
			try {
				Thread.sleep(800);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (int i = 0; i < mainThread.connections.size(); i++) {
				if (!(mainThread.connections.get(i) == null)) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NullPointerException e){
						e.printStackTrace();
					}
					if (!mainThread.connections.get(i).isAlive()) { // if the connection closes
						mainThread.connections.remove(i); // remove it from our list
						System.out.println("removed connection number: " + i);
						break; // to avoid index errors after removing an index
					} else {
						tempConnectedUsers.addElement(mainThread.connections.get(i).getUserDeets().getUserName());
					}
				}
			}
			mainThread.setUsers3(tempConnectedUsers);
		}
	}
}






















