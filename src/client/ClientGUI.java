package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.*;

import problemdomain.GridButton;
import problemdomain.Message;
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

	private ArrayList<Ship> ships; //list of ships the client has
	private ArrayList<Ship> opponentShips; //list of ships the opponent has
	
	private ArrayList<GridButton> playerGrid; //contains buttons of the client's board
	private ArrayList<GridButton> opponentGrid; //contains buttons of the opponent's board
	JPanel opponentGridPanel;
	JPanel playerPanel;

	//grid axis labels 
	private static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
	private static String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

	/**
	 * Constructor for the client GUI
	 */
	public ClientGUI() {

		//initialize ArrayLists
		playerGrid = new ArrayList<>();
		this.opponentGrid = new ArrayList<>();
		ships = new ArrayList<>();
		opponentShips = new ArrayList<>();

		//construct the main window
		this.frame = new JFrame("Battleship");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(600, 800);
		this.frame.setLayout(new BorderLayout());

		//construct panel to contain grids
		JPanel gamePanel = new JPanel(new GridLayout(2, 1));
		gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10 ,40));
		createClientPanel();
		createOpponentPanel();
		
		//construct chat panel
		JPanel chatPanel = createChatPanel();

		gamePanel.add(this.playerPanel);
		gamePanel.add(this.opponentGridPanel);

		this.frame.add(gamePanel, BorderLayout.CENTER);
		this.frame.add(chatPanel, BorderLayout.SOUTH);

		//window is displayed once the panels have been created
		display();

		//ask user for their name
		username = JOptionPane.showInputDialog(this.frame, "Enter username: ");
	}

	/**
	 * Creates the client's panel which contains the grid for their ship
	 */
	private void createClientPanel()
	{
		JPanel centerPanel = new JPanel(new GridLayout(10,10));
		this.playerPanel = new JPanel(new BorderLayout(10, 10));
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
				GridButton gridButton = new GridButton(button, x, y);
				playerGrid.add(gridButton); //add button to arraylist for player's buttons
				centerPanel.add(button);
			}
		}


		JLabel title = new JLabel("Your Grid", SwingConstants.CENTER);

		this.playerPanel.add(title, BorderLayout.NORTH);
		this.playerPanel.add(letterPanel, BorderLayout.WEST);
		this.playerPanel.add(numberPanel, BorderLayout.SOUTH);
		this.playerPanel.add(centerPanel, BorderLayout.CENTER);

		this.playerPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 30, 0));

	}

	/**
	 * Creates the opponents panel with grids for their ships. Buttons on this
	 * panel will respond when the client clicks on it,
	 */
	private void createOpponentPanel()
	{
		this.opponentGridPanel = new JPanel(new BorderLayout(10, 10));
		JPanel centerPanel = new JPanel(new GridLayout(10, 10));
		JPanel letterPanel = new JPanel(new GridLayout(10, 1));
		JPanel numberPanel = new JPanel(new GridLayout(1, 10));

		//label grid axis
		for (int i = 0; i < 10; i++) {
			JLabel letter = new JLabel(letters[i], SwingConstants.CENTER);
			letterPanel.add(letter);
		}

		for (int i=0; i < 10; i++) {
			JLabel number = new JLabel(numbers[i], SwingConstants.CENTER);
			numberPanel.add(number);
		}

		//add buttons and their coordinates 
		for (int y= 1; y <= 10; y++)
		{

			for (int x = 1; x <= 10; x++)
			{
				JButton button = new JButton();
				GridButton gridButton = new GridButton(button, x, y);
				this.opponentGrid.add(gridButton);
				centerPanel.add(button);
			}
		}

		JLabel title = new JLabel("Opponent's Grid", SwingConstants.CENTER);

		this.opponentGridPanel.add(title, BorderLayout.NORTH);
		this.opponentGridPanel.add(letterPanel, BorderLayout.WEST);
		this.opponentGridPanel.add(numberPanel, BorderLayout.SOUTH);
		this.opponentGridPanel.add(centerPanel, BorderLayout.CENTER);
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
		String host = JOptionPane.showInputDialog(this.frame, "Enter host ip:");
		int port = Integer.parseInt(JOptionPane.showInputDialog(this.frame, "Enter port number: "));

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
	 * Sends the player's grid with the ship coordinates to the server
	 */
	public void sendPlayerGrid() {
		try {
			objectOutputStream.writeObject(playerGrid);
		} catch (IOException e) {
			this.addMessage("Unable to send grid coordinates.");
		}
	}

	/**
	 * Sends the list of client's ships to the server.
	 */
	public void sendShips() {
		try {
			objectOutputStream.writeObject(ships);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.addMessage("Unable to send ships.");
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
	 * Checks if all of the opponent's ships have been destroyed
	 * @return true if the all ships are destroyed 
	 */
	private boolean checkShips() {
		int counter = 0;

		//go through all the ships the opponent has and check if they are destroyed
		for (Ship ship : opponentShips) {
			if (ship.isDestroyed()) {
				counter++;
			}
		}
		
		//will return true when the number of ships destroyed is equal to the size of the list 
		if (counter == ships.size()) {
			return true;
		}

		return false;
	}

	/**
	 * Executes when a player has won and prompts the user if they want to play again
	 */
	public void endOfGame() {
		Message message = new Message(this.username, "");
		this.endTurn();

		//prompt user if they want to play again
		int answer = JOptionPane.showConfirmDialog(this.frame, "Would you like to play again?");

		try {
			//disconnect from the user if user doesn't want to play again
			if (answer == JOptionPane.NO_OPTION ) {
				message.setMessage("Left the game.");
				this.objectOutputStream.writeObject(message);
				this.disconnectToNetwork();
			}
			else {
				message.setPlayAgain(true);
				message.setMessage("I want to play again.");
				this.objectOutputStream.writeObject(message);
			}
		}
		catch (IOException  e) {
			this.addMessage("Unable to send message.");
			e.printStackTrace();
		}
	}
	/**
	 * Receives the opponents grid coordinates and adds them to the window.
	 * Listeners are added to buttons so that color changes when user clicks on them.
	 */
	public void addOpponentGrid() {
		try {
			//get the ArrayList from the server
			this.opponentGrid = (ArrayList<GridButton>) objectInputStream.readObject();
			JPanel panel = new JPanel(new GridLayout(10,10));
			
			//remove the previous grid 
			BorderLayout layout = (BorderLayout)this.opponentGridPanel.getLayout();
			this.opponentGridPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
			
			//go through the new grid buttons and add listeners
			for (GridButton gridButton : this.opponentGrid) {
				//set all the buttons to blue so that client can't tell where ships are
				gridButton.getButton().setBackground(Color.BLUE);
				gridButton.getButton().addActionListener((ActionEvent a) -> {
					//if the buttons hasn't been clicked before change status to clicked
					//let user know if the button has already been clicked
					if (!gridButton.isHit()) {
						String message = gridButton.clicked();
						Message attackMessage = new Message(this.username, message);
						//store the coordinates in the message
						attackMessage.setX(gridButton.getX_location());
						attackMessage.setY(gridButton.getY_location());

						//check if all the ships are destroyed
						//end game if all ships are destroyed
						if (checkShips()) {
							sendMessage(attackMessage);
							Message gameEnd = new Message(this.username, "I sunk all your ships!");
							gameEnd.setWin(true);
							this.sendMessage(gameEnd);
							endOfGame();
						}
						else {
							attackMessage.setTurn(true);
							sendMessage(attackMessage);
							this.endTurn();	
						}
					}
					else {
						this.addMessage("This has already been hit! Try again.");
					}
				});
				panel.add(gridButton.getButton());
				this.opponentGridPanel.add(panel,BorderLayout.CENTER);
				this.opponentGridPanel.revalidate();
				this.opponentGridPanel.repaint();
			}
		} catch (ClassNotFoundException e) {
			this.addMessage("Did not receive opponent grid.");
			e.printStackTrace();
		} catch (IOException e) {
			this.addMessage("Unable to send message");
			this.addMessage("Did not receive opponent grid.");
			e.printStackTrace();
		}
	}

	/**
	 * Receives list of where the opponent ships are and stores them.
	 */
	public void addOpponentShips() {
		try {
			this.opponentShips = (ArrayList<Ship>) objectInputStream.readObject();
		} catch (ClassNotFoundException e) {
			this.addMessage("Did not receive opponent ships.");
			e.printStackTrace();
		} catch (IOException e) {
			this.addMessage("Did not receive opponent ships.");
			e.printStackTrace();
		}
	}

	/**
	 * Places all the ships needed for a standard battleship game on the grid.
	 */
	public void makeShips() {
		//make another list of buttons for the grid 
		JPanel panel = new JPanel(new GridLayout(10, 10));
		playerGrid.clear();
		BorderLayout layout = (BorderLayout)this.playerPanel.getLayout();
		this.playerPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));

		for (int y= 1; y <= 10; y++)
		{

			for (int x = 1; x <= 10; x++)
			{
				JButton button = new JButton();
				GridButton gridButton = new GridButton(button, x, y);
				playerGrid.add(gridButton);
				panel.add(button);
			}
		}


		//place each ship on the grid
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

		this.playerPanel.add(panel, BorderLayout.CENTER);
		this.playerPanel.validate();
		this.playerPanel.repaint();
		//this.playerPanel.setVisible(true);

	}

	/**
	 * Places each ship on the grid randomly
	 * @param numberOfParts number of squares the ship occupies
	 * @param shipType type of ship being placed
	 * @return true if the ships is successfully made
	 */
	private boolean placeShips(int numberOfParts, String shipType) {
		ArrayList<GridButton> shipParts = new ArrayList<>();
		boolean horizontal = true;

		//get a number between 1 and 10 for the a and y coordinates
		int x_location = (int) (1 + Math.random() * 10);
		int y_location = (int) (1 + Math.random() * 10);

		//obtain a number to determine if the ship will be placed horizontally or vertically
		int num = (int) (1+ Math.random() *100);
		if (num % 2 == 0) {
			horizontal = false;
		}

		int shipStart = 0;

		//get the GridButton with the coordinates obtained
		for (GridButton grid : playerGrid) {
			if (grid.getX_location() == x_location && grid.getY_location() == y_location)
			{
				shipStart = playerGrid.indexOf(grid);
			}
		}

		//will determine if the ship can be placed on the grid depending if its occupied
		//if occupied the method returns false and the ship is not placed
		//ship is not fully placed until all parts have been placed in an unoccupied coordinate
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

		//create a ship object with the coordinates obtained
		Ship ship = new Ship(shipType);

		for (GridButton part: shipParts) {
			ship.addShipPart(part);
			part.makeShipPart(ship);
		}

		ships.add(ship); //add ship to list of player ships

		return true;
	}

	/**
	 * Displays whole window with all the panels.
	 */
	public void display() {
		//this.frame.pack();
		this.frame.setVisible(true);
	}

	/**
	 * Activates buttons when it is the client's turn to attack.
	 */
	public void playTurn() {
		for (GridButton gridButton : opponentGrid) {
			gridButton.getButton().setEnabled(true);
		}
	}

	/**
	 * Deactivates buttons when it is the opponent's turn to attack.
	 */
	public void endTurn() {
		for (GridButton gridButton : opponentGrid) {
			gridButton.getButton().setEnabled(false);
		}
	}

	/**
	 * Updates the player's grid in response to the opponent's attacks.
	 * @param x
	 * @param y
	 */
	public void updatePlayerGrid(int x, int y) {
		for (GridButton gridButton : playerGrid) {
			if (gridButton.getX_location() == x && gridButton.getY_location() == y) {
				gridButton.clicked();
			}
		}
	}

}
