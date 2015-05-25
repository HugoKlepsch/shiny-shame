/*
		 Title: serverMain.java
		 Programmer: hugo
		 Date of creation: Apr 14, 2015
		 Description: 
 */

package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

/**
 * @author hugo
 *
 */
public class serverMain {
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

		int port = 6969;
		ServerSocket ssock = new ServerSocket(port);
		threadArray = new Vector<ServerThread>();

		while (true) {
			Socket sock = ssock.accept();
			threadArray.add(new ServerThread(sock));
			threadArray.get(threadArray.size() - 1).start();
			//

		}
	}

}

class ServerThread extends Thread {
	private Socket sock;
	ObjectOutputStream outStream;

	public ServerThread(Socket sock) {
		this.sock = sock;
	}

	public void run() { //this runs when you hit execute
		System.out.println(sock.toString());
		try {
			outStream = new ObjectOutputStream(sock.getOutputStream());

			outStream.flush();
			int counter = 0;
			while (counter < 1000000) {
				counter++;
				outStream.writeObject("Counter: " + counter);
				outStream.flush();
			}
			outStream.writeObject("End");
			outStream.flush();
			sock.close();
			System.out.println("Socket closed");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
