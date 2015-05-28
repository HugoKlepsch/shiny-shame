/*
		 Title: ServerInComms.java
		 Programmer: hugo
		 Date of creation: May 26, 2015
		 Description: 
*/


package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import sharedPackages.ActionRequest;
import sharedPackages.ActionTypes;

/**
 * @author hugo
 *
 */
public class ServerInComms extends Thread{
	Socket scSocket;
	ObjectInputStream scStream;
	
	public ServerInComms(Socket socket){
		scSocket = socket;
	}
	
	public void run(){
		try {
			ActionRequest actionRequest;
			scStream = new ObjectInputStream(scSocket.getInputStream());
			while(ClientMain.StayAlive()){
				actionRequest = (ActionRequest) scStream.readObject();
				if(actionRequest.getAction() == ActionTypes.SCSENDCURRENTMESSAGEINDEX){
					System.out.println("RemoteIndex: " + actionRequest.getIndex());
					ClientMain.setRemoteIndex(actionRequest.getIndex());
				} else if (actionRequest.getAction() == ActionTypes.SCSENDMESSAGE) {
					ClientMain.addMessage(actionRequest.getMessage());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
