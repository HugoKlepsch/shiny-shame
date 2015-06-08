package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Vector;

import sharedPackages.LoginDeets;
import sharedPackages.Message;

//TODO command list
/*
 * commands to have:
 * message - args are message as root
 * list - args either users or ip, if neither list both
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
			} else if(command.equals("list")){
				Vector<String> ipAddress = new Vector<String>();
				Vector<String> users = new Vector<String>();
				if(args[0].equals("ip")){
					ipAddress = mainThread.getConnectedIPAddresses();
					for(int i = 0; i<ipAddress.size();i++){
						System.out.println(ipAddress.elementAt(i));
					}
				} else if(args[0].equals("users")){
					users = mainThread.getUsers3();
					for(int i = 0; i<users.size();i++){
						System.out.println(users.elementAt(i));
					}
				} else{
					ipAddress = mainThread.getConnectedIPAddresses();
					users = mainThread.getUsers3();
					for(int i = 0; i<users.size();i++){
						System.out.println(users.elementAt(i) + " : " + ipAddress.elementAt(i));
					}
				}
			}
			
		}
		
	}
	
	
	

}
