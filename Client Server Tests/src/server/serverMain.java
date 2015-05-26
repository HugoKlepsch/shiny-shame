/*
		 Title: serverMain.java
		 Programmer: hugo
		 Date of creation: Apr 14, 2015
		 Description: 
 */

package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Vector;

/**
 * @author hugo
 *
 */
public class serverMain {
	public static int outPort = 6969;
	
	public static Vector<ServerThread> threadArray;

	/**
		 * @author hugo
		 * Date of creation: Apr 10, 2015 
		 * @param: None
		 * @return: None
	 * @throws IOException 
	 * @throws UnknownHostException 
		 * @Description: ( ͡° ͜ʖ ͡°)
		 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket ipTest = new Socket("192.168.1.1", 80);
		System.out.println(ipTest.getLocalAddress().getHostAddress());
		ipTest.close();


		ServerSocket serverSocket = new ServerSocket(outPort);
		threadArray = new Vector<ServerThread>();
		int numConnections = 0;
		while (numConnections < 100) {
			Socket outSocket = serverSocket.accept();
			numConnections++;
			threadArray.add(new ServerThread(outSocket));
			threadArray.get(threadArray.size() - 1).start();
			//

		}
		serverSocket.close();
	}

}

class ServerThread extends Thread {
	private Socket outSocket;
	InetAddress address;
	private Socket inSocket; 
	public static int inPort = 6970;
	ObjectOutputStream outStream;

	public ServerThread(Socket sock) {
		this.outSocket = sock;
	}

	private void spam() throws IOException {
		

		

		outStream.flush();
		int counter = 0;
		while (counter < 1000000) {
			counter++;
			outStream.writeObject("Counter: " + counter);
			outStream.flush();
		}
		outStream.writeObject("End");
		outStream.flush();
		outSocket.close();
		System.out.println("Socket closed");

	}
	
	@SuppressWarnings("unused")
	private void ping() throws IOException{
		outStream = new ObjectOutputStream(outSocket.getOutputStream());
		
	}

	public void run() { // this runs when you hit execute
		try {
			outStream = new ObjectOutputStream(outSocket.getOutputStream());
			address = outSocket.getInetAddress();
			inSocket = new Socket(address, inPort);
			ObjectInputStream inputStream = new ObjectInputStream(inSocket.getInputStream());
			String message;
			message = (String) inputStream.readObject();
		} catch (IOException e) {
			// TODO: handle exception
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
