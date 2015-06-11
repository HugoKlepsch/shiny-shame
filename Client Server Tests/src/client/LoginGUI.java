package client;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sharedPackages.LoginDeets;
/**
 * 
 * @author graham
 * @description - GUI for displaying and getting login info
 *
 */
public class LoginGUI {
	//initialize all GUI elements
	private static JFrame root;
	private static JPanel mainPanel;
	private static JLabel mainLabel, userLabel, ipLabel;
	private static JTextField userEntry, ipEntry;
	private static JButton connectButt, quitButt;
	private static ButtonHandler onClick = new ButtonHandler();
	private static boolean userEntered, ipEntered;
	private static String ipAddress;
	
	/**
	 * 
	 * @author graham
	 * @description - handles all button and entry input
	 */
	private static class ButtonHandler implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			//if statement checks what performed the action
			if(e.getSource() == userEntry){
				//if enter button pushed on keyboard in userEntry set true
				userEntered = true;
				/*if you've hit enter in both ip address entry and user entry
				 * enable connect button
				*/
				if(userEntered && ipEntered){
					connectButt.setEnabled(true);
				}
			} else if(e.getSource() == ipEntry){
				//if enter button pushed on keyboard in ipEntry set true
				ipEntered = true;
				ipAddress = ipEntry.getText();
				/*if you've hit enter in both ip address entry and user entry
				 * enable connect button
				*/
				if(userEntered && ipEntered){
					connectButt.setEnabled(true);
				}
			} else if(e.getSource() == connectButt){
				//set credentials of the thread
				ClientMain.setCreds(new LoginDeets(userEntry.getText(), null));
				//start the main GUI
				ClientMain.startGUI(ipAddress);
				//close this window
				root.dispose();
			} else if(e.getSource() == quitButt){
				//close the window
				root.dispose();
			}
			
		}
		
	}
	/**
	 * @description - creates all the GUI objects
	 */
	public LoginGUI(){
		//set up frame
		root = new JFrame("Login");
		root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dm = tk.getScreenSize();
		root.setBounds(0, 0, (int) (dm.width / 1.2), (int) (dm.height / 1.9));
		//set up the main panel
		mainPanel = new JPanel(new GridLayout(0,2));
		mainLabel = new JLabel("Login");
		mainLabel.setFont(ClientGUI.largeFont);
		//set up user label
		userLabel = new JLabel("Username:");
		userLabel.setFont(ClientGUI.defaultFont);
		//set up ip label
		ipLabel = new JLabel("IP Address:");
		ipLabel.setFont(ClientGUI.defaultFont);
		//set up user entry
		userEntry = new JTextField();
		userEntry.setFont(ClientGUI.defaultFont);
		userEntry.addActionListener(onClick);
		//set up ip entry
		ipEntry = new JTextField();
		ipEntry.setFont(ClientGUI.defaultFont);
		ipEntry.addActionListener(onClick);
		//set up connect button
		connectButt = new JButton("Connect");
		connectButt.setFont(ClientGUI.defaultFont);
		connectButt.addActionListener(onClick);
		connectButt.setEnabled(false);
		//set up quit button
		quitButt = new JButton("Quit");
		quitButt.setFont(ClientGUI.defaultFont);
		quitButt.addActionListener(onClick);
		//initialize states of entry to false
		ipEntered = userEntered = false;
		//add all the components to the GUI
		root.add(mainPanel);
		mainPanel.add(userLabel);
		mainPanel.add(userEntry);
		mainPanel.add(ipLabel);
		mainPanel.add(ipEntry);
		mainPanel.add(connectButt);
		mainPanel.add(quitButt);
		//make the window visible
		root.setVisible(true);
		
		
	}


	

}
