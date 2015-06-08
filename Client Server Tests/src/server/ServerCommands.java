package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ServerCommands extends Thread {
	private BufferedReader commandIn;
	private String command = "";
	
	public ServerCommands(){
		commandIn = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void run(){
		while(!(command.equals("/close"))){
			try {
				command = commandIn.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	}

}
