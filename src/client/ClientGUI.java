package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;

import problemdomain.Attack;
import problemdomain.GridButton;
import problemdomain.Message;
import problemdomain.PlayerGrid;
import problemdomain.Ship;
/**
 * Displays the client window with the battleship grids and chat panel.
 * @author Jean
 *
 */
public class ClientGUI {
	private JFrame frame; //parent window

	private JList chatList; //contains the list of messages sent
	private DefaultListModel chatListModel; //wraps the chatList
	JTextField inputMessage; //field for entering message the client wants to send

	private String username; //client username
	Socket socket; //client socket
	private ObjectOutputStream objectOutputStream; //sends the object through the socket to the server
	private ObjectInputStream objectInputStream; //receives messages from the server 

	//grid axis labels 
	private static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
	private static String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	
	private ArrayList<GridButton> myGrid;
	private ArrayList<GridButton> opponentGrid;

	/**
	 * Constructor for the client GUI
	 */
	public ClientGUI() {
		
		this.myGrid = new ArrayList<GridButton>();
		this.opponentGrid = new ArrayList<>();

		//construct the main window
		this.frame = new JFrame("Battleship");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(600, 800);
		this.frame.setLayout(new BorderLayout());

		//construct panel to contain grids
		JPanel gamePanel = new JPanel(new GridLayout(2, 1));
		gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10 ,40));
		
		JPanel clientPanel = createGridPanel("YourGrid");
		JPanel opponentPanel = createGridPanel("Opponent Grid");
		
		gamePanel.add(clientPanel);
		gamePanel.add(opponentPanel);
		
		//construct chat panel
		JPanel chatPanel = createChatPanel();

		this.frame.add(gamePanel, BorderLayout.CENTER);
		this.frame.add(chatPanel, BorderLayout.SOUTH);

		//window is displayed once the panels have been created
		display();

		//ask user for their name
		username = JOptionPane.showInputDialog(this.frame, "Enter username: ");
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Creates the client's panel which contains the grid for their ship
	 */
	private JPanel createGridPanel(String gridTitle)
	{
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		JPanel centerPanel = new JPanel(new GridLayout(10,10));
		JPanel letterPanel = new JPanel(new GridLayout(10, 1));
		JPanel numberPanel = new JPanel(new GridLayout(1, 10));

		//create labels for y axis
		for (int i = 0; i < 10; i++) {
			JLabel letter = new JLabel(letters[i], SwingConstants.CENTER);
			letterPanel.add(letter);
		}

		//create label for x axis
		for (int i=0; i < 10; i++) {
			JLabel number = new JLabel(numbers[i], SwingConstants.CENTER);
			numberPanel.add(number);
		}

		//create the buttons and store coordinates in GridButton class
		for (int y= 1; y <= 10; y++)
		{

			for (int x = 1; x <= 10; x++)
			{
				JButton button = new JButton();
				button.setBackground(Color.BLUE);
				centerPanel.add(button);
				GridButton gridButton = new GridButton(x, y);
				gridButton.setButton(button);

				if (gridTitle.equals("Opponent Grid")) {
					this.opponentGrid.add(gridButton);
					gridButton.getButton().addActionListener((ActionEvent a) -> {

						if (gridButton.isHit()) {
							this.addMessage("This has been hit already!");
						}
						else
						{
							Attack attack = new Attack(gridButton.getX_location(), gridButton.getY_location(), this.username);
							this.endTurn();

							try {
								objectOutputStream.writeObject(attack);
							}
							catch (IOException e1) 
							{
								this.addMessage("Unable to attack.");
							}
						}

					});
				} else {
					this.myGrid.add(gridButton);
				}

				button.setEnabled(false);
			}
		}

		JLabel title = new JLabel(gridTitle, SwingConstants.CENTER);
		
		panel.add(title, BorderLayout.NORTH);
		panel.add(letterPanel, BorderLayout.WEST);
		panel.add(numberPanel, BorderLayout.SOUTH);
		panel.add(centerPanel, BorderLayout.CENTER);
		
		panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 30, 0));
		
		return panel;

	}

	/**
	 * Creates the chat panel with connect, disconnect, and send buttons
	 * and the window displaying messages being sent.
	 * @return panel panel which contains the chat window
	 */
	private JPanel createChatPanel()
	{
		JPanel panel = new JPanel(new BorderLayout());
		JPanel typePanel = new JPanel(new BorderLayout());
		JButton sendButton = new JButton("Send");

		JPanel networkButtonPanel = new JPanel(new GridLayout(1,2));
		JButton connectButton = new JButton("Connect");
		JButton disconnectButton = new JButton("Disconnect");
		disconnectButton.setEnabled(false);

		//add listener when the connect button is clicked
		connectButton.addActionListener((ActionEvent a) -> {
			connectToNetwork();
			connectButton.setEnabled(false);
			disconnectButton.setEnabled(true);
		});

		//add listener when the disconnect button is clicked 
		disconnectButton.addActionListener((ActionEvent e) -> {
			disconnectToNetwork();
			connectButton.setEnabled(true);
		});

		//makes list that can be displayed on the panel and adds a scrollbar when needed
		chatListModel = new DefaultListModel();
		chatList = new JList(chatListModel);
		JScrollPane scrollPane = new JScrollPane(chatList);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		inputMessage = new JTextField();
		Font font = new Font("SansSerif", Font.PLAIN, 15);
		inputMessage.setFont(font);

		//adds a listener to the send button for sending messages
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

	/**
	 * Connects the client to the server
	 */
	private void connectToNetwork() {
		//ask user for input
		//String host = JOptionPane.showInputDialog(this.frame, "Enter host ip:");
		//int port = Integer.parseInt(JOptionPane.showInputDialog(this.frame, "Enter port number: "));
		String host = "localhost";
		int port = 1234;

		try {
			//make a new socket and obtain input and output streams
			socket = new Socket(host, port);

			this.addMessage("Connected!");

			OutputStream outputStream = socket.getOutputStream();
			objectOutputStream = new ObjectOutputStream(outputStream);

			InputStream inputStream = socket.getInputStream();
			objectInputStream = new ObjectInputStream(inputStream);

			Message username = new Message(this.username, "Connected!");
			objectOutputStream.writeObject(username);

			this.addMessage("Waiting for opponent...");

			//start a thread for receiving messages from the server
			ServerConnection serverConnection = new ServerConnection(this, objectInputStream, socket);
			Thread thread = new Thread(serverConnection);
			thread.start();
		} 
		catch (IOException e1) {
			this.addMessage("Unable to Connect.");
		}
	}

	private void disconnectToNetwork() {
		try {
			objectOutputStream.close();
			objectInputStream.close();
			socket.close();
		} catch (IOException e) {
			this.addMessage("Disconnected to server.");
		}
	}

	/**
	 * Send message object to the server
	 * @param message message object to be sent to the server
	 */
	public void sendMessage(Message message) {
		try {
			objectOutputStream.writeObject(message);

			inputMessage.setText("");

			addMessage(message.toString());
		}
		catch (IOException e1) 
		{
			this.addMessage("Unable to send message.");
		}
	}

	/**
	 * Adds and displays any messages sent or received. 
	 * @param message message from the message object
	 */
	public void addMessage(String message) {

		this.chatListModel.addElement(message);
	} 

	/**
	 * Displays whole window with all the panels.
	 */
	public void display() {
		//this.frame.pack();
		this.frame.setVisible(true);
	}
	
	public void displayShips(PlayerGrid playerGrid) {
		for (int i = 0; i < playerGrid.getGrid().size(); i++) {
			if (playerGrid.getGrid().get(i).isShipPart()) {
				this.myGrid.get(i).getButton().setBackground(Color.LIGHT_GRAY);
			}
			else
			{
				this.myGrid.get(i).getButton().setBackground(Color.BLUE);
			}
		}
	}
	
	public void updatetGrid(Attack attack, String username) {
		ArrayList<GridButton> updateGrid = this.myGrid;
		
		if (username.equals(this.username)) {
			updateGrid = this.opponentGrid;
		}
		
		for (GridButton gridButton: updateGrid) {
			int x = gridButton.getX_location();
			int y = gridButton.getY_location();
			JButton button = gridButton.getButton();

			if (x == attack.getX() && y == attack.getY()) {
				if (attack.isHit()) {
					button.setBackground(Color.RED);
					this.addMessage("A ship has been hit!");
				}
				else {
					button.setBackground(Color.white);
					this.addMessage(attack.getUsername() + " missed!");
				}
				break;
			}
		}

		if (attack.getShips() != null) {
			Ship ship = attack.getShips();
			this.addMessage(ship.getShipType() + " has been sunked by " + attack.getUsername());

			for (GridButton part: ship.getShipParts()) {
				int xShip = part.getX_location();
				int yShip = part.getY_location();

				for (GridButton gridButton: updateGrid) {
					int x = gridButton.getX_location();
					int y = gridButton.getY_location();
					JButton button = gridButton.getButton();

					if (x == xShip && y == yShip) {
						button.setBackground(Color.BLACK);
						break;
					}
				}
			}
		}

		if (attack.isGameOver()) {
			this.gameOver();
		}
	}
	
	public void turn() {
		for (GridButton gridButton: this.opponentGrid) {
			gridButton.getButton().setEnabled(true);
		}
	}

	private void endTurn() {
		for (GridButton gridButton: this.opponentGrid) {
			gridButton.getButton().setEnabled(false);
		}
	}
	
	private void gameOver() {
		
	}

}
