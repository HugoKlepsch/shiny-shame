/*
		 Title: connection.java
		 Programmer: graham
		 Date of creation: May 26, 2015
		 Description: 
 */

package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import sharedPackages.ActionRequest;
import sharedPackages.LoginDeets;
import sharedPackages.Message;

/**
 * @author graham
 * @description each connection thread manages communication with a user
 *
 */
public class Connection extends Thread {
	//intialize socket and stream object
	private Socket outSocket;
	private ObjectOutputStream scStream;
	private ObjectInputStream csStream;
	//defaults username to Connecting until initialized
	private LoginDeets userDeets = new LoginDeets("Connecting", null);
	//creates user info for root for sending connect and disconnect messages
	private static LoginDeets rootDeets = new LoginDeets("root", "dank");

	/**
	 * 
	 * @param sock - socket that the thread operates on
	 * @description - constructor accepts the socket to operate on
	 */
	public Connection(Socket sock) {
		this.outSocket = sock;
	}
	/**
	 * 
	 * @param message - message to send
	 * @throws IOException
	 * @description - sends a message
	 */
	private void sendMsg(Message message) throws IOException {
		//creates a new ActionRequest that contains the message
		ActionRequest serverResponse = new ActionRequest(ActionRequest.SCSENDMESSAGE, message);
		//server side message informing that a message is being send
		System.out.println("Sending message to: " + userDeets.getUserName() + " with message #"
			+ serverResponse.getMessage().getIndex());
		//writes the ActionRequest to the stream
		scStream.writeObject(serverResponse);
		//sends the ActionRequest
		scStream.flush();
		//sends the user info to the client
		sendUsers();
	}
	/**
	 * @throws IOException
	 * @description - sends list of users connected to the client
	 */
	private void sendUsers() throws IOException{
		//reads the list of usernames to a vector array
		Vector<String> userList = mainThread.getUsers3();
		//creates a new ActionRequest with the user list
		ActionRequest sendUserRequest = new ActionRequest(ActionRequest.SCSENDUSERS, userList);
		//writes the ActionRequest to the stream
		scStream.writeObject(sendUserRequest);
		//sends the ActionRequest
		scStream.flush();
		
	}
	/**
	 * 
	 * @throws IOException
	 * @description - sends a kick request to the client
	 */
	public void kick() throws IOException{
		//creates a new ActionRequest with the kick request
		ActionRequest kickRequest = new ActionRequest(ActionRequest.SCKICK);
		//writes the ActionRequest to the stream
		scStream.writeObject(kickRequest);
		//sends the ActionRequest
		scStream.flush();
	}
	/**
	 * @description - the main method of the thread called upon a .start() from the main thread
	 */
	public void run() {
		try {
			//Initializes the streams on the socket
			scStream = new ObjectOutputStream(outSocket.getOutputStream());
			csStream = new ObjectInputStream(outSocket.getInputStream());
			//initializes an ActionRequest
			ActionRequest actionRequest;
			//initializes a Message
			Message message;
			//initializes integers that hold index values
			int wantedIndex;
			int currIndex;
			//runs while the user has not sent a disconnect request
			do {
				//reads in the ActionRequest from the stream
				actionRequest = (ActionRequest) csStream.readObject();
				//if and else if statements check what type of action request it is
				if (actionRequest.getAction() == ActionRequest.CSGETCURRENTMESSAGEINDEX) {
					// gets the current message index
					currIndex = mainThread.getCurrentMessageIndex();
					//creates a new action request with the index
					ActionRequest sendIndexRequest = new ActionRequest(ActionRequest.SCSENDCURRENTMESSAGEINDEX, currIndex);
					//writes the action request to the stream
					scStream.writeObject(sendIndexRequest);
					//sends the action request
					scStream.flush();
					//sends the list of users
					sendUsers();

				} else if (actionRequest.getAction() == ActionRequest.CSGETMESSAGE) {
					System.out.println(userDeets.getUserName() + " wants message #" + actionRequest.getIndex());
					wantedIndex = actionRequest.getIndex();
					//gets the message at the index wanted
					message = mainThread.getMessage(wantedIndex);
					//sends that message
					sendMsg(message);
					
				} else if (actionRequest.getAction() == ActionRequest.CSSENDMESSAGE) {
					System.out.println(userDeets.getUserName() + "sent message" + actionRequest.getMessage().getMessage());
					message = actionRequest.getMessage();
					//adds the message received to the message vector
					mainThread.addMessage(message);
					//sends the list of users
					sendUsers();
				} else if (actionRequest.getAction() == ActionRequest.CSCONNECT) {
					//sets the credentials of the connection thread to the one received
					userDeets = actionRequest.getMessage().getCredentials();
					System.out.println(userDeets.getUserName() + " " + actionRequest.getAction());
					String msg = userDeets.getUserName() + " connected";
					//creates a new message as root that states the user is connecting
					Message connectMsg = new Message(rootDeets, msg);
					//adds the message to the message vector
					mainThread.addMessage(connectMsg);
					//sends the list of users
					sendUsers();
				}

			} while (actionRequest.getAction() != ActionRequest.CSDISCONNECT);
			String msg = userDeets.getUserName() + " disconnected";
			//creates a new message as root that states the user disconnected
			Message disConnectMsg = new Message(rootDeets, msg);
			//adds the message to the message vector
			mainThread.addMessage(disConnectMsg);
			//sends the user list
			sendUsers();
			System.out.println(msg);
			//close the streams and socket
			scStream.close();
			csStream.close();
			outSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @return - the credentials of the thread
	 * @description - allows other threads to check the user credentials
	 */
	public LoginDeets getUserDeets() {
		return userDeets;
	}
	/**
	 * 
	 * @param userDeets - the new userDeets
	 * @description - allows other threads to set the user credentials
	 */
	public void setUserDeets(LoginDeets userDeets) {
		this.userDeets = userDeets;
	}
	
	
	

	
	

}
