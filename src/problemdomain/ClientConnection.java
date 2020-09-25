package problemdomain;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import gui.ClientGUI;

public class ClientConnection implements Runnable {

	ClientGUI client;
	Socket socket;
	private OutputStream os;
	private ObjectOutputStream oos;
	private InputStream is;
	private ObjectInputStream ois;

	public ClientConnection (Socket socket, ClientGUI client) {

		this.socket = socket;
		this.client = client;
	}

	public void sendMessageToServer(Message message) {

		try {
			oos.writeObject(message);
		} 
		catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public Message receiveMessageFromServer() {
		
		Message input = new Message("Server", "Unable to send message");
		try {
			input = (Message) ois.readObject();
			client.addMessage(input);
		} 
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return input;
	}

	@Override
	public void run() {
		 
		try {
			
			os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);

			is = socket.getInputStream();
			ois = new ObjectInputStream(ois);

			while (ois.available() == 0) {

				try {
					Thread.sleep(1);
				} 
				catch (InterruptedException e) {

					e.printStackTrace();
					close();
				}
			}

			while (socket.isConnected()) {

				receiveMessageFromServer();	
			}
			 
		 }
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		
		try {
			
			oos.close();
			ois.close();
			socket.close();
			
		} 
		catch (IOException e) {

			e.printStackTrace();
		}
	}
}

