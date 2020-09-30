package server;

import java.io.IOException;

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
		
		while (!this.input.getSocket().isClosed() && !this.output.getSocket().isClosed()) {
			try {

				Message message = (Message) this.input.getOis().readObject();

				this.output.getOos().writeObject(message);
				serverGUI.addMessage(this.input.getUsername() + " sent a message.");

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
