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
public class Room {
	public static int outPort = 6969; // this is the port that all of the server communications run through
	private static ServerSocket serverSocket; // The serversocket is the type of socket that accepts incomming
												// connections, and produces a standard socket of it's own.
	public static Vector<Connection> connections; // the list of connections that are connected to the server,each
													// spawns it's own thread
	@Deprecated
	private static Vector<String> users; // depricated userlist
	private static Vector<String> connectedUsers; // the new userlist
	public static Vector<Message> messages; // the list of all messages in the server history

	/**
		 * @author hugo
		 * Date of creation: May 26, 2015 
		 * @param: None
		 * @return: None
	 * @throws IOException 
		 * @Description: ( ͡° ͜ʖ ͡°)
		 */
	public static void main(String[] args) throws IOException {
		String selfIP = getSelfIP(); // this method returns the local IP address of the server
		System.out.println("Self IP: " + selfIP); // we print out the local IP address

		// TODO this is where we would load the database, if we implemented that.

		serverSocket = new ServerSocket(outPort); // creating the socket that accepts connections
		connections = new Vector<Connection>(); // initialize the array of connections
		messages = new Vector<Message>(); // init the array of messages
		users = new Vector<String>(); // depricated list of connected users
		connectedUsers = new Vector<String>(); // the current list of connected users

		new ThreadManager().start(); // creates and starts the thread that lists and counts the currently connected
										// users
		new ServerCommands().start(); // creates and starts the thread that accepts user input for server commands

		while (true) { // allows the server to accept new connections forever
			Socket scSocket = serverSocket.accept(); // serversocket.accept() is a blocking function, and waits until a
														// new connection appears to continue. it then makes a new
														// socket, which we store.
			connections.add(new Connection(scSocket)); // we add the connection to the connectionarray
			connections.lastElement().start(); // we start the thread that handles communication with the client.

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
		Socket ipTest = new Socket("192.168.1.1", 80); // this works on the principal that there is a webserver on the
														// IP address of the router. it starts a connection on the
														// webserver port. Note: this only works on networks where the
														// subnet is on this exact subnet. For example, this method will
														// not work on a network where the host's IP address is
														// 192.168.0.1, or if there is no webserver on the specified IP.
		String selfIP = ipTest.getLocalAddress().getHostAddress(); // then we get the IP address that we have
		ipTest.close(); // close the socket so that it doesn't clog the network, free mem etc.
		return selfIP; // return the found IP
	}

	/**
	 * 
		 * @author hugo
		 * Date of creation: May 26, 2015 
		 * @param: The message to add to the running database
		 * @return: None
		 * @Description: assigns the message a index, and adds it to the server's message list
	 */
	public static void addMessage(Message message) {
		if (!messages.isEmpty()) { // if it's not the first one, get the previous index, and assigne the new one to one
									// more than the previous one
			int prevInd = messages.lastElement().getIndex();
			message.setIndex(prevInd + 1);
		} else {
			message.setIndex(0); // otherwise it is the first one.
		}
		messages.insertElementAt(message, message.getIndex()); // insert it at the appropriate place.

	}

	/**
	 * 
		 * @author hugo
		 * Date of creation: May 26, 2015 
		 * @param: None
		 * @return: The highest index message that the server knows. 
		 * @Description: returns the index of the last message in the list
	 */
	public static int getCurrentMessageIndex() {
		return messages.lastElement().getIndex();
	}

	/**
	 * 
		 * @author hugo
		 * Date of creation: May 26, 2015 
		 * @param: The index of the wanted message
		 * @return: The message object at the wanted index
		 * @Description: returns the message object at the specified index
	 */
	public static Message getMessage(int index) {
		return messages.get(index);
	}

	/**
	* @Deprecated
	* 
	*/
	public static void addUser(String username) {
		users.addElement(username);
		System.out.println("Added: " + username + " to the array");
	}

	/**
	* @Deprecated
	* 
	*/
	public static void removeUser(String username) {
		System.out.println("Attempted to remove " + username + " from list. "
				+ (users.remove(username) ? "Successful" : "Failed:"));
	}

	/**
	* @Deprecated
	* 
	*/
	public static Vector<String> getUsers() {
		return users;
	}

	/**
	* @Deprecated
	* 
	*/
	public static Vector<String> getUsers2() {
		Vector<String> userList = new Vector<String>();
		for (int i = 0; i < connections.size(); i++) {
			userList.add(connections.elementAt(i).getUserDeets().getUserName());
		}
		return userList;
	}

	/**
	 * 
		 * @author hugo
		 * Date of creation: June 3, 2015 
		 * @param: None 
		 * @return: The array of usernames of connected users, as a vector array
		 * @Description: returns the array of usernames of connected users
	 */
	public static Vector<String> getUsers3() {
		return connectedUsers;
	}

	/**
	 * 
		 * @author hugo
		 * Date of creation: June 3, 2015 
		 * @param: A vector array of strings that has all the usernames of currently connected users.  
		 * @return: None
		 * @Description: Sets the global variable.
	 */
	public static void setUsers3(Vector<String> connectedUsers) {
		Room.connectedUsers = connectedUsers;
	}

}

/**
 * @author hugo
 *
 */
class ThreadManager extends Thread { // this class is started in a new thread and accumulates a list of connected users.
										// We made this it's own thread because we didn't want to wait to make a new one
										// on each message send, we just want it making in the background, then take the
										// freshest version.
	public ThreadManager() {

	}

	@Override
	public void run() { // this is started on the new thread
		while (true) { // run forever
			Vector<String> tempConnectedUsers = new Vector<String>(); // the temporary array of connected users, will be
																		// used privately.
			try {
				Thread.sleep(800); // don't work all the time, only update every second (approx)
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (int i = 0; i < Room.connections.size(); i++) { // for each connection to the server.
				if (!(Room.connections.get(i) == null)) { // edge case where the object exists, but has yet to be
																// fully initialized
					try {
						Thread.sleep(20); //less CPU usage
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
					if (!Room.connections.get(i).isAlive()) { // if the connection closes
						Room.connections.remove(i); // remove it from our list
						System.out.println("removed connection number: " + i);
						break; // to avoid index errors after removing an index
					} else {
						tempConnectedUsers.addElement(Room.connections.get(i).getUserDeets().getUserName());
					}
				}
			}
			Room.setUsers3(tempConnectedUsers); //update the global list of users with our updated list. 
		}
	}
}
