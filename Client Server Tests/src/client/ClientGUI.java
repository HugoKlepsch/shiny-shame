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

public class ClientGUI {
	private static JFrame root;
	private static JPanel mainPanel;
	private static JTextArea messageArea;
	private static JTextArea userArea;
	private static JTextField entry;
	private static JScrollPane messageScrollPane;
	private static JScrollPane userScrollPane;
	private static ButtonHandler onClick = new ButtonHandler();
	public static Font defaultFont = new Font("Ubuntu", 1, 13);
	public static Font largeFont = new Font("Ubuntu", 1, 36);
	private static String userTitle = "Users:" + "\n";
	
	
	
	private static class ButtonHandler implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == entry){
				if (!(entry.getText().equals("/exit"))) {
					ClientMain.messageQueue.enQueue(new Message(ClientMain
							.getCreds(), entry.getText()));
					entry.setText("");
				} else {
					ClientMain.setAlive(false);
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					root.dispose();
				}
			}
			
		}
		
	}
	
	public ClientGUI(String ipAddress) {
		boolean shouldFill = true;
		
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
		
		
		messageArea = new JTextArea();
		messageArea.setFont(defaultFont);
		messageArea.setEditable(false);
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
		
		
		entry = new JTextField();
		entry.setFont(defaultFont);
		entry.addActionListener(onClick);
//		Dimension entryDim = new Dimension((int) defaultWidth, (int) (defaultHeight /100.0));
//		entry.setSize(entryDim);
//		entry.setPreferredSize(entryDim);
//		entry.setMinimumSize(entryDim);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 1;
		mainPanel.add(entry, c);
		
		ServerOutComms outComms = new ServerOutComms(ipAddress, ClientMain.getCreds());
		outComms.start();
		
		root.setVisible(true);
	}
	
	public static void addMessage(Message message){
		messageArea.setText(messageArea.getText() + message.getCredentials().getUserName() + ": " + message.getMessage() +"\n");
	}
	
	public static void updateUsers(Vector<String> users){
		userArea.setText(userTitle);
		for(int i = 0; i<users.size();i++){
			userArea.setText(userArea.getText() + users.get(i) + "\n");
//			System.out.println("Adding: " + users.get(i) + " to the GUI");
		}
	}

}
