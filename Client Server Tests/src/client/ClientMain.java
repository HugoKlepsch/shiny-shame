/*
		 Title: ClientMain.java
		 Programmer: hugo
		 Date of creation: May 26, 2015
		 Description: The main entry point for the Squad messenger client
 */

package client;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import sharedPackages.LoginDeets;
import sharedPackages.Message;
import sharedPackages.Queuer;

/**
 * @author hugo
 *
 */
public class ClientMain {
	private static Vector<Boolean> localIndex; // We use an array of booleans in stead of a single int because we might
												// recieve a higher number message before a lower one, and in that case
												// we would not recieve the lower message ever. In this method, and
												// index we do not have we get.
	private static int remoteIndex = 0; // the highest index of the remote database.
	private static boolean stayAlive = true; // variable that dictates whether we should keep going or not.
	public static Queuer<Message> messageQueue; // the queue of messages that need to be sent. This uses my own queue
												// class
	private static LoginDeets creds; // the credentials of the user.
	private static Vector<String> users; // the list of users

	/**
	 * 
		 * @author hugo
		 * Date of creation: May 26, 2015 
		 * @param: None, but in the future we may add command line options to allow enering command line mode, where it does not start the GUI, and in stead uses the command line interface instead. 
		 * @return: None
	 * @throws IOException 
		 * @Description: ( ͡° ͜ʖ ͡°)
		 */
	public static void main(String[] args) throws IOException {
		messageQueue = new Queuer<Message>(); // init the variables
		localIndex = new Vector<Boolean>();
		users = new Vector<String>();
		login(); // starts the process of login into the server.
	}

	/**
	 * 
		 * @author hugo
		 * Date of creation: May 26, 2015 
		 * @param: None, 
		 * @return: None
	 * @throws IOException 
		 * @Description: In stead of starting the GUI, the program enters command line mode, printing and receiving directly from the command line. 
		 */
	public static void commandLineMode() throws IOException {
		BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter username to connect as: "); // getting the users credentials
		String userName = userIn.readLine();
		System.out.println("Enter the IP address to connect to: ");
		String address = userIn.readLine();
		creds = new LoginDeets(userName, null); // making the credentials object

		ServerOutComms outComms = new ServerOutComms(address, creds); // the output object, handles all communication
																		// out to the server.
		outComms.start(); // starting the object in a new thread

		System.out.println("Type \"Exit\" to close");
		String messageContent = "";
		Message message;
		while (!(messageContent.equals("Exit"))) { // while the user keeps wanting to chat
			messageContent = userIn.readLine(); // read messages
			message = new Message(creds, messageContent); // make the message object
			if (!(messageContent.equals("Exit"))) {
				messageQueue.enQueue(message); // add the message to the output Queue
			} else {
				setAlive(false); // if the user wants to exit, set the variable the allows gracefull closing of app.
			}
		}
	}

	/**
	 * 
		 * @author hugo
		 * Date of creation: May 26, 2015 
		 * @param: The message object that the server incomms object recieved.  
		 * @return: None
	 * @throws IOException 
		 * @Description: Used in the GUI version, simple adds the given message to the GUI in a meaningful way. 
		 */
	public static void addMessage(Message message) {
		ClientGUI.addMessage(message);
	}

	/**
	 * 
		 * @author hugo
		 * Date of creation: May 26, 2015 
		 * @param: The index you wish to check
		 * @return: boolean value, true if we have it, else false. 
	 * @throws IOException 
		 * @Description: returns the value from the local list.  
		 */
	public static boolean hasMessage(int index) {
		return ClientMain.localIndex.get(index);
	}

	/**
	 * @return the length of the local message array
	 */
	public static int getLocalIndexLength() {
		return ClientMain.localIndex.size();
	}

	/**
	 * @descrip adds an index to the localIndex array, with status false
	 */
	public static void localIndexAddIndex() {
		localIndex.addElement(false); //on update of the servers high index, we assume that we do not have the message. 
	}

	/**
	 * @param set the local list to a value. 
	 */
	public static void setLocalIndex(int index, boolean value) {
		ClientMain.localIndex.set(index, value);
	}

	/**
	 * @return the remote index
	 */
	public static int getRemoteIndex() {
		return remoteIndex;
	}

	/**
	 * @param update the remote index value
	 */
	public static void setRemoteIndex(int remoteIndex) {
		ClientMain.remoteIndex = remoteIndex;
	}

	/**
	 * 
		 * @author hugo
		 * Date of creation: May 26, 2015 
		 * @param: None
		 * @return: a list of indexes that we do not have. 
	 * @throws IOException 
		 * @Description: goes through each message, and if we don't have, it add it to the list of messages we need. 
		 */
	public static Vector<Integer> getMissingIndices() { //
		Vector<Integer> missingIndices = new Vector<Integer>();
		for (int i = 0; i < localIndex.size(); i++) {
			if (!localIndex.get(i)) {
				missingIndices.add(i);
			}
		}
		return missingIndices;
	}

	/**
	 * 
		 * @author hugo
		 * Date of creation: May 26, 2015 
		 * @param: The IP address to connect to  
		 * @return: None
	 * @throws IOException 
		 * @Description: Used in the GUI version, starts the GUI in it's own thread.  
		 */
	public static void startGUI(final String ipAddress) {
		EventQueue.invokeLater(new Runnable() {
			public void run() { //makes a new thread and runs the given code. 
				try {
					new ClientGUI(ipAddress);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 
		 * @author hugo
		 * Date of creation: May 26, 2015 
		 * @param: None
		 * @return: None
	 * @throws IOException 
		 * @Description: Used in the GUI version, used to login.  opens the login window. 
		 */
	private static void login() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new LoginGUI();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * @return  stayAlive value 
	 */
	public static boolean StayAlive() {
		return stayAlive;
	}

	/**
	 * @param the value to set stayalive to  
	 */
	public static void setAlive(boolean stayAlive) {
		ClientMain.stayAlive = stayAlive;
	}

	/**
	 * 
		 * @author hugo
		 * Date of creation: May 26, 2015 
		 * @param: none
		 * @return: returns the credentials of the user. 
	 * @throws IOException 
		 * @Description: returns the credentials of the user. 
		 */
	public static LoginDeets getCreds() {
		return creds;
	}
	
	/**
	 * @param set the credentials used by the client. 
	 */
	public static void setCreds(LoginDeets creds) {
		ClientMain.creds = creds;
	}

	/**
	 * @return the connected users 
	 */
	public static Vector<String> getUsers() {
		return users;
	}

	/**
	 * @param set list of users that are connected. 
	 */
	public static void setUsers(Vector<String> users) {
		ClientMain.users = users;
		System.out.println("Updated user list");
	}

}
