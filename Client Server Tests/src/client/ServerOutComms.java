/*
		 Title: ServerComms.java
		 Programmer: graham
		 Date of creation: May 26, 2015
		 Description: 
*/


package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import sharedPackages.ActionRequest;
import sharedPackages.LoginDeets;
import sharedPackages.Message;

/**
 * @author graham
 * @description - thread that handles sending to the server
 *
 */
public class ServerOutComms extends Thread{
	//default port
	private static int port = 6969;
	//creates socket object
	private Socket socket;
	//sets standard loop delay
	public static final int loopDelay = 100;
	//create output stream object
	private static ObjectOutputStream csStream;
	//create credentials object
	private LoginDeets userDeets;
	//create string that holds the ip address
	private String ipAddress;
	/**
	 * @description - constructor gets data for starting the connection to the server
	 * @param ipAddress - ip address to connect to
	 * @param userDeets - credentials to use
	 */
	public ServerOutComms(String ipAddress, LoginDeets userDeets){
		this.ipAddress = ipAddress;
		this.userDeets = userDeets;
	}
	/**
	 * @description - sends a message to the server
	 * @param message - message to send to the server
	 * @throws IOException
	 */
	private void sendMsg(Message message) throws IOException{
		csStream.writeObject(new ActionRequest(ActionRequest.CSSENDMESSAGE, message));
		csStream.flush();
	}
	/**
	 * @description - the main method of the thread, called by .start()
	 */
	public void run(){
		//try catch catches all errors
		try {
		//creates the socket on the port and ip
		socket = new Socket(this.ipAddress, port);
		//opens the output stream
		csStream = new ObjectOutputStream(socket.getOutputStream());
		//create and start the in comms thread
		ServerInComms inComms = new ServerInComms(socket);
		inComms.start();
		//create and send a connect request
		ActionRequest connectRequest = new ActionRequest(ActionRequest.CSCONNECT, new Message(userDeets, null));
		csStream.writeObject(connectRequest);
		csStream.flush();
		//create a asking for current index request for later use
		ActionRequest indexRequest = new ActionRequest(ActionRequest.CSGETCURRENTMESSAGEINDEX);
		while(ClientMain.StayAlive()){
			//delay the thread for less network usage
			Thread.sleep(loopDelay);
			//send the current index request
			csStream.writeObject(indexRequest);
			csStream.flush();
			//check every message index to see if we have it
			for (int i = 0; i < ClientMain.getLocalIndexLength(); i++) {
				if (!ClientMain.hasMessage(i)) { //if the value stored = false, (we don't have it) 
					//create a request for the message we're missing and then send it
					ActionRequest getMsgRequest = new ActionRequest(ActionRequest.CSGETMESSAGE, i);
					csStream.writeObject(getMsgRequest);
					csStream.flush();
				}
			}
			//if we have messages to send
			if(!ClientMain.messageQueue.isEmpty()){
				//send the first message from the queue
				sendMsg(ClientMain.messageQueue.deQueue());
			}
			
		}
		//tells the user we disconnected
		System.out.println("Exited outstream loop to send disconnect");
		//creates and sends a disconnect request
		ActionRequest disconnectRequest = new ActionRequest(ActionRequest.CSDISCONNECT);
		csStream.writeObject(disconnectRequest);
		csStream.flush();
		//delays until the request has for sure been sent
		Thread.sleep(1000);
		//close the socket
		socket.close();
		//catches all errors
		} catch (IOException e){
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
