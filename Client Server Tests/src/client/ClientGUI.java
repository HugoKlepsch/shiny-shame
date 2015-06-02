package client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import sharedPackages.Message;

public class ClientGUI {
	private static JFrame root;
	private static JPanel mainPanel;
	private static JTextArea messageArea;
	private static JTextField entry;
	private static JLabel messageLabel;
	private static ButtonHandler onClick = new ButtonHandler();
	public static Font defaultFont = new Font("Ubuntu", 1, 13);
	public static Font largeFont = new Font("Ubuntu", 1, 36);
	
	
	
	private static class ButtonHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == entry){
				if (!(entry.getText().equals("/exit"))) {
					ClientMain.messageQueue.enQueue(new Message(ClientMain
							.getCreds(), entry.getText()));
					entry.setText("");
				} else {
					ClientMain.setAlive(false);
					root.dispose();
				}
			}
			
		}
		
	}
	
	public ClientGUI(String ipAddress) {
		root = new JFrame("SquadMessenger");
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dm = tk.getScreenSize();
		double defaultWidth = dm.width / 1.2;
		double defaultHeight = dm.height / 1.9;
		root.setBounds(0, 0, (int) (defaultWidth), (int) (defaultHeight));
		mainPanel = new JPanel(new GridLayout(0,1, 5, 5));
		
		messageLabel = new JLabel("Messages");
		messageLabel.setFont(defaultFont);
		messageArea = new JTextArea();
		messageArea.setFont(defaultFont);
		entry = new JTextField();
		entry.setFont(defaultFont);
		entry.addActionListener(onClick);
		Dimension entryDim = new Dimension((int) defaultWidth, (int) (defaultHeight /20));
		entry.setSize(entryDim);
		
		root.add(mainPanel);
		mainPanel.add(messageLabel);
		mainPanel.add(messageArea);
		mainPanel.add(entry);
		
		ServerOutComms outComms = new ServerOutComms(ipAddress, ClientMain.getCreds());
		outComms.start();
		
		root.setVisible(true);
	}
	
	public static void addMessage(Message message){
		messageArea.setText(messageArea.getText() + message.getCredentials().getUserName() + ": " + message.getMessage() +"\n");
	}

}
