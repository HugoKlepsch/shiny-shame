package sharedPackages;

import java.io.Serializable;
import java.util.Vector;
/**
 * 
 * @author graham
 * @description - class that is sent through the stream, holds all data that is transfered
 *
 */
public class ActionRequest implements Serializable {
	//constants for integers that represent different actions
	public static final int CSGETCURRENTMESSAGEINDEX = 1;
	public static final int CSGETMESSAGE = 2;
	public static final int CSSENDMESSAGE = 3;
	public static final int CSDISCONNECT = 4;
	public static final int CSCONNECT = 5;
	public static final int SCSENDCURRENTMESSAGEINDEX = 6;
	public static final int SCSENDMESSAGE = 7;
	public static final int CSPING = 8;
	public static final int SCSENDUSERS = 9;
	public static final int SCKICK = 10;
	//needed to make serializable
	private static final long serialVersionUID = 1L;
	//initializes variables to hold action type, message, index and/or users
	private int action;
	private Message message;
	private int index;
	private Vector<String> users;
	/**
	 * 
	 * @param action - action type
	 * @description - constructor for just an action type
	 */
	public ActionRequest(int action){
		this.action = action;
	}
	/**
	 * 
	 * @param action - action type
	 * @param message - message to send
	 * @description - constructor for an action type and a message
	 */
	public ActionRequest(int action, Message message){
		this.action = action;
		this.message = message;
	}
	/**
	 * 
	 * @param action - action type
	 * @param index - index of the message vector
	 * @description - constructor for an action type and the index
	 */
	public ActionRequest(int action, int index){
		this.action = action;
		this.index = index;
	}
	/**
	 * 
	 * @param action - action type
	 * @param users - vector array of current users
	 * @description - constructor for an action type and user vector
	 */
	public ActionRequest(int action, Vector<String> users){
		this.action = action;
		this.users = users;
	}
	/**
	 * 
	 * @param action - action type
	 * @param message - message to send
	 * @param users - vector array of current users
	 * @description - constructor for sending action type, message and user vector
	 */
	public ActionRequest(int action, Message message, Vector<String> users) {
		super();
		this.action = action;
		this.message = message;
		this.users = users;
	}
	/**
	 * 
	 * @param action - action type
	 * @param index - index of message vector
	 * @param users - vector array of current users
	 * @description - constructor for sending action type, index and user vector
	 */
	public ActionRequest(int action, int index, Vector<String> users) {
		super();
		this.action = action;
		this.index = index;
		this.users = users;
	}
	/**
	 * 
	 * @return - the action type of the request
	 */
	public int getAction() {
		return action;
	}
	/**
	 * 
	 * @param action - the type of action the request should be
	 */
	public void setAction(int action) {
		this.action = action;
	}
	/**
	 * 
	 * @return - the message the request contains
	 */
	public Message getMessage() {
		return message;
	}
	/**
	 * 
	 * @param message - the message the request should contain
	 */
	public void setMessage(Message message) {
		this.message = message;
	}
	/**
	 * 
	 * @return - the index of the message vector the request contains
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * 
	 * @param index - the index of the message vector the request should contain
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * 
	 * @return - the vector of current users the request contains
	 */
	public Vector<String> getUsers() {
		return users;
	}
	/**
	 * 
	 * @param users - the user vector the request should contain
	 */
	public void setUsers(Vector<String> users) {
		this.users = users;
	}
	
	

}
