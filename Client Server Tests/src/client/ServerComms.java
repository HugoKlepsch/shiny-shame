/*
		 Title: ServerComms.java
		 Programmer: graham
		 Date of creation: May 26, 2015
		 Description: 
*/


package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import sharedPackages.LoginDeets;

/**
 * @author graham
 *
 */
public class ServerComms {
	private static int port = 6969;
	private Socket socket;
	private static ObjectInputStream scStream;
	private static ObjectOutputStream csStream;
	private LoginDeets userDeets;
	private static final int GETCURRENTMESSAGEINDEX = 1;
	private static final int GETMESSAGE = 2;
	private static final int SENDMESSAGE = 3;
	private static final int DISCONNECT = 4;
	private static final int CONNECT = 5;
	private String ipAddress;
	private int localIndex;
	
	public ServerComms(String ipAddress, LoginDeets userDeets){
		this.ipAddress = ipAddress;
		this.userDeets = userDeets;
	}
	
	public void run() throws UnknownHostException, IOException{
		socket = new Socket(this.ipAddress, port);
		scStream = new ObjectInputStream(socket.getInputStream());
		csStream = new ObjectOutputStream(socket.getOutputStream());
		
	}

}
