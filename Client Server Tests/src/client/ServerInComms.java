/*
		 Title: ServerInComms.java
		 Date of creation: May 26, 2015
*/


package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import sharedPackages.ActionRequest;

/**
 * @author graham
 * @description - this class handles receiving from the server
 *
 */
public class ServerInComms extends Thread{
	//creates the socket and stream object
	Socket scSocket;
	ObjectInputStream scStream;
	/**
	 * @description - constructor initializes on the socket
	 * @param socket - socket to use
	 */
	public ServerInComms(Socket socket){
		scSocket = socket;
	}
	/**
	 * @description - the main method of the thread, called by .start()
	 */
	public void run(){
		try {
			//set up action request object
			ActionRequest actionRequest;
			//initializes input stream
			scStream = new ObjectInputStream(scSocket.getInputStream());
			//continues to run while being told to stay alive
			while(ClientMain.StayAlive()){
				//read in the action request from the stream
				actionRequest = (ActionRequest) scStream.readObject();
				//if and else statements handle what type of action to do
				if(actionRequest.getAction() == ActionRequest.SCSENDCURRENTMESSAGEINDEX){
					//set the variable that holds the remote index locally
					ClientMain.setRemoteIndex(actionRequest.getIndex());
					//calculate the difference between server version and client version
					int difference = ClientMain.getRemoteIndex() + 1 - ClientMain.getLocalIndexLength();
					//if there is a difference
					if(difference != 0){
						//add references that lets outcomms know to ask for the latest messages
						for (int i = 0; i < difference; i++) {
							ClientMain.localIndexAddIndex();
						}
					}
				} else if (actionRequest.getAction() == ActionRequest.SCSENDMESSAGE) {
					if(ClientMain.hasMessage(actionRequest.getMessage().getIndex())){ //if we already have the message, 
						//do nothing
					} else { //if we do not
						//add that we now have it
						ClientMain.setLocalIndex(actionRequest.getMessage().getIndex(), true);
						//add the message on screen
						ClientMain.addMessage(actionRequest.getMessage());
					}
					
					
				} else if(actionRequest.getAction() == ActionRequest.SCSENDUSERS){
					//update the users on screen
					ClientGUI.updateUsers(actionRequest.getUsers());
					
				} else if(actionRequest.getAction() == ActionRequest.SCKICK){
					//kill the in and out comms threads
					ClientMain.setAlive(false);
					//close the GUI
					ClientGUI.exit();
				}
			} 
		//catch errors
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
