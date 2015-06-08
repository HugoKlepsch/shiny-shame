package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import sharedPackages.LoginDeets;
import sharedPackages.Message;

/*
 * commands to have:
 * 
 */


public class ServerCommands extends Thread {
	private BufferedReader commandIn;
	private LoginDeets root;
	
	public ServerCommands(){
		commandIn = new BufferedReader(new InputStreamReader(System.in));
		root = new LoginDeets("root", "dank");
	}
	
	public void run(){
		String input = "";
		while(!(input.equals("close"))){
			try {
				input = commandIn.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String[] temp = input.split(" ");
			String command = temp[0];
			String[] args = null;
			if(temp.length > 1){
				args = Arrays.copyOfRange(temp, 1, temp.length);
			}
			if(command.equals("message")){
				String messageContent = String.join(" ", args);
				Message message = new Message(root, messageContent);
				mainThread.addMessage(message);
			}
			
		}
		
	}
	
	
	

}
