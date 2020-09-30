package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.*;

import problemdomain.*;
 
public class ClientGUI {
	private JFrame frame;
	
	private JList chatList;
	private DefaultListModel chatListModel;
	JTextField inputMessage;
	
	private String username;
	Socket socket;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	
	private static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
	private static String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	
	public ClientGUI() {
		
		this.frame = new JFrame("Battleship");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(600, 800);
		this.frame.setLayout(new BorderLayout());
		
		JPanel gamePanel = new JPanel(new GridLayout(2, 1));
		gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10 ,40));
		
		JPanel clientPanel = createClientPanel();
		JPanel opponentPanel = createOpponentPanel();
		JPanel chatPanel = createChatPanel();
		
		gamePanel.add(clientPanel);
		gamePanel.add(opponentPanel);
		
		this.frame.add(gamePanel, BorderLayout.CENTER);
		this.frame.add(chatPanel, BorderLayout.SOUTH);
		
		display();
		
		username = JOptionPane.showInputDialog(this.frame, "Enter username: ");
	}
	
	private JPanel createClientPanel()
	{
		JPanel centerPanel = new JPanel(new GridLayout(10,10));
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		JPanel letterPanel = new JPanel(new GridLayout(10, 1));
		JPanel numberPanel = new JPanel(new GridLayout(1, 10));
		
		for (int i = 0; i < 10; i++) {
			JLabel letter = new JLabel(letters[i], SwingConstants.CENTER);
			letterPanel.add(letter);
		}
		
		for (int i=0; i < 10; i++) {
			JLabel number = new JLabel(numbers[i], SwingConstants.CENTER);
			numberPanel.add(number);
		}
		
		JLabel title = new JLabel("Your Grid", SwingConstants.CENTER);
		
		for (int i= 0; i<100; i++)
		{
			JButton button = new JButton();
			//button.setPreferredSize(new Dimension(30, 40));
			button.setBackground(Color.LIGHT_GRAY);
			
			centerPanel.add(button);
		}
		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(title, BorderLayout.NORTH);
		mainPanel.add(letterPanel, BorderLayout.WEST);
		mainPanel.add(numberPanel, BorderLayout.SOUTH);
		
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 30, 0));
		
		return mainPanel;
		
	}
	
	private JPanel createOpponentPanel()
	{
		JPanel centerPanel = new JPanel(new GridLayout(10,10));
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		JPanel letterPanel = new JPanel(new GridLayout(10, 1));
		JPanel numberPanel = new JPanel(new GridLayout(1, 10));
		
		for (int i = 0; i < 10; i++) {
			JLabel letter = new JLabel(letters[i], SwingConstants.CENTER);
			letterPanel.add(letter);
		}
		
		for (int i=0; i < 10; i++) {
			JLabel number = new JLabel(numbers[i], SwingConstants.CENTER);
			numberPanel.add(number);
		}
		
		JLabel title = new JLabel("Opponent's Grid", SwingConstants.CENTER);
		
		//mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		
		for (int i= 0; i<100; i++)
		{
			JButton button = new JButton();
			//button.setPreferredSize(new Dimension(30, 40));
			button.setBackground(Color.LIGHT_GRAY);
			button.setOpaque(true);
			
			centerPanel.add(button);
		}
		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(title, BorderLayout.NORTH);
		mainPanel.add(letterPanel, BorderLayout.WEST);
		mainPanel.add(numberPanel, BorderLayout.SOUTH);
		
		return mainPanel;
		}
	
	private JPanel createChatPanel()
	{
		JPanel panel = new JPanel(new BorderLayout());
		JPanel typePanel = new JPanel(new BorderLayout());
		JButton sendButton = new JButton("Send");
		
		JPanel networkButtonPanel = new JPanel(new GridLayout(1,2));
		JButton connectButton = new JButton("Connect");
		JButton disconnectButton = new JButton("Disconnect");
		
		connectButton.addActionListener((ActionEvent a) -> {
			connectToNetwork();
		});
		
		disconnectButton.addActionListener((ActionEvent e) -> {
			disconnectToNetwork();
		});
		
		chatListModel = new DefaultListModel();
		chatList = new JList(chatListModel);
		JScrollPane scrollPane = new JScrollPane(chatList);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		inputMessage = new JTextField();
		Font font = new Font("SansSerif", Font.PLAIN, 15);
		inputMessage.setFont(font);
		
		sendButton.addActionListener(new sendButtonListener());
		
		networkButtonPanel.add(connectButton);
		networkButtonPanel.add(disconnectButton);
		
		typePanel.add(inputMessage, BorderLayout.CENTER);
		typePanel.add(sendButton, BorderLayout.EAST);
		typePanel.add(networkButtonPanel, BorderLayout.SOUTH);
		
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(typePanel, BorderLayout.SOUTH);

		return panel;
	}
	
	private void connectToNetwork() {
	
		try {
			socket = new Socket("localhost", 1234);
			
			this.addMessage("Connected!                                                          "
					+ "                                                                           ");
			
			OutputStream outputStream = socket.getOutputStream();
			objectOutputStream = new ObjectOutputStream(outputStream);
			
			InputStream inputStream = socket.getInputStream();
			objectInputStream = new ObjectInputStream(inputStream);
			
			Message username = new Message(this.username, "Connected!");
			objectOutputStream.writeObject(username);
			
			ServerConnection serverConnection = new ServerConnection(this, objectInputStream, socket);
			Thread thread = new Thread(serverConnection);
			thread.start();
			
		} 
		catch (IOException e1) {
			e1.printStackTrace();
			this.addMessage("Unable to Connect.");
		}
	}
	
	private void disconnectToNetwork() {
		try {
			objectOutputStream.close();
			objectInputStream.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	private class sendButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent a) {
			
			String text = inputMessage.getText();
			
			Message send = new Message(username, text);
			
			try {
				objectOutputStream.writeObject(send);
				
				inputMessage.setText("");
				
				addMessage(send.toString());
				
//				Message receive = (Message) objectInputStream.readObject();
//				
//				this.addMessage(receive.toString());
			}
//			catch (ClassNotFoundException e1) {
//				e1.printStackTrace();
//			}
			catch (IOException e1) 
			{
				e1.printStackTrace();
				addMessage("Unable to send message.");
			}
		}
	}
	
	public void addMessage(String message) {
		
		this.chatListModel.addElement(message);
	}
	
	public void display() {
		//this.frame.pack();
		this.frame.setVisible(true);
	}
}
