package client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import problemdomain.Attack;
import problemdomain.GridButton;
import problemdomain.Message;
import problemdomain.PlayerGrid;

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
				Object receive = ois.readObject();
				
				if (receive instanceof Message) {
					Message message = (Message) receive;
					if (message.getUsername().equals("xxxx0000")) {
						this.client.sendMessage(new Message(this.client.getUsername(), "Ready to play!"));
					}
					this.client.addMessage(message.toString());
					
					if (message.getMessage().equals("Your turn"))
						this.client.turn();
				}
				else if (receive instanceof ArrayList<?>) {
					this.client.displayShips((ArrayList<GridButton>) receive);
				}
				else if (receive instanceof Attack) {
					Attack attack = (Attack) receive;
					this.client.updatetGrid(attack, attack.getUsername());	
				}
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch (SocketException e) {
				this.client.addMessage("Disconnected to server");
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

}
