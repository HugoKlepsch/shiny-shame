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
 * kick - args either -u username or -a ip to kick
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
				list(args);
			} else if(command.equals("kick")){
				kick(args);
			}
			
		}
		
	}
	
	private void list(String[] args){
		Vector<String> users = new Vector<String>();
		users = mainThread.getUsers3();
		for(int i = 0; i<users.size();i++){
			System.out.println(users.elementAt(i));
		}
	}
	
	private void kick(String[] args){
		String userToKick;
		if(args[0].equals("-u")){
			userToKick = args[1];
			Vector<String> users = mainThread.getUsers3();
			String[] userArray = (String[]) users.toArray();
			Arrays.binarySearch(userArray, userToKick);
		} else if(args[0].equals("-id")){
			userToKick = args[1];
		}
	}
	
	

}
