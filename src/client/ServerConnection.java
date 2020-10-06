package client;

import java.io.*;
import java.net.Socket;
import problemdomain.Message;

/**
 * Class for the connection between the client and the server
 * @author Jean
 *@version October 6, 2020
 */
public class ServerConnection implements Runnable {

	private Socket server;
	private ClientGUI client;
	private ObjectInputStream ois;

	
	/**
	 * Constructor for the class.
	 * @param client ClientGUI 
	 * @param ois sends objects to the server
	 * @param server socket which connects the client and server
	 */
	public ServerConnection(ClientGUI client, ObjectInputStream ois, Socket server) {
		this.server = server;
		this.client = client;
		this.ois = ois;
	}

	@Override
	/**
	 * Receives messages from the server when the socket is open
	 */
	public void run() {

		while (!server.isClosed())
		{
			try {
				Message receive = (Message) ois.readObject();
				client.addMessage(receive.toString());
				
				//when server signals to begin the game ships are placed and sent
				if (receive.getMessage().equals("Begin game") && receive.getUsername().equals("Server")) {
					client.makeShips();
					client.sendPlayerGrid();
					client.sendShips();
					client.addOpponentGrid();
					client.addOpponentShips();
				}
				
				//if there are coordinates in the message update the player's grid
				if (receive.getX() != null && receive.getY() != null) {
					client.updatePlayerGrid(receive.getX(), receive.getY());
					client.playTurn();
				}
				
				if (receive.getTurn() != null && receive.getUsername().equals("Server") && receive.getTurn()) {
					client.playTurn();
				}
				
				//when the message signals that someone has won end the game
				if (receive.getWin()) {
					client.endTurn();
					client.endOfGame();
				}
				
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

}
