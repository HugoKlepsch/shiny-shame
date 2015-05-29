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
import java.util.Vector;

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
	public static int loopDelay = 100;
	private static ObjectOutputStream csStream;
	private LoginDeets userDeets;
	private String ipAddress;
	
	public ServerOutComms(String ipAddress, LoginDeets userDeets){
		this.ipAddress = ipAddress;
		this.userDeets = userDeets;
	}
	
	private void sendMsg(Message message) throws IOException{
		csStream.writeObject(new ActionRequest(ActionTypes.CSSENDMESSAGE, message));
		csStream.flush();
	}
	
	public void run(){
		try {
		socket = new Socket(this.ipAddress, port);
		csStream = new ObjectOutputStream(socket.getOutputStream());
		ServerInComms inComms = new ServerInComms(socket);
		inComms.start();
		ActionRequest connectRequest = new ActionRequest(ActionTypes.CSCONNECT, new Message(userDeets, null));
		csStream.writeObject(connectRequest);
		csStream.flush();
		ActionRequest indexRequest = new ActionRequest(ActionTypes.CSGETCURRENTMESSAGEINDEX);
		while(ClientMain.StayAlive()){
			Thread.sleep(loopDelay);
			csStream.writeObject(indexRequest);
			csStream.flush();
//			System.out.println("LocalIndex: " + ClientMain.getLocalIndex() + "RemoteIndex: " + ClientMain.getRemoteIndex());
//			Vector<Integer> missingIndices = ClientMain.getMissingIndices();
//			if(missingIndices.size() == 0){ //if we're up to date
//				if(!ClientMain.messageQueue.isEmpty()){
//					sendMsg(ClientMain.messageQueue.deQueue());
//				}
//			} else {
//				for(int i = 0; i < missingIndices.size(); i++){
//					ActionRequest getMsgRequest = new ActionRequest(ActionTypes.CSGETMESSAGE, missingIndices.get(i));
//					csStream.writeObject(getMsgRequest);
//					csStream.flush();
//				}
//			}
			for (int i = 0; i < ClientMain.getLocalIndexLength(); i++) {
				if (!ClientMain.hasMessage(i)) { //if the value stored = false, (we don't have it) 
					ActionRequest getMsgRequest = new ActionRequest(ActionTypes.CSGETMESSAGE, i);
					csStream.writeObject(getMsgRequest);
					csStream.flush();
				}
			}
			if(!ClientMain.messageQueue.isEmpty()){
				sendMsg(ClientMain.messageQueue.deQueue());
			}
			
		}
		ActionRequest disconnectRequest = new ActionRequest(ActionTypes.CSDISCONNECT);
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
