package client;

import java.io.*;
import java.net.Socket;
import problemdomain.Message;

public class ServerConnection implements Runnable {

	private Socket server;
	private ClientGUI client;
	private ObjectInputStream ois;

	
	public ServerConnection(ClientGUI client, ObjectInputStream ois, Socket server) {
		this.server = server;
		this.client = client;
		this.ois = ois;
	}

	@Override
	public void run() {

		while (!server.isClosed())
		{
			try {
				Message receive = (Message) ois.readObject();
				client.addMessage(receive.toString());
				
				if (receive.getMessage().equals("Begin game") && receive.getUsername().equals("Server")) {
					client.makeShips();
					client.sendPlayerGrid();
					client.sendShips();
					client.addOpponentGrid();
					client.addOpponentShips();
				}
				
				if (receive.getX() != null && receive.getY() != null) {
					client.updatePlayerGrid(receive.getX(), receive.getY());
					client.playTurn();
				}
				
				if (receive.getTurn() != null && receive.getUsername().equals("Server") && receive.getTurn()) {
					client.playTurn();
				}
				
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
