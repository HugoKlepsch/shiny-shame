/*
		 Title: connection.java
		 Programmer: hugo
		 Date of creation: May 26, 2015
		 Description: 
*/


package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author graham
 *
 */
public class Connection extends Thread {
	private Socket outSocket;
	InetAddress address;
	private Socket inSocket; 
	public static int inPort = 6970;
	static ObjectOutputStream outStream;
	static ObjectInputStream inStream;

	public Connection(Socket sock) {
		this.outSocket = sock;
	}

}
