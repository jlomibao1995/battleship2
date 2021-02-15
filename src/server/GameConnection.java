package server;

import java.io.IOException;
import java.util.ArrayList;

import problemdomain.GridButton;
import problemdomain.Message;
import problemdomain.PlayerGrid;
import problemdomain.Ship;

/**
 * Contains the streams and sockets for the clients that are connected.
 * @author Jean
 * @version October 6, 2020
 *
 */
public class GameConnection implements Runnable {
	
	private ClientConnection connection1;
	private ClientConnection connection2;
	private Server server;
	private ServerGUI serverGUI;
	private Thread thread1;
	private Thread thread2;
	
	/**
	 * User-defined constructor for the class.
	 * @param server server class containing connection to each client
	 * @param serverGUI the server GUI
	 * @param connection1 client socket and streams
	 * @param connection2 client socket and streams
	 */
	public GameConnection(Server server, ServerGUI serverGUI, ClientConnection connection1, ClientConnection connection2) {

		this.server = server;
		this.connection1 = connection1;
		this.connection2 = connection2;
		this.serverGUI = serverGUI;
	}

	@Override
	/**
	 * Starts the game and starts a thread for sending messages from the client to client
	 */
	public void run() {	
		
		//will run as long as the sockets are connected
		while (!connection1.getSocket().isClosed() || !connection2.getSocket().isClosed()) {
			
			PlayerGrid newGame = new PlayerGrid(this.connection1.getUsername(), this.connection2.getUsername());
			
			Message message = new Message("Server", "Begin game");
			serverGUI.addMessage("Game Started!");
			Message turnMessage = new Message("Server", "Your turn");
			
			int rand = (int) Math.random();
			
			try {
				this.connection1.getOos().writeObject(message);
				this.connection1.getOos().writeObject(newGame.getPlayer1Grid());
				this.connection2.getOos().writeObject(message);
				this.connection2.getOos().writeObject(newGame.getPlayer2Grid());
				
				if (rand % 2 == 0) {
					this.connection2.getOos().writeObject(turnMessage);
				}
				else {
					this.connection1.getOos().writeObject(turnMessage);
				}
			} catch (IOException e1) {
				e1.printStackTrace();

			}
			
			//start threads that will handle sending messages to each client
			InputOutputHandler  ioHandler1 = new InputOutputHandler(this.connection2, this.connection1, this.serverGUI);
			ioHandler1.attachObserver(newGame);
			this.thread1 = new Thread(ioHandler1);
			thread1.start();
			
			InputOutputHandler  ioHandler2 = new InputOutputHandler(this.connection1, this.connection2, this.serverGUI);
			ioHandler2.attachObserver(newGame);
			this.thread2 = new Thread(ioHandler2);
			thread2.start();	

			try {
				thread1.join();
				thread2.join();
				serverGUI.addMessage("Game ended between " + connection1.getUsername() + " and " + connection2.getUsername());
				
				while (this.connection1.getSocket().isClosed() || this.connection2.getSocket().isClosed()) {
					ClientConnection newConnection = this.server.getConnection();
					
					if (newConnection != null) {
						if (this.connection1.getSocket().isClosed()) {
							this.connection1 = newConnection;
						} else {
							this.connection2 = newConnection;
						}
					}
				}
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
