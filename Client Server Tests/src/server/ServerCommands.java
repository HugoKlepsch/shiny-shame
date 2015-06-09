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
 * list - no args
 * kick - args either -u username or -id id to kick, -a kicks all
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
				try {
					kick(args);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(command.equals("help")){
				System.out.println("message, args = the message sent - sends a message as root");
				System.out.println("list, no args - lists all users with id numbers");
				System.out.println("kick, args = -u 'username', -id 'id number', -a 'all' - kicks users");
			}
			
		}
		
	}
	
	private void list(String[] args){
		Vector<String> users = new Vector<String>();
		users = mainThread.getUsers3();
		for(int i = 0; i<users.size();i++){
			System.out.println("User: " + users.elementAt(i) + " ID: " + i);
		}
	}
	
	private void kick(String[] args) throws IOException{
		Vector<String> users = mainThread.getUsers3();
		String[] userArray = (String[]) users.toArray();
		String userToKick;
		if(args[0].equals("-u")){
			userToKick = args[1];
			int index = Arrays.binarySearch(userArray, userToKick);
			String kickMsg  = "User: " + mainThread.connections.elementAt(index).getUserDeets().getUserName() + " was kicked";
			Message kickMessage = new Message(root, kickMsg);
			mainThread.addMessage(kickMessage);
			mainThread.connections.elementAt(index).kick();
		} else if(args[0].equals("-id")){
			userToKick = args[1];
			int index = Integer.parseInt(userToKick);
			String kickMsg  = "User: " + mainThread.connections.elementAt(index).getUserDeets().getUserName() + " was kicked";
			Message kickMessage = new Message(root, kickMsg);
			mainThread.addMessage(kickMessage);
			mainThread.connections.elementAt(index).kick();
		} else if(args[0].equals("-a")){
			for(int i = 0; i<mainThread.connections.size();i++){
				int index = i;
				String kickMsg  = "User: " + mainThread.connections.elementAt(index).getUserDeets().getUserName() + " was kicked";
				Message kickMessage = new Message(root, kickMsg);
				mainThread.addMessage(kickMessage);
				mainThread.connections.elementAt(index).kick();
			}
		}
	}
	
	

}
