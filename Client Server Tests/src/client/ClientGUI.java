package client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import sharedPackages.Message;


/**
 * 
 * @author graham
 * @description - displays the messages and allows for input for sending messages
 *
 */
public class ClientGUI {
	//initialize all GUI elements
	private static JFrame root;
	private static JPanel mainPanel;
	private static JTextArea messageArea;
	private static JTextArea userArea;
	private static JTextField entry;
	private static JScrollPane messageScrollPane, userScrollPane, entryScrollPane;
	private static ButtonHandler onClick = new ButtonHandler();
	public static Font defaultFont = new Font("Ubuntu", 1, 13);
	public static Font largeFont = new Font("Ubuntu", 1, 36);
	private static String userTitle = "Users:" + "\n";
	
	
	/**
	 * 
	 * @author graham
	 * @description - handles button and text entry input
	 */
	private static class ButtonHandler implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			//if statement checks what performed the action
			//if the enter button is clicked within the text entry field
			if(e.getSource() == entry){
				//as long as the entered message is not /exit
				if (!(entry.getText().equals("/exit"))) {
					//add the message to the message queue
					ClientMain.messageQueue.enQueue(new Message(ClientMain
							.getCreds(), entry.getText()));
					//reset text entry field
					entry.setText("");
				} else {
					//kill the client threads
					ClientMain.setAlive(false);
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					//close window after delay
					root.dispose();
				}
			}
			
		}
		
	}
	/**
	 * @description - creates all the GUI objects and starts the other threads
	 * @param ipAddress - ip address to connect to
	 */
	public ClientGUI(String ipAddress) {
		boolean shouldFill = true;
		//sets up the frame and main panel
		root = new JFrame("SquadMessenger");
		root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dm = tk.getScreenSize();
		double defaultWidth = dm.width / 1.2;
		double defaultHeight = dm.height / 1.9;
		root.setBounds(0, 0, (int) (defaultWidth), (int) (defaultHeight));
		mainPanel = new JPanel();
		root.add(mainPanel);
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		if(shouldFill){
			c.fill = GridBagConstraints.HORIZONTAL;
		}
		
		//sets up the message area for receiving messages
		messageArea = new JTextArea();
		messageArea.setFont(defaultFont);
		messageArea.setEditable(false);
		//sets the scrollbar to auto scroll with new messages
		DefaultCaret caret = (DefaultCaret)messageArea.getCaret();
		 caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		messageScrollPane = new JScrollPane(messageArea);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 10;
		c.weighty = 100;
		c.ipady = 300;
		mainPanel.add(messageScrollPane, c);
		//sets up the user display area
		userArea = new JTextArea();
		userArea.setText("Users:" + "\n");
		userArea.setFont(defaultFont);
		userArea.setEditable(false);
		userScrollPane = new JScrollPane(userArea);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 100;
		c.ipady = 300;
		mainPanel.add(userScrollPane, c);
		
		//sets up the entry field
		entry = new JTextField();
		entry.setFont(defaultFont);
		entry.addActionListener(onClick);
		entryScrollPane = new JScrollPane(entry);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 1;
		mainPanel.add(entryScrollPane, c);
		
		//creates the new server communication out thread
		ServerOutComms outComms = new ServerOutComms(ipAddress, ClientMain.getCreds());
		//starts the thread
		outComms.start();
		//sets the frame to be visible
		root.setVisible(true);
	}
	/**
	 * @description - adds a message on screen
	 * @param message - message to add the text area for display
	 */
	public static void addMessage(Message message){
		messageArea.setText(messageArea.getText() + message.getCredentials().getUserName() + ": " + message.getMessage() +"\n");
	}
	/**
	 * @description - updates the users on screen
	 * @param users - the user vector to update the user list on display with
	 */
	public static void updateUsers(Vector<String> users){
		userArea.setText(userTitle);
		for(int i = 0; i<users.size();i++){
			userArea.setText(userArea.getText() + users.get(i) + "\n");
		}
	}
	/**
	 * @description - allows other threads to kill the window
	 */
	public static void exit(){
		root.dispose();
	}

}
