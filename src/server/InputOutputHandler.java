package server;

import java.io.IOException;
import java.util.ArrayList;

import problemdomain.GridButton;
import problemdomain.Message;

public class InputOutputHandler implements Runnable {

	private ClientConnection input;
	private ClientConnection output;
	private ServerGUI serverGUI;



	public InputOutputHandler(ClientConnection input, ClientConnection output, ServerGUI serverGUI) {
		this.input = input;
		this.output = output;
		this.serverGUI = serverGUI;
	}



	@Override
	public void run() {
		
		Message message = new Message("Server", "Start over.");

		while (!this.input.getSocket().isClosed() && !this.output.getSocket().isClosed()) {
			try {

				message = (Message) this.input.getOis().readObject();

					this.output.getOos().writeObject(message);
					serverGUI.addMessage(this.input.getUsername() + " sent a message.");

					if (message.getTurn()) {
						Message turnMessage = new Message("Server", "Your turn!");
						this.output.getOos().writeObject(turnMessage);
					}
					else {
						Message turnMessage = new Message("Server", "Opponent's turn.");
						this.output.getOos().writeObject(turnMessage);
					}

					if (message.getWin() && message.getWin() != null) {
						Message lostMessage = new Message("Server", this.input.getUsername() + " won the game!");
						serverGUI.addMessage(this.input.getUsername() + " won the game!");
						this.output.getOos().writeObject(lostMessage);
						break;
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

}
