/*
		 Title: mainThread.java
		 Programmer: hugo
		 Date of creation: May 26, 2015
		 Description: The class/thread that starts and manages the squad server
*/


package server;

import java.io.IOException;
import java.net.Socket;

/**
 * @author hugo
 *
 */
public class mainThread {
	public static int outPort = 6969;
	/**
		 * @author hugo
		 * Date of creation: May 26, 2015 
		 * @param: None
		 * @return: None
	 * @throws IOException 
		 * @Description: ( ͡° ͜ʖ ͡°)
		 */
	public static void main(String[] args) throws IOException {
		String selfIP = getSelfIP();
		System.out.println("Self IP: " + selfIP);

	}
	
	private static String getSelfIP() throws IOException{
		Socket ipTest = new Socket("192.168.1.1", 80);
		String selfIP = ipTest.getLocalAddress().getHostAddress();
		ipTest.close();
		return selfIP;
	}

}
