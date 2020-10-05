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

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import problemdomain.GridButton;
import problemdomain.Message;
import problemdomain.Ship;

public class ClientGUI {
	private JFrame frame;

	private JList chatList;
	private DefaultListModel chatListModel;
	JTextField inputMessage;

	private String username;
	Socket socket;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;

	private ArrayList<Ship> ships;
	private ArrayList<Ship> opponentShips;
	private ArrayList<GridButton> playerGrid;
	private ArrayList<GridButton> opponentGrid;
	JPanel opponentGridPanel;
	JPanel playerPanel;

	private static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
	private static String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

	public ClientGUI() {

		playerGrid = new ArrayList<>();
		ships = new ArrayList<>();
		opponentShips = new ArrayList<>();

		this.frame = new JFrame("Battleship");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(600, 800);
		this.frame.setLayout(new BorderLayout());

		JPanel gamePanel = new JPanel(new GridLayout(2, 1));
		gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10 ,40));

		createClientPanel();
		createOpponentPanel();
		JPanel chatPanel = createChatPanel();

		gamePanel.add(this.playerPanel);
		gamePanel.add(this.opponentGridPanel);

		this.frame.add(gamePanel, BorderLayout.CENTER);
		this.frame.add(chatPanel, BorderLayout.SOUTH);

		display();

		username = JOptionPane.showInputDialog(this.frame, "Enter username: ");
	}

	private void createClientPanel()
	{
		JPanel centerPanel = new JPanel(new GridLayout(10,10));
		this.playerPanel = new JPanel(new BorderLayout(10, 10));
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
		
		for (int y= 1; y <= 10; y++)
		{

			for (int x = 1; x <= 10; x++)
			{
				JButton button = new JButton();
				button.setBackground(Color.LIGHT_GRAY);
				GridButton gridButton = new GridButton(button, x, y);
				playerGrid.add(gridButton);
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

	private void createOpponentPanel()
	{
		this.opponentGridPanel = new JPanel(new BorderLayout(10, 10));
		JPanel centerPanel = new JPanel(new GridLayout(10,10));
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
		
		for (int y= 1; y <= 10; y++)
		{

			for (int x = 1; x <= 10; x++)
			{
				JButton button = new JButton();
				button.setBackground(Color.LIGHT_GRAY);
				centerPanel.add(button);
			}
		}

		JLabel title = new JLabel("Opponent's Grid", SwingConstants.CENTER);

		this.opponentGridPanel.add(title, BorderLayout.NORTH);
		this.opponentGridPanel.add(letterPanel, BorderLayout.WEST);
		this.opponentGridPanel.add(numberPanel, BorderLayout.SOUTH);
		this.opponentGridPanel.add(centerPanel, BorderLayout.CENTER);
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
			socket.close();
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendShips() {
		try {
			objectOutputStream.writeObject(ships);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addMessage(String message) {

		this.chatListModel.addElement(message);
	} 

	private boolean checkShips() {
		int counter = 0;

		for (Ship ship : opponentShips) {
			if (ship.isDestroyed()) {
				counter++;
			}
		}

		if (counter == ships.size()) {
			return true;
		}

		return false;
	}

	public void endOfGame() {
		Message message = new Message(this.username, "");

		int answer = JOptionPane.showConfirmDialog(this.frame, "Would you like to play again?");

		if (answer == JOptionPane.NO_OPTION ) {
			this.disconnectToNetwork();
		}
		
		try {
			this.objectOutputStream.writeObject(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addOpponentGrid() {
		try {
			this.opponentGrid = (ArrayList<GridButton>) objectInputStream.readObject();

			JPanel panel = new JPanel(new GridLayout(10, 10));
			
			BorderLayout layout = (BorderLayout)this.opponentGridPanel.getLayout();
			this.opponentGridPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));

			for (GridButton grid : opponentGrid) {
				grid.getButton().addActionListener((ActionEvent a) -> {
					if (!grid.isHit()) {
						String message = grid.clicked();
						Message attackMessage = new Message(this.username, message);
						attackMessage.setX(grid.getX_location());
						attackMessage.setY(grid.getY_location());
						attackMessage.setTurn(true);

						if (checkShips()) {
							Message gameEnd = new Message(this.username, "End of game!");
							gameEnd.setWin(true);
							endOfGame();
							try {
								objectOutputStream.writeObject(gameEnd);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						sendMessage(attackMessage);
						this.endTurn();
					}
					else {
						this.addMessage("This has already been hit! Try again.");
					}
				});
				panel.add(grid.getButton());
			}

			this.opponentGridPanel.add(panel, BorderLayout.CENTER);
			this.endTurn();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addOpponentShips() {
		try {
			this.opponentShips = (ArrayList<Ship>) objectInputStream.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void makeShips() {
		JPanel panel = new JPanel(new GridLayout(10, 10));
		playerGrid.clear();
		BorderLayout layout = (BorderLayout)this.playerPanel.getLayout();
		this.playerPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
		
		for (int y= 1; y <= 10; y++)
		{

			for (int x = 1; x <= 10; x++)
			{
				JButton button = new JButton();
				button.setBackground(Color.LIGHT_GRAY);
				GridButton gridButton = new GridButton(button, x, y);
				playerGrid.add(gridButton);
				panel.add(button);
			}
		}
		
		
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

		Ship ship = new Ship(shipType);

		for (GridButton part: shipParts) {
			ship.addShipPart(part);
			part.makeShipPart(ship);
		}

		ships.add(ship);

		return true;
	}

	public void display() {
		//this.frame.pack();
		this.frame.setVisible(true);
	}

	public synchronized void playTurn() {
		for (GridButton gridButton : opponentGrid) {
			gridButton.getButton().setEnabled(true);
		}
	}

	public synchronized void endTurn() {
		for (GridButton gridButton : opponentGrid) {
			gridButton.getButton().setEnabled(false);
		}
	}

	public void updatePlayerGrid(int x, int y) {
		for (GridButton gridButton : playerGrid) {
			if (gridButton.getX_location() == x && gridButton.getY_location() == y) {
				gridButton.clicked();
			}
		}
	}

}
