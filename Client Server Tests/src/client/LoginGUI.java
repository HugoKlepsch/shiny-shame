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

public class LoginGUI {
	private static JFrame root;
	private static JPanel mainPanel;
	private static JLabel mainLabel, userLabel, ipLabel;
	private static JTextField userEntry, ipEntry;
	private static JButton connectButt;
	private static ButtonHandler onClick = new ButtonHandler();
	private static boolean userEntered, ipEntered;
	private static String ipAddress;
	
	
	private static class ButtonHandler implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == userEntry){
				userEntered = true;
				if(userEntered && ipEntered){
					connectButt.setEnabled(true);
				}
			} else if(e.getSource() == ipEntry){
				ipEntered = true;
				ipAddress = ipEntry.getText();
				if(userEntered && ipEntered){
					connectButt.setEnabled(true);
				}
			} else if(e.getSource() == connectButt){
				ClientMain.setCreds(new LoginDeets(userEntry.getText(), null));
				ClientMain.startGUI(ipAddress);
				root.dispose();
			}
			
		}
		
	}
	
	public LoginGUI(){
		root = new JFrame("Login");
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dm = tk.getScreenSize();
		root.setBounds(0, 0, (int) (dm.width / 1.2), (int) (dm.height / 1.9));
		mainPanel = new JPanel(new GridLayout(0,2));
		
		mainLabel = new JLabel("Login");
		mainLabel.setFont(ClientGUI.largeFont);
		userLabel = new JLabel("Username:");
		userLabel.setFont(ClientGUI.defaultFont);
		ipLabel = new JLabel("IP Address:");
		ipLabel.setFont(ClientGUI.defaultFont);
		userEntry = new JTextField();
		userEntry.setFont(ClientGUI.defaultFont);
		userEntry.addActionListener(onClick);
		ipEntry = new JTextField();
		ipEntry.setFont(ClientGUI.defaultFont);
		ipEntry.addActionListener(onClick);
		connectButt = new JButton("Connect");
		connectButt.setFont(ClientGUI.defaultFont);
		connectButt.addActionListener(onClick);
		connectButt.setEnabled(false);
		ipEntered = userEntered = false;
		
		root.add(mainPanel);
		mainPanel.add(userLabel);
		mainPanel.add(userEntry);
		mainPanel.add(ipLabel);
		mainPanel.add(ipEntry);
		mainPanel.add(connectButt);
		
		root.setVisible(true);
		
		
	}

	public static String getIpAddress() {
		return ipAddress;
	}

	public static void setIpAddress(String ipAddress) {
		LoginGUI.ipAddress = ipAddress;
	}
	

}
