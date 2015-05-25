/*
		 Title: clientMain.java
		 Programmer: hugo
		 Date of creation: Apr 14, 2015
		 Description: 
 */


package client;

import java.net.*;
import java.io.*;

import javax.xml.ws.handler.MessageContext;

/**
 * @author hugo
 *
 */
public class clientMain {

	/**
	 * @author hugo
	 * Date of creation: Apr 10, 2015 
	 * @param: None
	 * @return: None
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @Description: ( ͡° ͜ʖ ͡°)
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Socket ipTest = new Socket("192.168.1.1", 80);
		System.out.println(ipTest.getLocalAddress().getHostAddress());
		ipTest.close();

		String serverIP = (new BufferedReader(new InputStreamReader(System.in))).readLine();
		int port = 6969;
		Socket sock = new Socket(serverIP, port);
		ObjectInputStream is = new ObjectInputStream(sock.getInputStream());


		String message;
		do {
			message = (String) is.readObject();
			System.out.println(message);
		} while (!message.equals("End"));

		sock.close();
		System.out.println("Socket closed");

	}

}
