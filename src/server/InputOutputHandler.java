package server;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

import problemdomain.Attack;
import problemdomain.GridButton;
import problemdomain.Message;
import problemdomain.PlayerGrid;

public class InputOutputHandler implements Runnable {

	private ClientConnection input;
	private ClientConnection output;
	private ServerGUI serverGUI;
	private PropertyChangeSupport pcs;

	public InputOutputHandler (ClientConnection input, ClientConnection output, ServerGUI serverGUI) {
		this.input = input;
		this.output = output;
		this.serverGUI = serverGUI;
		this.pcs = new PropertyChangeSupport(this);
	}

	@Override
	public void run() {

		while (!this.input.getSocket().isClosed() && !this.output.getSocket().isClosed()) {
			try {

				Object receive = this.input.getOis().readObject();

				if (receive instanceof Attack) {					
					
					this.pcs.firePropertyChange("Attack", null, receive);
				} else {
					this.output.getOos().writeObject(receive);
				}

			}
			catch (SocketException e) {
				break;
			}
			catch (EOFException e) {
				break;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();

			}
		}

		System.out.println("Thread done.");
	}
	
	public void attachObserver(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
	
	public void sendToPlayer(Attack attack) {
		try {
			this.output.getOos().writeObject(attack);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendBackToOpponent(Attack attack) {
		try {
			this.input.getOos().writeObject(attack);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void playAgain(PlayerGrid playerGrid) {
		String player1 = playerGrid.getPlayer1Name();
		Message message = new Message("Server", "Begin game");
		int rand = (int) Math.random();
		Message turnMessage = new Message("Server", "Your turn");
		
		try {

			if (this.output.getUsername().equals(player1)) {
				this.output.getOos().writeObject(playerGrid.getPlayer1Grid());
				this.input.getOos().writeObject(playerGrid.getPlayer2Grid());
			} else {
				this.output.getOos().writeObject(playerGrid.getPlayer2Grid());
				this.input.getOos().writeObject(playerGrid.getPlayer1Grid());
			}
			
			this.output.getOos().writeObject(message);
			this.input.getOos().writeObject(message);
			
			if (rand % 2 == 0) {
				this.output.getOos().writeObject(turnMessage);
			} else {
				this.input.getOos().writeObject(turnMessage);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeSocket(String username) {
		this.serverGUI.addMessage("Player " + username +  " has exited the game.");
		ClientConnection quit;
		ClientConnection play;
		
		try {
			if (this.output.getUsername().equals(username)) {
				quit = this.output;
				play = this.input;
			} else {
				quit = this.input;
				play = this.output;
			}
			
			play.getOos().writeObject(new Message("xxxx0000", "Player " + username +  " has exited the game."));
			quit.getSocket().close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
