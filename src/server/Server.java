package server;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import problemdomain.GridButton;
import problemdomain.Message;
import problemdomain.Ship;


public class Server {

	//private ArrayList<ClientConnection> connections;
	ServerGUI serverGUI;

	public Server(ServerGUI server) {

		//this.connections = new ArrayList<>();
		this.serverGUI = server;

	}

	public void connectToNetwork() throws IOException {
		
		ArrayList<ClientConnection> connections = new ArrayList<>() ;

		ServerSocket listener = new ServerSocket(1234);

		while (listener.isBound()) {
			try {
				Socket client = listener.accept();

				//server.addMessage("Client connected.");

				InputStream inputStream = client.getInputStream();
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

				OutputStream outputStream = client.getOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
				
				Message username = (Message) objectInputStream.readObject();
				this.serverGUI.addMessage(username.toString());

				System.out.println("Waiting for messages...");

				ClientConnection connection = new ClientConnection(client, objectInputStream, objectOutputStream, username.getUsername());
				connections.add(connection);

				if (connections.size() % 2 == 0) {
					ClientConnection connection1 = connections.get(0);
					ClientConnection connection2 = connections.get(1);
					
					ArrayList<GridButton> playerGrid1 = makeGrid();
					ArrayList<GridButton> playerGrid2 = makeGrid();
					
					connection1.getOos().writeObject(playerGrid1);
					connection2.getOos().writeObject(playerGrid2);
					
					GameConnection game = new GameConnection(this.serverGUI, connection1, connection2);
					Thread thread = new Thread(game);
					
					thread.start();
					
					connections.remove(connection1);
					connections.remove(connection2);
				}

			}

			catch (IOException ex) {

			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		listener.close();
	}
	
	private ArrayList<GridButton> makeGrid() {
		
		ArrayList<GridButton> playerGrid = new ArrayList<>();
		
		for (int y= 1; y <= 10; y++)
		{
			
			for (int x = 1; x <= 10; x++)
			{
				JButton button = new JButton();
				button.setBackground(Color.LIGHT_GRAY);
				playerGrid.add(new GridButton(button, x, y));
				
				
			}
		}
		
		boolean shipPlaced = false;

		while (!shipPlaced) {
			shipPlaced = placeShips(5, playerGrid, "Aircraft Carrier");
		}
		shipPlaced = false;
		while (!shipPlaced) {
			shipPlaced = placeShips(4, playerGrid, "Battleship");
		}
		shipPlaced = false;
		while (!shipPlaced) {
			shipPlaced = placeShips(3, playerGrid, "Cruiser");
		}
		shipPlaced = false;
		while (!shipPlaced) {
			shipPlaced = placeShips(3, playerGrid, "Submarine");
		}
		shipPlaced = false;
		while (!shipPlaced) {
			shipPlaced = placeShips(2, playerGrid, "Destroyer");
		}
		
		return playerGrid;
	}
	
	private boolean placeShips(int numberOfParts, ArrayList<GridButton> playerGrid, String shipType) {
		ArrayList<GridButton> shipParts = new ArrayList<>();
		boolean horizontal = true;
		
		int x_location = (int) (1 + Math.random() * 10);
		int y_location = (int) (1 + Math.random() * 10);
		
		System.out.println("\n" + x_location + " " + y_location);
		
		int num = (int) (1+ Math.random() *100);
		System.out.println(num);
		
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

}
