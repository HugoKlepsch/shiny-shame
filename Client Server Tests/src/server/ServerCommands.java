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

/**
 * 
 * @author graham
 * @description - the thread that handles server input for different commands
 *
 */
public class ServerCommands extends Thread {
	//buffered reader for accepting input
	private BufferedReader commandIn;
	//root credentials
	private LoginDeets root;
	/**
	 * @description - intializes root and buffered reader
	 */
	public ServerCommands(){
		commandIn = new BufferedReader(new InputStreamReader(System.in));
		root = new LoginDeets("root", "dank");
	}
	/**
	 * @description - the main method of the thread, called upon .start()
	 */
	public void run(){
		String input = "";
		//while server input isn't close accept input
		while(!(input.equals("close"))){
			//try reading input in
			try {
				input = commandIn.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//splits the input on spaces
			String[] temp = input.split(" ");
			//the first word is the command
			String command = temp[0];
			//set args to null in case they don't exist
			String[] args = null;
			//if args exist put them in an array
			if(temp.length > 1){
				args = Arrays.copyOfRange(temp, 1, temp.length);
			}
			//if statements check what command was entered
			if(command.equals("message")){
				//join the words of the message (the args) together into one string
				String messageContent = String.join(" ", args);
				//make the words into a message from root
				Message message = new Message(root, messageContent);
				//add the message to the message vector
				mainThread.addMessage(message);
			} else if(command.equals("list")){
				//if the command is list run the list method
				list();
			} else if(command.equals("kick")){
				//try to run the kick method if that is the command
				try {
					kick(args);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(command.equals("help")){
				//if the command is help display info on all the available commands
				System.out.println("message, args = the message sent - sends a message as root");
				System.out.println("list, no args - lists all users with id numbers");
				System.out.println("kick, args = -u 'username', -id 'id number', -a 'all' - kicks users");
				System.out.println("refresh, no args - kicks all users then deletes all messages");
			} else if(command.equals("refresh")){
				//try to run the refresh method
				try {
					refresh();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		//close the server environment
		System.exit(0);
		
	}
	/**
	 * @description - lists all the connected users and their id number
	 */
	private void list(){
		Vector<String> users = new Vector<String>();
		users = mainThread.getUsers3();
		for(int i = 0; i<users.size();i++){
			System.out.println("User: " + users.elementAt(i) + " ID: " + i);
		}
	}
	/**
	 * 
	 * @param args - the user to kick
	 * @throws IOException
	 * @description - kicks a user from the server
	 */
	private void kick(String[] args) throws IOException{
		Vector<String> users = mainThread.getUsers3();
		//turn the vector array into a normal one for easier binary search
		Object[] userArray =  users.toArray();
		String userToKick;
		//if -u then look for user by username
		if(args[0].equals("-u")){
			userToKick = args[1];
			//find the user in the user array
			int index = Arrays.binarySearch(userArray, userToKick);
			String kickMsg  = "User: " + mainThread.connections.elementAt(index).getUserDeets().getUserName() + " was kicked";
			//create the kick message as root
			Message kickMessage = new Message(root, kickMsg);
			//add the message to the message vector
			mainThread.addMessage(kickMessage);
			//send the kick request
			mainThread.connections.elementAt(index).kick();
		//if -id then look for user by id
		} else if(args[0].equals("-id")){
			userToKick = args[1];
			//turn the string id into an integer
			int index = Integer.parseInt(userToKick);
			String kickMsg  = "User: " + mainThread.connections.elementAt(index).getUserDeets().getUserName() + " was kicked";
			//create the kick message as root
			Message kickMessage = new Message(root, kickMsg);
			//add the message to the message vector
			mainThread.addMessage(kickMessage);
			//send the kick request
			mainThread.connections.elementAt(index).kick();
		//if -a then kick all users
		} else if(args[0].equals("-a")){
			/*for loop runs through all users, creates a kick message then sends
			a kick request
			*/
			for(int i = 0; i<mainThread.connections.size();i++){
				int index = i;
				String kickMsg  = "User: " + mainThread.connections.elementAt(index).getUserDeets().getUserName() + " was kicked";
				Message kickMessage = new Message(root, kickMsg);
				mainThread.addMessage(kickMessage);
				mainThread.connections.elementAt(index).kick();
			}
		}
	}
	/**
	 * @description - kicks all users then clears the message vector
	 * @throws IOException
	 */
	private void refresh() throws IOException{
		String[] all = {"-a"};
		kick(all);
		mainThread.messages.clear();
	}
	
	

}
