/*
		 Title: connection.java
		 Programmer: hugo
		 Date of creation: May 26, 2015
		 Description: 
*/


package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import sharedPackages.LoginDeets;

/**
 * @author graham
 *
 */
public class Connection extends Thread {
	private Socket outSocket;
	InetAddress address;
	private Socket inSocket; 
	public static int inPort = 6970;
	static ObjectOutputStream scStream;
	static ObjectInputStream csStream;
	private LoginDeets deets;

	public Connection(Socket sock) {
		this.outSocket = sock;
	}
	
	private void getUserName() throws IOException, ClassNotFoundException{
		String username;
		scStream.flush();
		scStream.writeObject("Please enter your username");
		scStream.flush();
		username = (String) csStream.readObject();
		this.deets = new LoginDeets(username, null);
	}
	
	public void run(){
		try {
			scStream = new ObjectOutputStream(outSocket.getOutputStream());
			address = outSocket.getInetAddress();
			inSocket = new Socket(address, inPort);
			csStream = new ObjectInputStream(inSocket.getInputStream());
			getUserName();
			String message;
			do {
				message = (String) csStream.readObject();
				if(message.equalsIgnoreCase("1")){
					//call getCurrentMsgNum()
					//outStream.writeObject(currMsgNum);
					//outStream.flush();
				} else{
					
				}
			} while (message.equalsIgnoreCase("Quit"));
			
			
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
