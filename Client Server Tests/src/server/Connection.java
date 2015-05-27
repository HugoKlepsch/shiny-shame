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
import java.net.InetAddress;
import java.net.Socket;

import sharedPackages.ActionRequest;
import sharedPackages.LoginDeets;
import sharedPackages.Message;

/**
 * @author graham
 *
 */
public class Connection extends Thread {
	private Socket outSocket;
	private InetAddress address;
	private Socket inSocket; 
	public static int inPort = 6970;
	private ObjectOutputStream scStream;
	private ObjectInputStream csStream;
	private LoginDeets userDeets;
	private static LoginDeets rootDeets = new LoginDeets("root", "dank");

	public Connection(Socket sock, LoginDeets userDeets) {
		this.outSocket = sock;
		this.userDeets = userDeets;
	}
	
	
	private void sendMsg(Message message) throws IOException{
		scStream.writeObject(message);
		scStream.flush();
	}
	
	public void run(){
		try {
			scStream = new ObjectOutputStream(outSocket.getOutputStream());
			address = outSocket.getInetAddress();
			inSocket = new Socket(address, inPort);
			csStream = new ObjectInputStream(inSocket.getInputStream());
			ActionRequest actionRequest;
			Message message;
			int wantedIndex;
			int currIndex;
			do {
				actionRequest = (ActionRequest) csStream.readObject();
				if(actionRequest.getAction() == 1){
					currIndex = mainThread.getCurrentMessageIndex();
					scStream.writeObject(currIndex);
					scStream.flush();
				} else if(actionRequest.getAction() == 2){
					wantedIndex = actionRequest.getIndex();
					message = mainThread.getMessage(wantedIndex);
					sendMsg(message);
				} else if(actionRequest.getAction() == 3){
					message = actionRequest.getMessage();
					mainThread.addMessage(message);
				}
			} while (actionRequest.getAction() != 4);
			String msg = userDeets.getUserName() + "disconnected";
			Message disConnectMsg = new Message(rootDeets, msg);
			mainThread.addMessage(disConnectMsg);
			scStream.close(); 
			
			
		} catch (IOException e) {
			// TODO: handle exception
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
