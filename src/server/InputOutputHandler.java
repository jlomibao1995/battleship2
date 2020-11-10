package server;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;

import problemdomain.Attack;
import problemdomain.GridButton;
import problemdomain.Message;

public class InputOutputHandler implements Runnable {

	private ClientConnection input;
	private ClientConnection output;
	private ServerGUI serverGUI;
	private PropertyChangeSupport pcs;

	public InputOutputHandler(ClientConnection input, ClientConnection output, ServerGUI serverGUI) {
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
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();

			}
		}

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
}
