package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

import problemdomain.*;
 
public class ClientGUI implements PropertyChangeListener {
	private JFrame frame;
	
	private JList chatList;
	private DefaultListModel chatListModel;
	JTextField inputMessage;
	
	private String username;
	Socket socket;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	
	private ArrayList<GridButton> playerGrid;
	private ArrayList<GridButton> opponentGrid;
	JPanel mainPanel;
	
	private static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
	private static String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	
	//private PropertyChangeSupport propertyChangeSupport;
	
	public ClientGUI() {
		
		playerGrid = new ArrayList<>();
		
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
		
		for (int y= 1; y <= 10; y++)
		{
			
			for (int x = 1; x <= 10; x++)
			{
				JButton button = new JButton();
				button.setBackground(Color.LIGHT_GRAY);
				GridButton gridButton = new GridButton(button, x, y);
				gridButton.attachObserver(this);
				
//				button.addActionListener((ActionEvent a) -> {
//					String message = gridButton.clicked();
//					
//					Message attackMessage = new Message(this.username, message);
//					sendMessage(attackMessage);
//				});
				
				playerGrid.add(gridButton);
				
				centerPanel.add(button);
				
			}
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
		mainPanel = new JPanel(new BorderLayout(10, 10));
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
		
		
//		for (int i= 0; i<100; i++)
//		{
//			JButton button = new JButton();
//			button.setBackground(Color.LIGHT_GRAY);
//			button.setOpaque(true);
//			
//			centerPanel.add(button);
//		}
		
		//mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(title, BorderLayout.NORTH);
		mainPanel.add(letterPanel, BorderLayout.WEST);
		mainPanel.add(numberPanel, BorderLayout.SOUTH);
		mainPanel.setVisible(false);
		
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
		
		sendButton.addActionListener((ActionEvent a) -> {
			
			String text = inputMessage.getText();
			
			Message send = new Message(username, text);
			
			sendMessage(send);
		});
		
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
			
			this.addMessage("Connected!");
			
			OutputStream outputStream = socket.getOutputStream();
			objectOutputStream = new ObjectOutputStream(outputStream);
			
			InputStream inputStream = socket.getInputStream();
			objectInputStream = new ObjectInputStream(inputStream);
			
			Message username = new Message(this.username, "Connected!");
			objectOutputStream.writeObject(username);
			
			this.addMessage("Waiting for opponent...");
			
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

		}
	}
	
	public void sendMessage(Message message) {
		try {
			objectOutputStream.writeObject(message);
			
			inputMessage.setText("");
			
			addMessage(message.toString());
		}
		catch (IOException e1) 
		{
			e1.printStackTrace();
			addMessage("Unable to send message.");
		}
	}
	
	public void sendPlayerGrid() {
		try {
			objectOutputStream.writeObject(playerGrid);
			addMessage("Sending grid!");
			System.out.println("Sent player grid.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addMessage(String message) {
		
		this.chatListModel.addElement(message);
	} 
	
	public void addOpponentShips() {
		try {
			this.opponentGrid = (ArrayList<GridButton>) objectInputStream.readObject();
			System.out.println("Received opponent grid!");
			System.out.println(this.opponentGrid.get(0));

			JPanel panel = new JPanel(new GridLayout(10,10));

			for (GridButton grid : opponentGrid) {
				grid.getButton().addActionListener((ActionEvent a) -> {
					String message = grid.gotHit();

					Message attackMessage = new Message(this.username, message);
					sendMessage(attackMessage);
				});
				panel.add(grid.getButton());
			}

			mainPanel.add(panel, BorderLayout.CENTER);
			mainPanel.setVisible(true);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void makeShips() {
		boolean shipPlaced = false;

		while (!shipPlaced) {
			shipPlaced = placeShips(5, "Aircraft Carrier");
		}
		shipPlaced = false;
		while (!shipPlaced) {
			shipPlaced = placeShips(4, "Battleship");
		}
		shipPlaced = false;
		while (!shipPlaced) {
			shipPlaced = placeShips(3, "Cruiser");
		}
		shipPlaced = false;
		while (!shipPlaced) {
			shipPlaced = placeShips(3, "Submarine");
		}
		shipPlaced = false;
		while (!shipPlaced) {
			shipPlaced = placeShips(2, "Destroyer");
		}
		
		
	}
	
	private boolean placeShips(int numberOfParts, String shipType) {
		ArrayList<GridButton> shipParts = new ArrayList<>();
		boolean horizontal = true;
		
		int x_location = (int) (1 + Math.random() * 10);
		int y_location = (int) (1 + Math.random() * 10);
		
		//System.out.println("\n" + x_location + " " + y_location);
		
		int num = (int) (1+ Math.random() *100);
		//System.out.println(num);
		
		if (num % 2 == 0) {
			horizontal = false;
		}
		
		int shipStart = 0;
		
		for (GridButton grid : playerGrid) {
			if (grid.getX_location() == x_location && grid.getY_location() == y_location)
			{
				shipStart = playerGrid.indexOf(grid);
			}
		}
		
		if (horizontal) {
			int xShip = shipStart;
			if (numberOfParts > x_location)
			{
				for (int i = 0; i < numberOfParts; i++)
				{
					 
					xShip = shipStart + i;
					GridButton currentButton = playerGrid.get(xShip);
					if (!currentButton.isShipPart()) {
						shipParts.add(currentButton);
					}
					else {
						return false;
					}
				}
			}
			else {
				
				for (int i = 0; i < numberOfParts; i++)
				{
					xShip = shipStart - i;
					GridButton currentButton = playerGrid.get(xShip);
					if (!currentButton.isShipPart()) {
						shipParts.add(currentButton);
					}
					else {
						return false;
					}
				}
			}
		}
		else {
			int yShip = shipStart;
			if (numberOfParts > y_location)
			{
				for (int i = 0; i < numberOfParts; i++) {
					yShip = shipStart + i*10;
					GridButton currentButton = playerGrid.get(yShip);
					if (!currentButton.isShipPart()) {
						shipParts.add(currentButton);
					}
					else {
						return false;
					}
				}
			}
			else {
				for (int i = 0; i < numberOfParts; i++) {
					yShip = shipStart - i*10;
					GridButton currentButton = playerGrid.get(yShip);
					if (!currentButton.isShipPart()) {
						shipParts.add(currentButton);
					}
					else {
						return false;
					}
				}
			}
		}
		
		Ship ship = new Ship(numberOfParts, shipType);

		for (GridButton part: shipParts) {
			ship.addShipPart(part);
			part.makeShipPart(ship);
		}
		
		return true;
	}
	
	public void display() {
		//this.frame.pack();
		this.frame.setVisible(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("Here!");
		GridButton gridButton = (GridButton) evt.getSource();
		int button = playerGrid.indexOf(gridButton);
		playerGrid.get(button).clicked();
		
	}
	
}
