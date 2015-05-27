/*
		 Title: ClientMain.java
		 Programmer: hugo
		 Date of creation: May 26, 2015
		 Description: A quick and dirty computer client for the squad messenger beta
*/


package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import sharedPackages.LoginDeets;
import sharedPackages.Message;
import sharedPackages.Queuer;

/**
 * @author hugo
 *
 */
public class ClientMain {
	private static BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
	private static int localIndex = 0;
	private static int remoteIndex = 0;
	private static boolean stayAlive = true;
	public static Queuer<Message> messageQueue;
	
	/**
		 * @author hugo
		 * Date of creation: May 26, 2015 
		 * @param: None
		 * @return: None
	 * @throws IOException 
		 * @Description: ( ͡° ͜ʖ ͡°)
		 */
	public static void main(String[] args) throws IOException {
		messageQueue = new Queuer<Message>();
		System.out.println("Enter username to connect as: ");
		String userName = userIn.readLine();
		System.out.println("Enter the IP address to connect to: ");
		String address = userIn.readLine();
		LoginDeets creds = new LoginDeets(userName, null);
		
		ServerOutComms outComms = new ServerOutComms(address, creds);
		outComms.start();
		
		System.out.println("Type \"Exit\" to close");
		String messageContent =  "";
		Message message;
		while(!(messageContent.equals("Exit"))){
			messageContent = userIn.readLine();
			message = new Message(creds, messageContent);
			if (!(messageContent.equals("Exit"))) {
				messageQueue.enQueue(message); 
			} else {
				setAlive(false);
			}
		}
	
	}

	public static void addMessage(Message message){
		System.out.println(message.getCredentials().getUserName() + ": " + message.getMessage());
	}
	
	/**
	 * @return the localIndex
	 */
	public static int getLocalIndex() {
		return localIndex;
	}

	/**
	 * @param localIndex the localIndex to set
	 */
	public static void setLocalIndex(int localIndex) {
		ClientMain.localIndex = localIndex;
	}
	
	/**
	 * @return the localIndex
	 */
	public static int getRemoteIndex() {
		return remoteIndex;
	}

	/**
	 * @param localIndex the localIndex to set
	 */
	public static void setRemoteIndex(int remoteIndex) {
		ClientMain.remoteIndex = remoteIndex;
		System.out.println("Updated remoteindex to : " + ClientMain.remoteIndex);
	}
	
	public static boolean isUpToDate(){
		return (localIndex == remoteIndex ? true : false);
	}

	/**
	 * @return the stayAlive
	 */
	public static boolean StayAlive() {
		return stayAlive;
	}

	/**
	 * @param stayAlive the stayAlive to set
	 */
	public static void setAlive(boolean stayAlive) {
		ClientMain.stayAlive = stayAlive;
	}

}
