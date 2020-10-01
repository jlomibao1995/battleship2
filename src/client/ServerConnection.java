package client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import problemdomain.GridButton;
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
				
				if (receive.getMessage().equals("Begin game")) {
					client.makeShips();
					client.sendPlayerGrid();
					client.addOpponentShips();
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
