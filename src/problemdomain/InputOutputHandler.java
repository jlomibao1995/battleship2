package problemdomain;

import java.io.IOException;

public class InputOutputHandler implements Runnable {

	private ClientConnection input;
	private ClientConnection output;
	
	public InputOutputHandler(ClientConnection input, ClientConnection output) {

		this.input = input;
		this.output = output;
	}
	
	@Override
	public void run() {
		
		while (!input.getSocket().isClosed() && !output.getSocket().isClosed()) {
			try {

				Message message1 = (Message) input.getOis().readObject();

				output.getOos().writeObject(message1);

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
