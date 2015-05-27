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
import java.net.UnknownHostException;

import sharedPackages.ActionRequest;
import sharedPackages.LoginDeets;
import sharedPackages.Message;
import sharedPackages.ActionTypes;

/**
 * @author graham
 *
 */
public class ServerOutComms extends Thread{
	private static int port = 6969;
	private Socket socket;
	public static int loopDelay = 333;
	private static ObjectOutputStream csStream;
	private LoginDeets userDeets;
	private String ipAddress;
	
	public ServerOutComms(String ipAddress, LoginDeets userDeets){
		this.ipAddress = ipAddress;
		this.userDeets = userDeets;
	}
	
	private void sendMsg(Message message) throws IOException{
		csStream.writeObject(new ActionRequest(ActionTypes.SENDMESSAGE, message));
		csStream.flush();
	}
	
	public void run(){
		try {
		socket = new Socket(this.ipAddress, port);
		csStream = new ObjectOutputStream(socket.getOutputStream());
		ActionRequest connectRequest = new ActionRequest(ActionTypes.CONNECT, new Message(userDeets, null));
		csStream.writeObject(connectRequest);
		csStream.flush();
		ActionRequest indexRequest = new ActionRequest(ActionTypes.GETCURRENTMESSAGEINDEX);
		while(ClientMain.StayAlive()){
			Thread.sleep(loopDelay);
			csStream.writeObject(indexRequest);
			csStream.flush();
			if(ClientMain.isUpToDate()){
				if(!ClientMain.messageQueue.isEmpty()){
					sendMsg(ClientMain.messageQueue.deQueue());
				}
			} else{
				for(int i = ClientMain.getLocalIndex() + 1; i<ClientMain.getRemoteIndex();i++){
					ActionRequest getMsgRequest = new ActionRequest(ActionTypes.GETMESSAGE, i);
					csStream.writeObject(getMsgRequest);
					csStream.flush();
				}
			}
		}
		ActionRequest disconnectRequest = new ActionRequest(ActionTypes.DISCONNECT);
		csStream.writeObject(disconnectRequest);
		csStream.flush();
		Thread.sleep(400);
		csStream.close();
		} catch (IOException e){
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
