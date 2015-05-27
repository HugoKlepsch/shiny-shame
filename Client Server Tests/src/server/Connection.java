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

import sharedPackages.ActionRequest;
import sharedPackages.LoginDeets;
import sharedPackages.Message;
import sharedPackages.ActionTypes;

/**
 * @author graham
 *
 */
public class Connection extends Thread {
	private Socket outSocket;
	private ObjectOutputStream scStream;
	private ObjectInputStream csStream;
	private LoginDeets userDeets;
	private static LoginDeets rootDeets = new LoginDeets("root", "dank");
	
	public Connection(Socket sock) {
		this.outSocket = sock;
	}
	
	
	private void sendMsg(Message message) throws IOException{
		ActionRequest sendMsgRequest = new ActionRequest(ActionTypes.SCSENDMESSAGE, message);
		scStream.writeObject(sendMsgRequest);
		scStream.flush();
	}
	
	public void run(){
		try {
			scStream = new ObjectOutputStream(outSocket.getOutputStream());
			csStream = new ObjectInputStream(outSocket.getInputStream());
			ActionRequest actionRequest;
			Message message;
			int wantedIndex;
			int currIndex;
			do {
				actionRequest = (ActionRequest) csStream.readObject();
				if(actionRequest.getAction() == ActionTypes.CSGETCURRENTMESSAGEINDEX){
					currIndex = mainThread.getCurrentMessageIndex();
					ActionRequest sendIndexRequest = new ActionRequest(ActionTypes.SCSENDCURRENTMESSAGEINDEX, currIndex);
					scStream.writeObject(sendIndexRequest);
					scStream.flush();
					
				} else if(actionRequest.getAction() == ActionTypes.CSGETMESSAGE){
					System.out.println(userDeets.getUserName() + " " + actionRequest.getAction());
					wantedIndex = actionRequest.getIndex();
					message = mainThread.getMessage(wantedIndex);
					sendMsg(message);
				} else if(actionRequest.getAction() == ActionTypes.CSSENDMESSAGE){
					System.out.println(userDeets.getUserName() + " " + actionRequest.getAction());
					message = actionRequest.getMessage();
					mainThread.addMessage(message);
				} else if(actionRequest.getAction() == ActionTypes.CSCONNECT){
					System.out.println(userDeets.getUserName() + " " + actionRequest.getAction());
					 userDeets = actionRequest.getMessage().getCredentials();
					 String msg = userDeets.getUserName() + " connected";
					 Message connectMsg = new Message(rootDeets, msg);
					 mainThread.addMessage(connectMsg);
				}
				
			} while (actionRequest.getAction() != ActionTypes.CSDISCONNECT);
			String msg = userDeets.getUserName() + " disconnected";
			Message disConnectMsg = new Message(rootDeets, msg);
			mainThread.addMessage(disConnectMsg);
			System.out.println(msg);
			scStream.close(); 
			outSocket.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} 
	}

}
