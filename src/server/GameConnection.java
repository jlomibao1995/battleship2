package server;

import java.io.IOException;
import java.util.ArrayList;

import problemdomain.GridButton;
import problemdomain.Message;
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
		while (!connection1.getSocket().isClosed() && !connection2.getSocket().isClosed()) {
			
			Message message = new Message("Server", "Begin game");
			serverGUI.addMessage("Game Started!");
			
			try {
				this.connection1.getOos().writeObject(message);
				this.connection2.getOos().writeObject(message);
			} catch (IOException e1) {
				e1.printStackTrace();

			}
			
			this.sendGrids();
			this.sendShips();
			this.determineTurn();
			
			//start threads that will handle sending messages to each client
			InputOutputHandler  ioHandler1 = new InputOutputHandler(this.connection2, this.connection1, this.serverGUI);
			this.thread1 = new Thread(ioHandler1);
			thread1.start();
			
			InputOutputHandler  ioHandler2 = new InputOutputHandler(this.connection1, this.connection2, this.serverGUI);
			this.thread2 = new Thread(ioHandler2);
			thread2.start();	

			try {
				thread1.join();
				thread2.join();
			} 
			catch (InterruptedException e) {

				serverGUI.addMessage("Game ended between " + connection1.getUsername() + " and " + connection2.getUsername());
				e.printStackTrace();
			}
			
			//when game ends and the other cient is still connected find the next client and connect them
			ClientConnection newConnection = null;
			Message newGame = new Message("Server", "Waiting for connection...");
			
			try {
				if (connection1.getSocket().isClosed() && !connection2.getSocket().isClosed()) {
					connection2.getOos().writeObject(newGame);
					
					while (newConnection == null) {
						newConnection = server.getConnection();
					}
					
					connection1 = newConnection;
				}
				else if (connection2.getSocket().isClosed() && !connection1.getSocket().isClosed()) {
					connection1.getOos().writeObject(newGame);
					while (newConnection == null) {
						newConnection = server.getConnection();
					}
					
					connection2 = newConnection;
				}
				
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	/**
	 * Sends client grid to the other client.
	 */
	private void sendGrids() {
		try {
			ArrayList<GridButton> playerGrid1 = (ArrayList<GridButton>)  this.connection1.getOis().readObject();
			this.connection2.getOos().writeObject(playerGrid1);
			
			ArrayList<GridButton> playerGrid2 = (ArrayList<GridButton>)  this.connection2.getOis().readObject();
			this.connection1.getOos().writeObject(playerGrid2);
			
			serverGUI.addMessage("Sent player grids.");
		} catch (ClassNotFoundException e1) {
			serverGUI.addMessage("Unable to send grids.");
			e1.printStackTrace();
		} catch (IOException e1) {
			serverGUI.addMessage("Unable to send grids.");
			e1.printStackTrace();
		}
	}
	
	/**
	 * Sends client ships to the other client.
	 */
	private void sendShips() {
		try {
			ArrayList<Ship> ships1 = (ArrayList<Ship>) this.connection1.getOis().readObject();
			this.connection2.getOos().writeObject(ships1);
			
			ArrayList<Ship> ships2 = (ArrayList<Ship>) this.connection2.getOis().readObject();
			this.connection1.getOos().writeObject(ships2);
		} catch (ClassNotFoundException e1) {
			serverGUI.addMessage("Unable to send ships.");
			e1.printStackTrace();
		} catch (IOException e1) {
			serverGUI.addMessage("Unable to send grids.");
			e1.printStackTrace();
		}
	}
	
	/**
	 * Determines which player goes first.
	 */
	private void determineTurn() {
		int turn = (int) Math.random();

		try {
			Message turnMessage1 = new Message("Server", "You have the first move.");
			turnMessage1.setTurn(true);
			if (turn % 2 == 0) {
				connection1.getOos().writeObject(turnMessage1);
				Message turnMessage2 = new Message("Server", connection1.getUsername() + " gets the first move.");
				connection2.getOos().writeObject(turnMessage2);
			}
			else {
				connection2.getOos().writeObject(turnMessage1);
				Message turnMessage2 = new Message("Server", connection2.getUsername() + " gets the first move.");
				connection1.getOos().writeObject(turnMessage2);
			}
		}
		catch (IOException e) {
			serverGUI.addMessage("Unable to determine player turn.");
			e.printStackTrace();
		}
	}
	
	
}
